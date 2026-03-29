package com.securetrade.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// utility class for generating and reading JWT tokens

@Component
public class JwtUtil {

    // secret key used to sign the token
    // should be at least 32 characters for HMAC-SHA256
    private final String SECRET = "mysecretkeymysecretkeymysecretkey12345";

    // convert secret string to a Key object
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // generate JWT token for a given username
    // token expires in 1 hour (1000ms * 60 * 60)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey())
                .compact();
    }

    // extract username from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}