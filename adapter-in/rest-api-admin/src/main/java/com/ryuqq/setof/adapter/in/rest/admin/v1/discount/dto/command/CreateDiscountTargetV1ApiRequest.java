package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * CreateDiscountTargetV1ApiRequest - 할인 대상 등록 요청 DTO.
 *
 * <p>레거시 CreateDiscountTarget 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter/@Setter → record 기본 접근자
 *   <li>IssueType enum → String + @Schema(allowableValues)
 *   <li>targetIds @Size(min=1) 검증 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.dto.CreateDiscountTarget
 */
@Schema(description = "할인 대상 등록 요청")
public record CreateDiscountTargetV1ApiRequest(
        @Schema(
                        description = "적용 대상 유형 (PRODUCT: 상품, SELLER: 판매자, BRAND: 브랜드)",
                        example = "PRODUCT",
                        allowableValues = {"PRODUCT", "SELLER", "BRAND"},
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "적용 대상 유형은 필수입니다.")
                String issueType,
        @Schema(
                        description = "대상 ID 목록 (최소 1개 이상)",
                        example = "[101, 102, 103]",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "대상 ID 목록은 필수입니다.")
                @Size(min = 1, message = "대상 ID 목록은 하나 이상이어야 합니다.")
                List<Long> targetIds) {

    /**
     * 상품 대상인지 확인합니다.
     *
     * @return issueType이 "PRODUCT"이면 true
     */
    public boolean isProductTarget() {
        return "PRODUCT".equalsIgnoreCase(issueType);
    }

    /**
     * 판매자 대상인지 확인합니다.
     *
     * @return issueType이 "SELLER"이면 true
     */
    public boolean isSellerTarget() {
        return "SELLER".equalsIgnoreCase(issueType);
    }

    /**
     * 브랜드 대상인지 확인합니다.
     *
     * @return issueType이 "BRAND"이면 true
     */
    public boolean isBrandTarget() {
        return "BRAND".equalsIgnoreCase(issueType);
    }
}
