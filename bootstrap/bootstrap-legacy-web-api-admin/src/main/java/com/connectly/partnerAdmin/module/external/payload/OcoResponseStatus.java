package com.connectly.partnerAdmin.module.external.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcoResponseStatus {
    private int shop_acid;
    private String returnMessage;
    private String statusCode;


    public OcoResponseStatus(int shop_acid, String returnMessage, String statusCode) {
        this.shop_acid = shop_acid;
        this.returnMessage = returnMessage;
        this.statusCode = statusCode;
    }
}
