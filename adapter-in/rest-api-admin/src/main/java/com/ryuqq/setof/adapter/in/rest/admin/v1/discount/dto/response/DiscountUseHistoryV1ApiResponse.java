package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DiscountUseHistoryV1ApiResponse - 할인 사용 이력 응답 DTO.
 *
 * <p>레거시 DiscountUseHistoryDto 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>@JsonIgnore 필드(discountUseHistoryId) 제거
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.DiscountUseHistoryDto
 */
@Schema(description = "할인 사용 이력 응답")
public record DiscountUseHistoryV1ApiResponse(
        @Schema(description = "사용자 ID", example = "1001") long userId,
        @Schema(description = "사용자 이름", example = "홍길동") String name,
        @Schema(description = "주문 ID", example = "50001") long orderId,
        @Schema(description = "주문 금액", example = "150000") BigDecimal orderAmount,
        @Schema(description = "결제 ID", example = "60001") long paymentId,
        @Schema(description = "상품 그룹 ID", example = "5001") long productGroupId,
        @Schema(description = "직접 할인 금액", example = "15000") BigDecimal directDiscountPrice,
        @Schema(description = "사용 일시", example = "2026-01-15 14:30:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime useDate,
        @Schema(description = "총 직접 할인 금액 (집계)", example = "15000")
                BigDecimal totalDirectDiscountPrice) {

    /**
     * 팩토리 메서드.
     *
     * @param userId 사용자 ID
     * @param name 사용자 이름
     * @param orderId 주문 ID
     * @param orderAmount 주문 금액
     * @param paymentId 결제 ID
     * @param productGroupId 상품 그룹 ID
     * @param directDiscountPrice 직접 할인 금액
     * @param useDate 사용 일시
     * @param totalDirectDiscountPrice 총 직접 할인 금액
     * @return DiscountUseHistoryV1ApiResponse 인스턴스
     */
    public static DiscountUseHistoryV1ApiResponse of(
            long userId,
            String name,
            long orderId,
            BigDecimal orderAmount,
            long paymentId,
            long productGroupId,
            BigDecimal directDiscountPrice,
            LocalDateTime useDate,
            BigDecimal totalDirectDiscountPrice) {
        return new DiscountUseHistoryV1ApiResponse(
                userId,
                name,
                orderId,
                orderAmount,
                paymentId,
                productGroupId,
                directDiscountPrice,
                useDate,
                totalDirectDiscountPrice);
    }

    /**
     * 할인율을 계산합니다.
     *
     * @return 주문 금액 대비 할인 비율 (0-100%)
     */
    public BigDecimal calculateDiscountRate() {
        if (orderAmount == null || orderAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (directDiscountPrice == null) {
            return BigDecimal.ZERO;
        }
        return directDiscountPrice
                .multiply(BigDecimal.valueOf(100))
                .divide(orderAmount, 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * 할인 후 결제 금액을 계산합니다.
     *
     * @return 주문 금액 - 직접 할인 금액
     */
    public BigDecimal calculateNetAmount() {
        if (orderAmount == null) {
            return BigDecimal.ZERO;
        }
        if (directDiscountPrice == null) {
            return orderAmount;
        }
        return orderAmount.subtract(directDiscountPrice);
    }
}
