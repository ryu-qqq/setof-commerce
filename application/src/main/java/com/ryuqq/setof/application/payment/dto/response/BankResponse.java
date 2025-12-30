package com.ryuqq.setof.application.payment.dto.response;

import com.ryuqq.setof.domain.payment.vo.BankCode;

/**
 * BankResponse - 은행 정보 응답 DTO
 *
 * <p>가상계좌 및 환불 계좌에서 사용되는 은행 정보를 담습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record BankResponse(String code, String displayName) {

    /**
     * BankCode enum에서 응답 DTO 생성
     *
     * @param bankCode BankCode enum
     * @return BankResponse
     */
    public static BankResponse from(BankCode bankCode) {
        return new BankResponse(bankCode.code(), bankCode.displayName());
    }
}
