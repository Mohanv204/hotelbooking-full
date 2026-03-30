package com.example.hotelbooking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expirationMs}") long expirationMs
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(Long userId, String email, List<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))       // was setSubject()
                .claim("email", email)
                .claim("roles", roles)
                .issuedAt(now)                         // was setIssuedAt()
                .expiration(expiry)                    // was setExpiration()
                .signWith(signingKey)                  // algorithm auto-detected from key type
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)                // was setSigningKey()
                .build()
                .parseSignedClaims(token)              // was parseClaimsJws()
                .getPayload();                         // was getBody()
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Long getUserId(Claims claims) {
        return Long.parseLong(claims.getSubject());
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        Object roles = claims.get("roles");
        return roles == null ? List.of() : (List<String>) roles;
    }
}