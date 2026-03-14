package com.djxlzk.dsasystem.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类，用于密码加密和验证
 */
public class PasswordUtil {
    /**
     * BCrypt密码编码器
     */
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 对密码进行加密
     * 
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码是否匹配
     * 支持明文密码和 BCrypt 哈希密码
     * 
     * @param rawPassword     原始密码
     * @param encodedPassword 存储的密码（可能是明文或哈希）
     * @return 是否匹配
     */
    public static boolean match(String rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        
        // 如果是 BCrypt 哈希格式，使用加密验证
        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$")) {
            return encoder.matches(rawPassword, encodedPassword);
        } else {
            // 否则直接字符串比较（明文密码）
            return rawPassword.equals(encodedPassword);
        }
    }
}