async function payNow(){

    const token = localStorage.getItem("jwt_token");


     const response = await fetch("http://localhost:8088/api/payments/create-order",
     {
     method: "post",
     headers:{
     "Authorization": "Bearer " + token,
     "Content-Type":"application/json"}
     ,
     body:JSON.stringify({
     bookingId: 53
     })
     });
      const orderData = await response.json();

       openRazorPay(orderData);
}
function openRazorPay(orderData){

const options = {
     key : orderData.key,
     amount : orderData.amount,
     currency : orderData.currency,
     order_id : orderData.orderId,
     name: "Farm Rental System",
         description: "Equipment Booking Payment",
        handler: function (response){
              verifyPayment(response);
        },
        theme: {
              color: "#3399cc"
            }

  };
const rzp = new Razorpay(options);
rzp.open();
}

async function verifyPayment(response) {
    const token = localStorage.getItem("jwt_token");
  const res = await fetch("http://localhost:8088/api/payments/verify", {
    method: "POST",
    headers: {
        "Authorization": "Bearer " + token,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      razorpayPaymentId: response.razorpay_payment_id,
      razorpayOrderId: response.razorpay_order_id,
      razorpaySignature: response.razorpay_signature
    })
  });

  if(res.ok){
    alert("Payment Successful!");
  }
  else{
  alert("Payment Verification Failed!");
  }
}
