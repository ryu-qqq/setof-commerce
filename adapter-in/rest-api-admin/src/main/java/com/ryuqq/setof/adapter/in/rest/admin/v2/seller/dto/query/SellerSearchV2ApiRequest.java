package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 셀러 검색 API 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "셀러 검색 요청")
public record SellerSearchV2ApiRequest(
        @Schema(description = "셀러명 검색어", example = "테스트") String sellerName,
        @Schema(
                        description = "승인 상태 (PENDING, APPROVED, REJECTED, SUSPENDED)",
                        example = "APPROVED")
                String approvalStatus,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
                Integer page,
        @Schema(description = "페이지 크기", example = "20", defaultValue = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
                @Max(value = 100, message = "페이지 크기는 100을 초과할 수 없습니다")
                Integer size) {

    public SellerSearchV2ApiRequest {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
    }

    public int offset() {
        return page * size;
    }
}
