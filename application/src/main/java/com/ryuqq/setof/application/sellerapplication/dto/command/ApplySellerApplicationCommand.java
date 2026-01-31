package com.ryuqq.setof.application.sellerapplication.dto.command;

/**
 * 셀러 입점 신청 Command.
 *
 * <p>입점 신청에 필요한 모든 정보를 전달합니다.
 *
 * @param sellerInfo 셀러 기본 정보
 * @param businessInfo 사업자 정보
 * @param csContact CS 연락처 정보
 * @param addressInfo 주소 정보 (출고지/반품지)
 * @author ryu-qqq
 */
public record ApplySellerApplicationCommand(
        SellerInfoCommand sellerInfo,
        BusinessInfoCommand businessInfo,
        CsContactCommand csContact,
        AddressInfoCommand addressInfo) {

    /**
     * 셀러 기본 정보 Command.
     *
     * @param sellerName 셀러명
     * @param displayName 표시명
     * @param logoUrl 로고 URL (선택)
     * @param description 설명 (선택)
     */
    public record SellerInfoCommand(
            String sellerName, String displayName, String logoUrl, String description) {}

    /**
     * 사업자 정보 Command.
     *
     * @param registrationNumber 사업자등록번호
     * @param companyName 회사명
     * @param representative 대표자명
     * @param saleReportNumber 통신판매업 신고번호 (선택)
     * @param businessAddress 사업장 주소
     */
    public record BusinessInfoCommand(
            String registrationNumber,
            String companyName,
            String representative,
            String saleReportNumber,
            AddressCommand businessAddress) {}

    /**
     * CS 연락처 Command.
     *
     * @param phone 전화번호
     * @param email 이메일
     * @param mobile 휴대폰번호 (선택)
     */
    public record CsContactCommand(String phone, String email, String mobile) {}

    /**
     * 주소 정보 Command.
     *
     * @param addressType 주소 유형 (SHIPPING / RETURN)
     * @param addressName 주소 별칭
     * @param address 주소
     * @param contactInfo 담당자 연락처
     */
    public record AddressInfoCommand(
            String addressType,
            String addressName,
            AddressCommand address,
            ContactInfoCommand contactInfo) {}

    /**
     * 공통 주소 Command.
     *
     * @param zipCode 우편번호
     * @param line1 주소1 (기본주소)
     * @param line2 주소2 (상세주소)
     */
    public record AddressCommand(String zipCode, String line1, String line2) {}

    /**
     * 담당자 연락처 Command.
     *
     * @param name 담당자명
     * @param phone 전화번호
     */
    public record ContactInfoCommand(String name, String phone) {}
}
