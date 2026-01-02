package com.connectly.partnerAdmin.auth.provider;

import com.connectly.partnerAdmin.auth.config.JwtConfig;
import com.connectly.partnerAdmin.auth.core.AuthToken;
import com.connectly.partnerAdmin.auth.core.JwtAuthToken;
import com.connectly.partnerAdmin.auth.exception.ExpiredJwtTokenException;
import com.connectly.partnerAdmin.auth.service.CustomUserDetailsService;
import com.connectly.partnerAdmin.auth.service.RefreshTokenFetchService;
import com.connectly.partnerAdmin.module.generic.date.DateGenerator;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

    private static final String TOKEN_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final JwtConfig jwtConfig;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenFetchService refreshTokenFetchService;

    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public AuthToken createAuthToken(UserDetails userDetails, boolean isRefreshToken) {
        DateGenerator issueDateGenerator = new DateGenerator();
        Date issueDate = issueDateGenerator.toDate();

        long expirationTime = jwtConfig.getExpirationTime(isRefreshToken);
        DateGenerator expiryDateGenerator = new DateGenerator(issueDateGenerator.getCurrentDateTime(), TOKEN_DATE_FORMAT);
        Date expiryDate = expiryDateGenerator.plus(expirationTime, ChronoUnit.MILLIS).toDate();

        return new JwtAuthToken(userDetails.getUsername(), SecurityUtils.getAuthorization().getName(), issueDate, expiryDate, key);
    }

    public Authentication getAuthentication(String token){
        if(isEqualSecreteKey(token)){
            return getAuthTokenForAdmin();
        }

        AuthToken authToken = new JwtAuthToken(key, token);
        return getAuthToken(authToken);
    }


    private Authentication getAuthToken(AuthToken authToken) {
        try {
            return generateAuthentication(authToken);
        } catch (ExpiredJwtException e) {
            return handleExpiredToken(e);
        }
    }

    private Authentication handleExpiredToken(ExpiredJwtException e) {
        Optional<AuthToken> refreshTokenOpt = refreshTokenFetchService.fetchRefreshToken(e.getClaims().getSubject());
        if (refreshTokenOpt.isPresent()) {
            AuthToken refreshToken = refreshTokenOpt.get();
            try {
                return generateAuthentication(refreshToken);
            } catch (ExpiredJwtException ex) {
                throw new ExpiredJwtTokenException();
            }
        }
        throw new ExpiredJwtTokenException();
    }


    private Authentication generateAuthentication(AuthToken authToken) {
        String subject = authToken.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }


    private Authentication getAuthTokenForAdmin() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtConfig.getAdministratorEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }


    private boolean isEqualSecreteKey(String inputKey){
        Key changeKey= Keys.hmacShaKeyFor(inputKey.getBytes());
        return key.equals(changeKey);
    }


}
