package com.connectly.partnerAdmin.module.external.enums.oco;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OcoOrderStatus {

    W("결제완료"),
    S("상품 준비중"),
    I("배송중"),
    E("배송 완료")
    ;


    private final String displayName;
}
