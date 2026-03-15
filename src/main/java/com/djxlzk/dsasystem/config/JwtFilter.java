package com.djxlzk.dsasystem.config;

import com.djxlzk.dsasystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = JwtUtil.parseToken(token);
                Object idObj = claims.get("userId");
                Long userId = idObj instanceof Number ? ((Number) idObj).longValue() : null;
                String role = (String) claims.get("role");
                String authority = "ROLE_" + (role != null ? role.toUpperCase() : "");
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId, role, Collections.singletonList(new SimpleGrantedAuthority(authority)));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // token无效，不做处理
            }
        }
        filterChain.doFilter(request, response);
    }
}