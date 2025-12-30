package com.connectly.partnerAdmin.auth.service;

import com.connectly.partnerAdmin.auth.core.AuthToken;

import java.util.Optional;

public interface RefreshTokenFetchService {

    Optional<AuthToken> fetchRefreshToken(String email);
}
