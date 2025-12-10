package com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 마일리지 내역 Response
 *
 * <p>마일리지 내역 정보를 반환하는 응답 DTO입니다.
 *
 * @param mileageHistoryId 내역 ID
 * @param mileageId 마일리지 ID
 * @param title 마일리지 명
 * @param reason 유형 (EARN: 적립, USE: 사용, EXPIRE: 소멸)
 * @param changeAmount 잔액
 * @param paymentId 결제 ID (관련 결제이 있는 경우)
 * @param orderId 주문 ID (관련 주문이 있는 경우)
 * @param usedDate 발생일시
 * @param expirationDate 만료일시 (적립인 경우)
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "마일리지 내역 응답")
public record MyMileageHistoryV1ApiResponse(
        @Schema(description = "내역 ID", example = "1") Long mileageHistoryId,
        @Schema(description = "내역 ID", example = "1") Long mileageId,
        @Schema(description = "마일리지 명", example = "회원 가입 축하 적립금") String title,
        @Schema(description = "결제 ID (관련 결제가 있는 경우)", example = "1") Long paymentId,
        @Schema(description = "주문 ID (관련 주문이 있는 경우)", example = "1") Long orderId,
        @Schema(description = "금액", example = "1000") Long changeAmount,
        @Schema(description = "유형 (EARN: 적립, USE: 사용, EXPIRE: 소멸)", example = "EARN") String reason,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "발생일시", example = "2025-01-01 10:00:00") LocalDateTime usedDate,
        @Schema(description = "만료일시 (적립인 경우)", example = "2026-01-01 23:59:59")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expirationDate
) {}
