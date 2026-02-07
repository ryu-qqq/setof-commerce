package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DiscountPolicyV1ApiResponse - 할인 정책 응답 DTO.
 *
 * <p>레거시 DiscountPolicyResponseDto 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Embedded DiscountDetails → 중첩 record DiscountDetailsResponse
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto
 */
@Schema(description = "할인 정책 응답")
public record DiscountPolicyV1ApiResponse(
        @Schema(description = "할인 정책 ID", example = "1") long discountPolicyId,
        @Schema(description = "할인 상세 정보") DiscountDetailsResponse discountDetails,
        @Schema(description = "등록일시", example = "2026-01-01 10:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate,
        @Schema(description = "수정일시", example = "2026-01-15 14:30:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime updateDate,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator) {

    /**
     * 팩토리 메서드.
     *
     * @param discountPolicyId 할인 정책 ID
     * @param discountDetails 할인 상세 정보
     * @param insertDate 등록일시
     * @param updateDate 수정일시
     * @param insertOperator 등록자
     * @param updateOperator 수정자
     * @return DiscountPolicyV1ApiResponse 인스턴스
     */
    public static DiscountPolicyV1ApiResponse of(
            long discountPolicyId,
            DiscountDetailsResponse discountDetails,
            LocalDateTime insertDate,
            LocalDateTime updateDate,
            String insertOperator,
            String updateOperator) {
        return new DiscountPolicyV1ApiResponse(
                discountPolicyId,
                discountDetails,
                insertDate,
                updateDate,
                insertOperator,
                updateOperator);
    }

    /**
     * 정책이 현재 활성 상태인지 확인합니다.
     *
     * @return activeYn이 "Y"이면 true
     */
    public boolean isActive() {
        return discountDetails != null && "Y".equalsIgnoreCase(discountDetails.activeYn());
    }

    /**
     * DiscountDetailsResponse - 할인 상세 정보 중첩 record.
     *
     * <p>레거시 DiscountDetails (Embedded) 기반 변환.
     */
    @Schema(description = "할인 상세 정보")
    public record DiscountDetailsResponse(
            @Schema(description = "정책명", example = "신규 회원 할인") String discountPolicyName,
            @Schema(
                            description = "할인 유형 (RATE: 비율, PRICE: 금액)",
                            example = "RATE",
                            allowableValues = {"RATE", "PRICE"})
                    String discountType,
            @Schema(
                            description = "발행자 유형 (ADMIN: 관리자, SELLER: 판매자)",
                            example = "ADMIN",
                            allowableValues = {"ADMIN", "SELLER"})
                    String publisherType,
            @Schema(
                            description = "적용 대상 유형 (PRODUCT: 상품, SELLER: 판매자, BRAND: 브랜드)",
                            example = "PRODUCT",
                            allowableValues = {"PRODUCT", "SELLER", "BRAND"})
                    String issueType,
            @Schema(
                            description = "할인 한도 적용 여부",
                            example = "Y",
                            allowableValues = {"Y", "N"})
                    String discountLimitYn,
            @Schema(description = "최대 할인 금액", example = "10000") long maxDiscountPrice,
            @Schema(
                            description = "분담 여부",
                            example = "Y",
                            allowableValues = {"Y", "N"})
                    String shareYn,
            @Schema(description = "분담 비율 (%)", example = "50.0") double shareRatio,
            @Schema(description = "할인율 (%)", example = "10.0") double discountRatio,
            @Schema(description = "정책 시작일", example = "2026-01-01 00:00:00")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime policyStartDate,
            @Schema(description = "정책 종료일", example = "2026-12-31 23:59:59")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime policyEndDate,
            @Schema(description = "메모", example = "신규 회원 대상 10% 할인") String memo,
            @Schema(description = "우선순위 (낮을수록 우선)", example = "1") int priority,
            @Schema(
                            description = "활성화 여부",
                            example = "Y",
                            allowableValues = {"Y", "N"})
                    String activeYn) {

        /**
         * 팩토리 메서드.
         *
         * @return DiscountDetailsResponse 인스턴스
         */
        public static DiscountDetailsResponse of(
                String discountPolicyName,
                String discountType,
                String publisherType,
                String issueType,
                String discountLimitYn,
                long maxDiscountPrice,
                String shareYn,
                double shareRatio,
                double discountRatio,
                LocalDateTime policyStartDate,
                LocalDateTime policyEndDate,
                String memo,
                int priority,
                String activeYn) {
            return new DiscountDetailsResponse(
                    discountPolicyName,
                    discountType,
                    publisherType,
                    issueType,
                    discountLimitYn,
                    maxDiscountPrice,
                    shareYn,
                    shareRatio,
                    discountRatio,
                    policyStartDate,
                    policyEndDate,
                    memo,
                    priority,
                    activeYn);
        }

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
         * 정책이 현재 유효한 기간 내인지 확인합니다.
         *
         * @return 현재 시간이 정책 기간 내이면 true
         */
        public boolean isWithinPolicyPeriod() {
            LocalDateTime now = LocalDateTime.now();
            return policyStartDate != null
                    && policyEndDate != null
                    && !now.isBefore(policyStartDate)
                    && !now.isAfter(policyEndDate);
        }
    }
}
