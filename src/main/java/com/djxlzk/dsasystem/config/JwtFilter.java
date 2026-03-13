package com.djxlzk.dsasystem.config;

import com.djxlzk.dsasystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = JwtUtil.parseToken(token);
                Long userId = (Long) claims.get("userId");
                String role = (String) claims.get("role");
                Authentication authentication = new UsernamePasswordAuthenticationToken(userId, role);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // token无效，不做处理
            }
        }
        filterChain.doFilter(request, response);
    }
}