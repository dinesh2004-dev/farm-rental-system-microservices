package com.example.payment_service.service.impl;



import com.example.payment_service.dtos.CreateOrderDTO;
import com.example.payment_service.dtos.PaymentVerifyDTO;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.exceptions.PaymentGatewayException;
import com.example.payment_service.exceptions.PaymentNotFound;
import com.example.payment_service.exceptions.PaymentNotInitiated;
import com.example.payment_service.repository.PaymentRepository;
import com.example.payment_service.service.PaymentService;
import com.example.payment_service.util.OutboxUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.apache.commons.codec.binary.Hex;
import org.example.events.BookingCreatedEvent;
import org.example.events.PaymentFailedEvent;
import org.example.events.PaymentSuccessEvent;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;



@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key}")
    private String RAZORPAY_KEY;
    @Value("${razorpay.secret}")
    private String RAZORPAY_SECRET;

    private final PaymentRepository paymentRepository;
    private final OutboxUtil outboxUtil;



    public PaymentServiceImpl(PaymentRepository paymentRepository,OutboxUtil outboxUtil) {
        this.paymentRepository = paymentRepository;
        this.outboxUtil = outboxUtil;

    }

    @Override
    public CreateOrderDTO createOrder(int bookingId) throws PaymentNotFound,
            PaymentNotInitiated,PaymentGatewayException {


        Payment payment = paymentRepository.findByBookingId(bookingId);

        if(Objects.isNull(payment)){
            throw new PaymentNotFound("Payment not found for booking id: " + bookingId);
        }
        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Payment already completed");
        }

        if(payment.getPaymentStatus() != PaymentStatus.INITIATED){
            if (payment.getRazorpayOrderId() != null) {
                return existingOrderDTO(payment);
            }

            throw new PaymentNotInitiated("Payment is not in INITIATED state for booking id: " + bookingId);
        }
        try {
            RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount",convertAmountToPaise(payment.getAmount()));
            orderRequest.put("currency","INR");
            orderRequest.put("receipt","receipt#booking_" + bookingId + "_payment_" + payment.getId()
            );
            Order order = razorpayClient.orders.create(orderRequest);

            updatePaymentWithOrder(payment, order);

            CreateOrderDTO createOrderDTO = new CreateOrderDTO();
            createOrderDTO.setOrderId(order.get("id"));
            createOrderDTO.setAmount(order.get("amount"));
            createOrderDTO.setCurrency(order.get("currency"));
            createOrderDTO.setKey(RAZORPAY_KEY);
            return createOrderDTO;


        } catch (RazorpayException e) {
            throw new PaymentGatewayException("Order creation failed", e);
        }


    }

    private int convertAmountToPaise(BigDecimal amount){
        return amount
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValueExact();
    }

    @Override
    @Transactional
    public void verifyPayment(PaymentVerifyDTO paymentVerifyDTO) throws PaymentNotFound{
        // Implementation for payment verification
        Payment payment = paymentRepository.findPaymentByRazorpayOrderId(
                paymentVerifyDTO.getRazorpayOrderId()
        );

        if(Objects.isNull(payment)){

            throw new PaymentNotFound("Payment not found for order id: " + paymentVerifyDTO.getRazorpayOrderId());
        }

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            return;
        }

        String payload = paymentVerifyDTO.getRazorpayOrderId() + '|' + paymentVerifyDTO.getRazorpayPaymentId();

        String expectedSignature =
                hmacSha256(payload, RAZORPAY_SECRET);

        if(!expectedSignature.equals(paymentVerifyDTO.getRazorpaySignature())){
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            outboxUtil.publishOutboxEvents(
                    "Payment_Failed",
                    payment,
                    new PaymentFailedEvent(
                            payment.getBookingId(),
                            payment.getId(),
                            payment.getIdempotencyKey(),
                            "Signature verification failed",
                            Instant.now()
                    )
            );

            return;
        }

        payment.setRazorpayPaymentId(paymentVerifyDTO.getRazorpayPaymentId());
        payment.setRazorpaySignature(paymentVerifyDTO.getRazorpaySignature());
        payment.setPaymentStatus(PaymentStatus.PAID);


        paymentRepository.save(payment);

        outboxUtil.publishOutboxEvents(
                "Payment_Success",
                payment,
                new PaymentSuccessEvent(
                        payment.getBookingId(),
                        payment.getId(),
                        payment.getIdempotencyKey(),
                        Instant.now()
                )
        );
    }

    private String hmacSha256(String data,String secret)  {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hasBytes = mac.doFinal(data.getBytes());

            return Hex.encodeHexString(hasBytes);
        }
        catch (Exception e){
            throw new RuntimeException("Signature verification failed", e);
        }

    }

    @Transactional
    public void updatePaymentWithOrder(Payment payment, Order order) {
        payment.setRazorpayOrderId(order.get("id"));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
    }

    private CreateOrderDTO existingOrderDTO(Payment payment) {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setOrderId(payment.getRazorpayOrderId());
        dto.setAmount(convertAmountToPaise(payment.getAmount()));
        dto.setCurrency("INR");
        dto.setKey(RAZORPAY_KEY);
        return dto;
    }

    @Override
    public int initiatePayment(BookingCreatedEvent event){

        Optional<Payment> existing =
                paymentRepository.findByIdempotencyKey(event.getSagaId());

        if (existing.isPresent()) {

            return existing.get().getId();
        }


        Payment payment = new Payment();
        payment.setBookingId(event.getBookingId());
        payment.setAmount(BigDecimal.valueOf(event.getAmount()));
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        payment.setPayerId(event.getRenterId());
        payment.setLenderId(event.getLenderId());
        payment.setIdempotencyKey(event.getSagaId());

        paymentRepository.save(payment);


        return payment.getId();


    }
}
