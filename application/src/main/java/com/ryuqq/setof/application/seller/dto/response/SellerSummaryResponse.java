package com.ryuqq.setof.application.seller.dto.response;

/**
 * 셀러 요약 정보 응답 DTO
 *
 * <p>셀러 목록 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 셀러 ID
 * @param sellerName 셀러명
 * @param logoUrl 로고 URL
 * @param approvalStatus 승인 상태
 * @author development-team
 * @since 1.0.0
 */
public record SellerSummaryResponse(
        Long id, String sellerName, String logoUrl, String approvalStatus) {

    /**
     * Static Factory Method
     *
     * @param id 셀러 ID
     * @param sellerName 셀러명
     * @param logoUrl 로고 URL
     * @param approvalStatus 승인 상태
     * @return SellerSummaryResponse 인스턴스
     */
    public static SellerSummaryResponse of(
            Long id, String sellerName, String logoUrl, String approvalStatus) {
        return new SellerSummaryResponse(id, sellerName, logoUrl, approvalStatus);
    }
}
