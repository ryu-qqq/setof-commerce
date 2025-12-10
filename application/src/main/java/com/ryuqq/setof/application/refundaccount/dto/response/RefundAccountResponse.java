package com.ryuqq.setof.application.refundaccount.dto.response;

import java.time.Instant;

/**
 * 환불계좌 정보 응답 DTO
 *
 * <p>환불계좌 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 환불계좌 ID
 * @param bankId 은행 ID
 * @param bankName 은행명
 * @param bankCode 은행코드
 * @param maskedAccountNumber 마스킹된 계좌번호
 * @param accountHolderName 예금주명
 * @param isVerified 검증 완료 여부
 * @param verifiedAt 검증 완료 일시
 * @param createdAt 생성일시
 * @param modifiedAt 수정일시
 */
public record RefundAccountResponse(
        Long id,
        Long bankId,
        String bankName,
        String bankCode,
        String maskedAccountNumber,
        String accountHolderName,
        boolean isVerified,
        Instant verifiedAt,
        Instant createdAt,
        Instant modifiedAt) {

    /**
     * Static Factory Method
     *
     * @return RefundAccountResponse 인스턴스
     */
    public static RefundAccountResponse of(
            Long id,
            Long bankId,
            String bankName,
            String bankCode,
            String maskedAccountNumber,
            String accountHolderName,
            boolean isVerified,
            Instant verifiedAt,
            Instant createdAt,
            Instant modifiedAt) {
        return new RefundAccountResponse(
                id,
                bankId,
                bankName,
                bankCode,
                maskedAccountNumber,
                accountHolderName,
                isVerified,
                verifiedAt,
                createdAt,
                modifiedAt);
    }
}
