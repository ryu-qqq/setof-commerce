package com.ryuqq.setof.domain.seller.vo;

/**
 * 사업자 정보 Value Object (Embedded VO)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>사업자등록번호, 대표자명, 사업장주소 필수
 *   <li>통신판매업 신고번호 선택
 * </ul>
 *
 * @param registrationNumber 사업자등록번호 (필수)
 * @param saleReportNumber 통신판매업 신고번호 (선택)
 * @param representative 대표자명 (필수)
 * @param address 사업장 주소 (필수)
 */
public record BusinessInfo(
        RegistrationNumber registrationNumber,
        SaleReportNumber saleReportNumber,
        Representative representative,
        BusinessAddress address) {

    /** Compact Constructor - null 검증 */
    public BusinessInfo {
        if (registrationNumber == null) {
            throw new IllegalArgumentException("사업자등록번호는 필수입니다.");
        }
        if (representative == null) {
            throw new IllegalArgumentException("대표자명은 필수입니다.");
        }
        if (address == null) {
            throw new IllegalArgumentException("사업장 주소는 필수입니다.");
        }
    }

    /**
     * Static Factory Method
     *
     * @param registrationNumber 사업자등록번호 (필수)
     * @param saleReportNumber 통신판매업 신고번호 (선택)
     * @param representative 대표자명 (필수)
     * @param address 사업장 주소 (필수)
     * @return BusinessInfo 인스턴스
     */
    public static BusinessInfo of(
            RegistrationNumber registrationNumber,
            SaleReportNumber saleReportNumber,
            Representative representative,
            BusinessAddress address) {
        return new BusinessInfo(registrationNumber, saleReportNumber, representative, address);
    }

    /**
     * 통신판매업 신고번호 존재 여부 확인
     *
     * @return 신고번호가 있으면 true
     */
    public boolean hasSaleReportNumber() {
        return saleReportNumber != null && saleReportNumber.hasValue();
    }

    // ========== Law of Demeter Helper Methods ==========

    public String getRegistrationNumberValue() {
        return registrationNumber.value();
    }

    public String getSaleReportNumberValue() {
        return saleReportNumber != null ? saleReportNumber.value() : null;
    }

    public String getRepresentativeValue() {
        return representative.value();
    }

    public String getAddressLine1() {
        return address.addressLine1();
    }

    public String getAddressLine2() {
        return address.addressLine2();
    }

    public String getZipCode() {
        return address.zipCode();
    }

    public String getFullAddress() {
        return address.fullAddress();
    }
}
