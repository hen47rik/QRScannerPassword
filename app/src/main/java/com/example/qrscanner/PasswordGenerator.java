package com.example.qrscanner;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final double OTP_LENGTH = 8;
    int generatePassword(String password){
        SecureRandom random = new SecureRandom();
        int min = (int) Math.pow(10, OTP_LENGTH - 1);
        int max = (int) Math.pow(10, OTP_LENGTH) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}
