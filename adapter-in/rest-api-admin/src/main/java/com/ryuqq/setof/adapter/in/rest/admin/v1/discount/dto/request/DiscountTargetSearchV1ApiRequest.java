package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DiscountTargetSearchV1ApiRequest - 할인 대상 검색 요청 DTO.
 *
 * <p>레거시 Controller 파라미터 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>IssueType enum → String + @Schema(allowableValues)
 *   <li>Spring Pageable → page, size 필드 내장
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "할인 대상 검색 요청")
public record DiscountTargetSearchV1ApiRequest(
        @Parameter(
                        description = "적용 대상 유형 (PRODUCT: 상품, SELLER: 판매자)",
                        example = "PRODUCT",
                        required = true,
                        schema = @Schema(allowableValues = {"PRODUCT", "SELLER"}))
                @NotNull(message = "issueType은 필수입니다.")
                String issueType,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    /**
     * 기본값이 적용된 인스턴스를 반환합니다.
     *
     * @return 기본값이 적용된 DiscountTargetSearchV1ApiRequest
     */
    public DiscountTargetSearchV1ApiRequest withDefaults() {
        return new DiscountTargetSearchV1ApiRequest(
                this.issueType,
                this.page != null ? this.page : 0,
                this.size != null ? this.size : 20);
    }

    /**
     * 상품 대상 검색 여부를 반환합니다.
     *
     * @return issueType이 "PRODUCT"이면 true
     */
    public boolean isProductTarget() {
        return "PRODUCT".equalsIgnoreCase(issueType);
    }

    /**
     * 판매자 대상 검색 여부를 반환합니다.
     *
     * @return issueType이 "SELLER"이면 true
     */
    public boolean isSellerTarget() {
        return "SELLER".equalsIgnoreCase(issueType);
    }
}
