package com.connectly.partnerAdmin.auth.service;


import com.connectly.partnerAdmin.auth.core.AuthToken;

public interface RefreshTokenQueryService {

    void saveToken(AuthToken token);

}
