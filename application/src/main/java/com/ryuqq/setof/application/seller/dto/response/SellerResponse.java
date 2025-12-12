package com.ryuqq.setof.application.seller.dto.response;

/**
 * 셀러 상세 정보 응답 DTO
 *
 * <p>셀러 상세 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 셀러 ID
 * @param sellerName 셀러명
 * @param logoUrl 로고 URL
 * @param description 설명
 * @param approvalStatus 승인 상태
 * @param businessInfo 사업자 정보
 * @param csInfo CS 정보
 * @author development-team
 * @since 1.0.0
 */
public record SellerResponse(
        Long id,
        String sellerName,
        String logoUrl,
        String description,
        String approvalStatus,
        BusinessInfoResponse businessInfo,
        CsInfoResponse csInfo) {

    /**
     * Static Factory Method
     *
     * @param id 셀러 ID
     * @param sellerName 셀러명
     * @param logoUrl 로고 URL
     * @param description 설명
     * @param approvalStatus 승인 상태
     * @param businessInfo 사업자 정보
     * @param csInfo CS 정보
     * @return SellerResponse 인스턴스
     */
    public static SellerResponse of(
            Long id,
            String sellerName,
            String logoUrl,
            String description,
            String approvalStatus,
            BusinessInfoResponse businessInfo,
            CsInfoResponse csInfo) {
        return new SellerResponse(
                id, sellerName, logoUrl, description, approvalStatus, businessInfo, csInfo);
    }
}
