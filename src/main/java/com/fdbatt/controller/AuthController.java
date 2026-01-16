package com.fdbatt.controller;

import com.fdbatt.config.JwtUtil;
import com.fdbatt.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(), req.getPassword()
                )
        );

        String token = JwtUtil.createToken(req.getUsername());

        Map<String, String> res = new HashMap<>();
        res.put("token", token);
        return res;
    }

    @GetMapping("test")
    public String test() {
        return "success curl";
    }
}
