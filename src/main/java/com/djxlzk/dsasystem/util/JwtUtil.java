package com.djxlzk.dsasystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类，用于生成和验证JWT令牌
 */
public class JwtUtil {
    /**
     * JWT密钥，实际项目中应该从配置文件读取
     */
    private static final String SECRET_KEY = "driving-school-app-secret-key-2026";

    /**
     * 过期时间：2小时
     */
    private static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

    /**
     * 生成JWT令牌
     * 
     * @param userId 用户ID
     * @param role   用户角色
     * @return JWT令牌
     */
    public static String generateToken(Long userId, String role) {
        // 创建声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);

        // 生成令牌
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析JWT令牌
     * 
     * @param token JWT令牌
     * @return 令牌中的声明
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证JWT令牌
     * 
     * @param token JWT令牌
     * @return 是否有效
     */
    public static boolean verifyToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从令牌中获取用户ID
     * 
     * @param token JWT令牌
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object idObj = claims.get("userId");
        return idObj instanceof Number ? ((Number) idObj).longValue() : null;
    }

    /**
     * 从令牌中获取用户角色
     * 
     * @param token JWT令牌
     * @return 用户角色
     */
    public static String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("role");
    }
}