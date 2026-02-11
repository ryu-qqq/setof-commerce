package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * DiscountPolicySearchV1ApiRequest - 할인 정책 검색 요청 DTO.
 *
 * <p>레거시 DiscountFilter 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>PeriodType, Yn, PublisherType, IssueType enum → String + @Schema(allowableValues)
 *   <li>Spring Pageable → page, size 필드 내장
 *   <li>SearchAndDateFilter 필드 포함 (startDate, endDate, searchKeyword, searchWord)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.filter.DiscountFilter
 */
@Schema(description = "할인 정책 검색 요청")
public record DiscountPolicySearchV1ApiRequest(
        @Parameter(description = "할인 정책 ID 필터 (특정 정책만 조회)", example = "1") Long discountPolicyId,
        @Parameter(
                        description = "기간 유형 (POLICY: 정책 적용 기간, INSERT: 등록 기간)",
                        example = "POLICY",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "DISPLAY",
                                            "INSERT",
                                            "PAYMENT",
                                            "POLICY",
                                            "SETTLEMENT",
                                            "ORDER_HISTORY"
                                        }))
                String periodType,
        @Parameter(description = "조회 시작일", example = "2026-01-01 00:00:00")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(description = "조회 종료일", example = "2026-12-31 23:59:59")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Parameter(
                        description = "활성화 여부 (Y: 활성, N: 비활성)",
                        example = "Y",
                        schema = @Schema(allowableValues = {"Y", "N"}))
                String activeYn,
        @Parameter(
                        description = "발행자 유형 (ADMIN: 관리자, SELLER: 판매자)",
                        example = "ADMIN",
                        schema = @Schema(allowableValues = {"ADMIN", "SELLER"}))
                String publisherType,
        @Parameter(
                        description = "적용 대상 유형 (PRODUCT: 상품, SELLER: 판매자, BRAND: 브랜드)",
                        example = "PRODUCT",
                        schema = @Schema(allowableValues = {"PRODUCT", "SELLER", "BRAND"}))
                String issueType,
        @Parameter(description = "사용자 ID 필터", example = "1001") Long userId,
        @Parameter(description = "검색 키워드 유형", example = "NAME") String searchKeyword,
        @Parameter(description = "검색어", example = "할인") String searchWord,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size,
        @Parameter(
                        description = "정렬 필드 (예: id, insertDate)",
                        example = "id",
                        schema = @Schema(defaultValue = "id"))
                String sortBy,
        @Parameter(
                        description = "정렬 방향",
                        example = "DESC",
                        schema =
                                @Schema(
                                        allowableValues = {"ASC", "DESC"},
                                        defaultValue = "DESC"))
                String sortDirection) {

    /**
     * 기본값이 적용된 인스턴스를 반환합니다.
     *
     * @return 기본값이 적용된 DiscountPolicySearchV1ApiRequest
     */
    public DiscountPolicySearchV1ApiRequest withDefaults() {
        return new DiscountPolicySearchV1ApiRequest(
                this.discountPolicyId,
                this.periodType,
                this.startDate,
                this.endDate,
                this.activeYn,
                this.publisherType,
                this.issueType,
                this.userId,
                this.searchKeyword,
                this.searchWord,
                this.page != null ? this.page : 0,
                this.size != null ? this.size : 20,
                this.sortBy != null ? this.sortBy : "id",
                this.sortDirection != null ? this.sortDirection : "DESC");
    }

    /**
     * 정책 기간 기준 검색 여부를 반환합니다.
     *
     * @return periodType이 "POLICY"이면 true
     */
    public boolean isPolicyPeriodSearch() {
        return "POLICY".equalsIgnoreCase(periodType);
    }

    /**
     * 활성화된 정책만 검색 여부를 반환합니다.
     *
     * @return activeYn이 "Y"이면 true
     */
    public boolean isActiveOnly() {
        return "Y".equalsIgnoreCase(activeYn);
    }

    /**
     * 내림차순 정렬 여부를 반환합니다.
     *
     * @return sortDirection이 "DESC"이면 true
     */
    public boolean isDescending() {
        return !"ASC".equalsIgnoreCase(sortDirection);
    }
}
