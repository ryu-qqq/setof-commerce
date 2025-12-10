package com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.response;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 환불계좌 정보 API 응답 DTO
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
@Schema(description = "환불계좌 정보 응답")
public record RefundAccountV2ApiResponse(
        @Schema(description = "환불계좌 ID", example = "1") Long id,
        @Schema(description = "은행 ID", example = "1") Long bankId,
        @Schema(description = "은행명", example = "KB국민은행") String bankName,
        @Schema(description = "은행코드", example = "004") String bankCode,
        @Schema(description = "마스킹된 계좌번호", example = "123-***-***-0123") String maskedAccountNumber,
        @Schema(description = "예금주명", example = "홍길동") String accountHolderName,
        @Schema(description = "검증 완료 여부", example = "true") boolean isVerified,
        @Schema(description = "검증 완료 일시") Instant verifiedAt,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant modifiedAt) {

    /**
     * Application Response -> API Response 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static RefundAccountV2ApiResponse from(RefundAccountResponse response) {
        return new RefundAccountV2ApiResponse(
                response.id(),
                response.bankId(),
                response.bankName(),
                response.bankCode(),
                response.maskedAccountNumber(),
                response.accountHolderName(),
                response.isVerified(),
                response.verifiedAt(),
                response.createdAt(),
                response.modifiedAt());
    }
}
