package com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * MileageHistoryV1ApiResponse - 마일리지 이력 응답 DTO.
 *
 * <p>레거시 UserMileageHistoryDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param mileageId 마일리지 ID
 * @param title 이력 제목 (적립: DB 저장값, 사용/환불/만료: 동적 설정)
 * @param paymentId 결제 ID (주문 관련 마일리지인 경우)
 * @param orderId 주문 ID (주문 관련 마일리지인 경우)
 * @param changeAmount 변동 금액 (양수: 적립/환불, 음수: 사용)
 * @param reason 사유 (SAVE, USE, REFUND, EXPIRED)
 * @param usedDate 사용/적립 일시
 * @param expirationDate 만료일
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto
 */
@Schema(description = "마일리지 이력 응답")
public record MileageHistoryV1ApiResponse(
        @Schema(description = "마일리지 ID", example = "1001") long mileageId,
        @Schema(description = "이력 제목", example = "회원가입 축하 적립금") String title,
        @Schema(description = "결제 ID (주문 관련 마일리지인 경우)", example = "5001", nullable = true)
                Long paymentId,
        @Schema(description = "주문 ID (주문 관련 마일리지인 경우)", example = "3001", nullable = true)
                Long orderId,
        @Schema(description = "변동 금액 (양수: 적립/환불, 음수: 사용)", example = "5000.0") double changeAmount,
        @Schema(
                        description = "사유",
                        example = "SAVE",
                        allowableValues = {"SAVE", "USE", "REFUND", "EXPIRED"})
                String reason,
        @Schema(description = "사용/적립 일시", example = "2026-01-15T10:30:00") LocalDateTime usedDate,
        @Schema(description = "만료일", example = "2027-01-15T23:59:59", nullable = true)
                LocalDateTime expirationDate) {}
