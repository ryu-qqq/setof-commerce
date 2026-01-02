package com.connectly.partnerAdmin.auth.service;

import com.connectly.partnerAdmin.auth.dto.AuthTokenResponse;
import com.connectly.partnerAdmin.auth.dto.CreateAuthToken;

public interface AuthTokenGenerateService {

    AuthTokenResponse generateToken(CreateAuthToken createAuthToken);
}
