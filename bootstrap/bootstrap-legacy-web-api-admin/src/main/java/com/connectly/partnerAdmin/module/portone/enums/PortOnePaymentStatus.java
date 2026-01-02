package com.connectly.partnerAdmin.module.portone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PortOnePaymentStatus {
    paid, //결제 승인 및 가상계좌 결제 금액 입금
    ready, // 가상 계좌 발급
    failed,
    cancelled, // 관리자 콘솔에서 결제 취소

}
