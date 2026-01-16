package com.fdbatt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    /**
     * Token 过期时间（毫秒）
     * 1 小时
     */
    private static final long EXPIRE_TIME = 60 * 60 * 1000;

    /**
     * 密钥（⚠️ 必须 >= 32 字节）
     */
    private static final String SECRET =
            "workflow-secret-key-1234567890-abcdef";

    /**
     * 签名 Key
     */
    private static final Key KEY = Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
    );

    /**
     * 生成 JWT Token
     *
     * @param username 用户名
     * @return token 字符串
     */
    public static String generateToken(String username) {

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token
     *
     * @param token JWT
     * @return Claims
     */
    public static Claims parseToken(String token) {

        return Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从 Token 中获取用户名
     */
    public static String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 判断 Token 是否过期
     */
    public static boolean isExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
