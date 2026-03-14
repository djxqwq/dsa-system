package com.djxlzk.dsasystem;

import com.djxlzk.dsasystem.util.PasswordUtil;

public class TestPassword {
    public static void main(String[] args) {
        String rawPassword = "Aa123456";
        String storedHash = "$2a$10$8K3ds9GLcTqMkqkXFrbEO4tY1u5w2wQv3rXp1MqJc8e9T8wL5fO";
        
        System.out.println("原始密码: " + rawPassword);
        System.out.println("存储的哈希: " + storedHash);
        System.out.println("密码匹配结果: " + PasswordUtil.match(rawPassword, storedHash));
        
        // 生成新的哈希进行对比
        String newHash = PasswordUtil.encrypt(rawPassword);
        System.out.println("新生成的哈希: " + newHash);
        System.out.println("新哈希匹配结果: " + PasswordUtil.match(rawPassword, newHash));
    }
}
