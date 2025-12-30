package com.connectly.partnerAdmin.module.external.dto.auth;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OcoTokenRequest implements ExternalTokenRequest{

    private String id;
    private String password;

}
