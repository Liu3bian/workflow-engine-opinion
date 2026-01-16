package com.fdbatt.controller;

import com.fdbatt.config.JwtUtil;
import com.fdbatt.dto.LoginRequest;
import com.fdbatt.vo.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        try {
            // 1. 构造认证对象
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    );

            // 2. 交给 Spring Security 认证
            Authentication authentication =
                    authenticationManager.authenticate(authToken);

            // 3. 认证成功，生成 JWT
            String token = JwtUtil.generateToken(request.getUsername());

            // 4. 返回 Token
            return new LoginResponse(token);

        } catch (AuthenticationException e) {
            // 认证失败
            throw new RuntimeException("用户名或密码错误");
        }
    }
    @GetMapping("success")
    public String test() {
        return "success hello world";
    }
}
