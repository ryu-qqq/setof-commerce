package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * SearchSellersV1ApiRequest - 판매자 목록 검색 요청 DTO.
 *
 * <p>레거시 SellerFilter 기반 변환.
 *
 * <p>GET /api/v1/sellers - 판매자 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>SearchKeyword enum → String 타입
 *   <li>ApprovalStatus enum → String 타입
 *   <li>Pageable → record 내부 page, size 필드
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.seller.filter.SellerFilter
 */
@Schema(description = "판매자 목록 검색 요청")
public record SearchSellersV1ApiRequest(
        @Parameter(
                        description = "검색 키워드 타입",
                        example = "SELLER_NAME",
                        schema = @Schema(allowableValues = {"SELLER_ID", "SELLER_NAME"}))
                String searchKeyword,
        @Parameter(description = "검색어", example = "홍길동") String searchWord,
        @Parameter(description = "사이트 ID 목록 (필터)", example = "[1, 2]") List<Long> siteIds,
        @Parameter(
                        description = "승인 상태 필터",
                        example = "APPROVED",
                        schema = @Schema(allowableValues = {"PENDING", "APPROVED", "REJECTED"}))
                String status,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {}
