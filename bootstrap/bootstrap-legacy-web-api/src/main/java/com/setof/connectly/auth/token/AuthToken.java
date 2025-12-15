package com.setof.connectly.auth.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;

public class AuthToken {

    private final String token;
    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthToken(String id, String role, Date issue, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, issue, expiry);
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

    public String getToken() {
        return token;
    }
}
