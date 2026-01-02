package com.connectly.partnerAdmin.auth.service;

import com.connectly.partnerAdmin.auth.core.AuthToken;
import com.connectly.partnerAdmin.auth.dto.AuthTokenResponse;
import com.connectly.partnerAdmin.auth.dto.CreateAuthToken;
import com.connectly.partnerAdmin.auth.provider.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class JwtTokenGenerateService implements AuthTokenGenerateService{

    private final UserDetailsService userDetailsService;
    private final AuthTokenProvider authTokenProvider;
    private final RefreshTokenQueryService refreshTokenQueryService;

    @Override
    public AuthTokenResponse generateToken(CreateAuthToken createAuthToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(createAuthToken.userId());
        AuthToken authToken = authTokenProvider.createAuthToken(userDetails, false);
        AuthToken refreshAuthToken = authTokenProvider.createAuthToken(userDetails, true);
        refreshTokenQueryService.saveToken(refreshAuthToken);
        return new AuthTokenResponse(authToken.getToken());
    }


}
