package com.fdbatt.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 获取 Authorization Header
        String authHeader = request.getHeader("Authorization");

        // 2. 判断是否携带 Bearer Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                // 3. 解析 Token
                Claims claims = JwtUtil.parseToken(token);
                String username = claims.getSubject();

                // 4. 如果 SecurityContext 里还没有认证信息
                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    // ⚠️ 这里暂时写死一个 ROLE_USER
                    // 后续 Phase 会改成从数据库 / JWT 中取权限
                    List<SimpleGrantedAuthority> authorities =
                            Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_USER")
                            );

                    // 5. 构建 Authentication
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    authorities
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // 6. 放入 SecurityContext
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }

            } catch (Exception e) {
                // Token 解析失败，直接放行，由 Spring Security 处理
            }
        }

        // 7. 放行请求
        filterChain.doFilter(request, response);
    }
}
