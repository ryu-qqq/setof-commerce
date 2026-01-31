package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * SellerFilterV1ApiRequest - V1 셀러 목록 조회 필터 요청 DTO.
 *
 * <p>레거시 SellerFilter 구조와 호환되는 V1 요청 형태입니다.
 *
 * <p>레거시 필드 매핑:
 *
 * <ul>
 *   <li>searchKeyword: SearchKeyword enum → String (SELLER_ID, SELLER_NAME 등)
 *   <li>status: ApprovalStatus enum → String (PENDING, APPROVED, REJECTED)
 *   <li>siteIds: List&lt;Long&gt; (현재 미사용, 향후 확장용)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "V1 셀러 목록 조회 필터")
public record SellerFilterV1ApiRequest(
        @Schema(
                        description = "검색 키워드 타입 (레거시 SearchKeyword enum)",
                        example = "SELLER_NAME",
                        allowableValues = {"SELLER_ID", "SELLER_NAME"})
                String searchKeyword,
        @Schema(description = "검색어", example = "테스트") String searchWord,
        @Schema(
                        description = "승인 상태 (레거시 ApprovalStatus enum)",
                        example = "APPROVED",
                        allowableValues = {"PENDING", "APPROVED", "REJECTED"})
                String status,
        @Schema(description = "사이트 ID 목록 (현재 미사용)", example = "[1, 2]") List<Long> siteIds,
        @Schema(description = "정렬 기준", example = "createdAt", defaultValue = "createdAt")
                String sortKey,
        @Schema(description = "정렬 방향 (ASC/DESC)", example = "DESC", defaultValue = "DESC")
                String sortDirection,
        @Min(0) @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
                Integer page,
        @Min(1) @Max(100) @Schema(description = "페이지 크기", example = "20", defaultValue = "20")
                Integer size) {

    /** 기본값이 적용된 인스턴스 생성. */
    public SellerFilterV1ApiRequest {
        if (sortKey == null) {
            sortKey = "createdAt";
        }
        if (sortDirection == null) {
            sortDirection = "DESC";
        }
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
    }
}
