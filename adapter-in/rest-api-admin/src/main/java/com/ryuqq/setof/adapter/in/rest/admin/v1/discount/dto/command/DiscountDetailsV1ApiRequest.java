package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DiscountDetailsV1ApiRequest - 할인 상세 정보 요청 DTO.
 *
 * <p>레거시 CreateDiscountDetails (Embedded) 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>DiscountType, PublisherType, IssueType enum → String + @Schema(allowableValues)
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>@Valid 중첩 객체로 여러 커맨드 DTO에서 공유
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.CreateDiscountDetails
 */
@Schema(description = "할인 상세 정보 요청")
public record DiscountDetailsV1ApiRequest(
        @Schema(
                        description = "할인 정책명",
                        example = "신규 회원 할인",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "할인 정책명은 필수입니다.")
                @Size(max = 50, message = "할인 정책명은 50자 이하여야 합니다.")
                String discountPolicyName,
        @Schema(
                        description = "할인 유형 (RATE: 비율 할인, PRICE: 금액 할인)",
                        example = "RATE",
                        allowableValues = {"RATE", "PRICE"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String discountType,
        @Schema(
                        description = "발행자 유형 (ADMIN: 관리자, SELLER: 판매자)",
                        example = "ADMIN",
                        allowableValues = {"ADMIN", "SELLER"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String publisherType,
        @Schema(
                        description = "적용 대상 유형 (PRODUCT: 상품, SELLER: 판매자, BRAND: 브랜드)",
                        example = "PRODUCT",
                        allowableValues = {"PRODUCT", "SELLER", "BRAND"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String issueType,
        @Schema(
                        description = "할인 한도 적용 여부 (Y: 적용, N: 미적용)",
                        example = "Y",
                        allowableValues = {"Y", "N"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String discountLimitYn,
        @Schema(description = "최대 할인 금액 (discountLimitYn=Y일 때 유효)", example = "10000")
                @Min(value = 0, message = "최대 할인 금액은 0 이상이어야 합니다.")
                long maxDiscountPrice,
        @Schema(
                        description = "할인 분담 여부 (Y: 분담, N: 미분담)",
                        example = "N",
                        allowableValues = {"Y", "N"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String shareYn,
        @Schema(description = "분담 비율 (0~100, shareYn=Y일 때 유효)", example = "50.0")
                @DecimalMin(value = "0.0", message = "분담 비율은 0 이상이어야 합니다.")
                @DecimalMax(value = "100.0", message = "분담 비율은 100 이하여야 합니다.")
                double shareRatio,
        @Schema(description = "할인율 (0~100, discountType=RATE일 때 유효)", example = "10.0")
                @DecimalMin(value = "0.0", message = "할인율은 0 이상이어야 합니다.")
                @DecimalMax(value = "100.0", message = "할인율은 100 이하여야 합니다.")
                double discountRatio,
        @Schema(
                        description = "정책 시작일",
                        example = "2026-01-01 00:00:00",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime policyStartDate,
        @Schema(
                        description = "정책 종료일",
                        example = "2026-12-31 23:59:59",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime policyEndDate,
        @Schema(description = "메모", example = "신규 회원 대상 10% 할인")
                @Size(max = 100, message = "메모는 100자 이하여야 합니다.")
                String memo,
        @Schema(description = "우선순위 (0~100, 낮을수록 우선 적용)", example = "1")
                @Min(value = 0, message = "우선순위는 0 이상이어야 합니다.")
                @Max(value = 100, message = "우선순위는 100 이하여야 합니다.")
                int priority,
        @Schema(
                        description = "활성화 여부 (Y: 활성, N: 비활성)",
                        example = "Y",
                        allowableValues = {"Y", "N"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String activeYn) {

    /**
     * 비율 할인 타입인지 확인합니다.
     *
     * @return discountType이 "RATE"이면 true
     */
    public boolean isRateDiscount() {
        return "RATE".equalsIgnoreCase(discountType);
    }

    /**
     * 금액 할인 타입인지 확인합니다.
     *
     * @return discountType이 "PRICE"이면 true
     */
    public boolean isPriceDiscount() {
        return "PRICE".equalsIgnoreCase(discountType);
    }

    /**
     * 활성화 여부를 확인합니다.
     *
     * @return activeYn이 "Y"이면 true
     */
    public boolean isActive() {
        return "Y".equalsIgnoreCase(activeYn);
    }

    /**
     * 할인 한도 적용 여부를 확인합니다.
     *
     * @return discountLimitYn이 "Y"이면 true
     */
    public boolean hasDiscountLimit() {
        return "Y".equalsIgnoreCase(discountLimitYn);
    }

    /**
     * 분담 여부를 확인합니다.
     *
     * @return shareYn이 "Y"이면 true
     */
    public boolean isShared() {
        return "Y".equalsIgnoreCase(shareYn);
    }
}
