package com.example.payment_service.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSigner {

    public static String calculateHmac(String secret,String payload) throws NoSuchAlgorithmException, InvalidKeyException {

        String algorithm = "HmacSHA256";

        SecretKeySpec spec = new SecretKeySpec(

                secret.getBytes(StandardCharsets.UTF_8),
                algorithm
        );

        Mac mac = Mac.getInstance(algorithm);
        mac.init(spec);

        byte[] hmacBytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        return byteToHex(hmacBytes);
    }

    private static String byteToHex(byte[] bytes){

        StringBuilder sb = new StringBuilder();

        for(byte b : bytes){

            sb.append(String.format("%02x",b));
        }

        return sb.toString();

    }
}
