package com.ryuqq.setof.application.seller.dto.command;

/**
 * 셀러 등록 Command.
 *
 * <p>Seller + BusinessInfo + Address를 한번에 등록합니다. (모두 1:1 관계)
 *
 * @param seller 셀러 기본 정보
 * @param businessInfo 사업자 정보
 * @param address 주소 (출고지/반품지, 1:1)
 */
public record RegisterSellerCommand(
        SellerInfoCommand seller,
        SellerBusinessInfoCommand businessInfo,
        SellerAddressCommand address) {

    /** 셀러 기본 정보 Command. */
    public record SellerInfoCommand(
            String sellerName, String displayName, String logoUrl, String description) {}

    /** 사업자 정보 Command. */
    public record SellerBusinessInfoCommand(
            String registrationNumber,
            String companyName,
            String representative,
            String saleReportNumber,
            AddressCommand businessAddress,
            CsContactCommand csContact) {}

    /** 주소 Command. */
    public record SellerAddressCommand(
            String addressType,
            String addressName,
            AddressCommand address,
            ContactInfoCommand contactInfo,
            boolean defaultAddress) {}

    /** 공통 주소 Command. */
    public record AddressCommand(String zipCode, String line1, String line2) {}

    /** CS 연락처 Command. */
    public record CsContactCommand(String phone, String email, String mobile) {}

    /** 담당자 연락처 Command. */
    public record ContactInfoCommand(String name, String phone) {}
}
