package com.djxlzk.dsasystem;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Aa123456";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("BCrypt encoded password: " + encodedPassword);
    }
}
