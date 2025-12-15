package com.setof.connectly.module.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WithdrawalReason {
    DELIVERY,
    QUALITY,
    PRODUCT,
    MEMBERSHIP,
    SERVICE;
}
