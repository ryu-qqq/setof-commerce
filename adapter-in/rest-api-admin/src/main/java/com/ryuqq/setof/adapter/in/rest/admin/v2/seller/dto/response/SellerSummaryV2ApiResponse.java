package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response;

import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 셀러 요약 API 응답 DTO (목록 조회용)
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "셀러 요약 응답")
public record SellerSummaryV2ApiResponse(
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "셀러명", example = "테스트 셀러") String sellerName,
        @Schema(description = "로고 URL", example = "https://example.com/logo.png") String logoUrl,
        @Schema(description = "승인 상태", example = "APPROVED") String approvalStatus) {

    public static SellerSummaryV2ApiResponse from(SellerSummaryResponse response) {
        return new SellerSummaryV2ApiResponse(
                response.id(),
                response.sellerName(),
                response.logoUrl(),
                response.approvalStatus());
    }
}
