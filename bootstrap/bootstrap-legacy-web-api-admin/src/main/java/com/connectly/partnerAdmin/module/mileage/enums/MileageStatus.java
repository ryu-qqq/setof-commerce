package com.connectly.partnerAdmin.module.mileage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MileageStatus {

    PENDING("대기중"),
    APPROVED("승인"),
    CANCELLED("취소");

    private final String displayName;

    public boolean isPending(){
        return this.equals(PENDING);
    }

}