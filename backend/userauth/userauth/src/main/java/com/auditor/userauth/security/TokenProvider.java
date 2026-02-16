package com.auditor.userauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {

    // 1. Secret String (Must be at least 32 characters for HS256)
    private final String SECRET_STRING = "9a4f641d668f4e56877960784a0d84269a4f641d668f4e56877960784a0d8426";

    // 2. Convert string to SecretKey object (Modern 0.12.x way)
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    // 3. Create Token (Used in Login)
    public String createToken(Long userId, String email) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 Hours
                .signWith(SECRET_KEY) // No need to specify algorithm, it's inferred
                .compact();
    }

    // 4. Validate Token (Used by the Filter "Bouncer")
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // Log error: ExpiredJwtException, MalformedJwtException, etc.
            return false;
        }
    }

    // 5. Extract User ID (Used by the Filter "Bouncer")
    public Long getUserIdFromToken(String token) {
        String subject = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Long.parseLong(subject);
    }

    public String getEmailFromToken(String token) {
        String claim = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class); // Still use .get() for custom claims

        return claim;
    }
}
