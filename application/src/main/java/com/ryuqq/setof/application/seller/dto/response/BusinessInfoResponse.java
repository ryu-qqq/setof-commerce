package com.ryuqq.setof.application.seller.dto.response;

/**
 * 사업자 정보 응답 DTO
 *
 * @param registrationNumber 사업자 등록번호
 * @param saleReportNumber 통신판매업 신고번호
 * @param representative 대표자명
 * @param addressLine1 사업장 주소 (기본)
 * @param addressLine2 사업장 주소 (상세)
 * @param zipCode 우편번호
 * @author development-team
 * @since 1.0.0
 */
public record BusinessInfoResponse(
        String registrationNumber,
        String saleReportNumber,
        String representative,
        String addressLine1,
        String addressLine2,
        String zipCode) {

    /**
     * Static Factory Method
     *
     * @param registrationNumber 사업자 등록번호
     * @param saleReportNumber 통신판매업 신고번호
     * @param representative 대표자명
     * @param addressLine1 사업장 주소 (기본)
     * @param addressLine2 사업장 주소 (상세)
     * @param zipCode 우편번호
     * @return BusinessInfoResponse 인스턴스
     */
    public static BusinessInfoResponse of(
            String registrationNumber,
            String saleReportNumber,
            String representative,
            String addressLine1,
            String addressLine2,
            String zipCode) {
        return new BusinessInfoResponse(
                registrationNumber,
                saleReportNumber,
                representative,
                addressLine1,
                addressLine2,
                zipCode);
    }
}
