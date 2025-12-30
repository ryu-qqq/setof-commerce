package com.connectly.partnerAdmin.auth.core;

import com.connectly.partnerAdmin.auth.enums.JwtErrorEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.Key;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class JwtAuthToken implements AuthToken {

    private final String token;
    private final Key key;
    private String subject;
    private static final String AUTHORITIES_KEY = "role";


    public JwtAuthToken(String id, String role, Date issue, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, issue, expiry);
    }

    public JwtAuthToken(Key key, String token) {
        this.key = key;
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getSubject() {
        if (subject == null) {
            subject = parseClaimsJws().getSubject();
        }
        return subject;
    }

    @JsonIgnore
    public Key getKey() {
        return key;
    }

    private String createAuthToken(String id, String role, Date issue, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(issue)
                .setExpiration(expiry)
                .compact();
    }

    private Claims parseClaimsJws() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
        } catch (Exception e) {
            throw new JwtException(JwtErrorEnum.from(e).getMessage());
        }
    }

}
