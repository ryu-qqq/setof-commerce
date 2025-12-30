package com.connectly.partnerAdmin.auth.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.connectly.partnerAdmin.auth.config.AuthConstants;
import com.connectly.partnerAdmin.auth.exception.TokenTypeException;
import com.connectly.partnerAdmin.auth.provider.AuthTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider authTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> jwtToken = resolveToken(request);

        if (jwtToken.isPresent()) {
            Authentication authentication = authTokenProvider.getAuthentication(jwtToken.get());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken)) {
            if(bearerToken.startsWith(AuthConstants.BEARER_PREFIX)){
                return Optional.of(bearerToken.substring(AuthConstants.BEARER_PREFIX.length()));
            }
            throw new TokenTypeException();
        }

        String airflowHeaderKey = request.getHeader(AuthConstants.AIR_FLOW_HEADER_KEY);

        if (StringUtils.hasText(airflowHeaderKey)) {
            return Optional.of(airflowHeaderKey);
        }

        return Optional.empty();
    }

}
