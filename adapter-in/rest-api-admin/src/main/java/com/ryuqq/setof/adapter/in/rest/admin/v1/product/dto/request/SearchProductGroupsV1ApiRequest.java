package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * SearchProductGroupsV1ApiRequest - 상품그룹 목록 검색 요청 DTO.
 *
 * <p>레거시 ProductGroupFilter 기반 변환.
 *
 * <p>GET /api/v1/products/group - 상품그룹 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>SearchKeyword enum → String 타입
 *   <li>ManagementType enum → String 타입
 *   <li>Yn enum → String 타입
 *   <li>Pageable → record 내부 page, size 필드
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter
 */
@Schema(description = "상품그룹 목록 검색 요청")
public record SearchProductGroupsV1ApiRequest(
        @Parameter(description = "조회 시작일 (yyyy-MM-dd HH:mm:ss)", example = "2024-01-01 00:00:00")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(description = "조회 종료일 (yyyy-MM-dd HH:mm:ss)", example = "2024-12-31 23:59:59")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Parameter(
                        description = "검색 키워드 타입",
                        example = "PRODUCT_NAME",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "PRODUCT_ID",
                                            "PRODUCT_NAME",
                                            "SELLER_NAME"
                                        }))
                String searchKeyword,
        @Parameter(description = "검색어", example = "나이키 티셔츠") String searchWord,
        @Parameter(description = "No-Offset 페이징용 마지막 상품그룹 ID", example = "12345") Long lastDomainId,
        @Parameter(
                        description = "관리 유형",
                        example = "NORMAL",
                        schema = @Schema(allowableValues = {"NORMAL", "CRAWL", "EXTERNAL"}))
                String managementType,
        @Parameter(description = "카테고리 ID (하위 카테고리 자동 포함)", example = "100") Long categoryId,
        @Parameter(description = "브랜드 ID", example = "50") Long brandId,
        @Parameter(
                        description = "품절 여부",
                        example = "N",
                        schema = @Schema(allowableValues = {"Y", "N"}))
                String soldOutYn,
        @Parameter(
                        description = "노출 여부",
                        example = "Y",
                        schema = @Schema(allowableValues = {"Y", "N"}))
                String displayYn,
        @Parameter(description = "최소 판매가", example = "10000") Long minSalePrice,
        @Parameter(description = "최대 판매가", example = "100000") Long maxSalePrice,
        @Parameter(description = "최소 할인율 (%)", example = "0") Long minDiscountRate,
        @Parameter(description = "최대 할인율 (%)", example = "50") Long maxDiscountRate,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    /**
     * No-Offset 페이징 사용 여부를 반환합니다.
     *
     * @return lastDomainId가 null이 아니면 true
     */
    public boolean isNoOffsetFetch() {
        return lastDomainId != null;
    }
}
