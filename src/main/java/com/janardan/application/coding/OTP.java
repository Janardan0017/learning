package com.janardan.application.coding;

import java.util.Random;

/**
 * Created for janardan on 27/10/20
 */
public class OTP {

    private static String generateOTP(int len) {
        // Using numeric values
        String numbers = "012";

        // Using random method
        Random random = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            int i1 = random.nextInt(numbers.length());
//            System.out.println(i1);
            otp[i] = numbers.charAt(i1);
        }
        return String.valueOf(otp);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String s = generateOTP(4);
            System.out.println(s);
        }
    }
}
