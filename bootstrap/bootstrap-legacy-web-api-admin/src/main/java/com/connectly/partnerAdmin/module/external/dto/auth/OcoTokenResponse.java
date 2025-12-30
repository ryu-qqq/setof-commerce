package com.connectly.partnerAdmin.module.external.dto.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcoTokenResponse {
    private String token;

    public OcoTokenResponse(String token) {
        this.token = token;
    }
}
