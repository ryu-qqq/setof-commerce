package com.ryuqq.setof.application.seller.dto.command;

/**
 * 셀러 주소 수정 Command.
 *
 * @param addressId 주소 ID
 * @param addressName 주소명
 * @param address 주소
 * @param contactInfo 담당자 연락처
 */
public record UpdateSellerAddressCommand(
        Long addressId,
        String addressName,
        AddressCommand address,
        ContactInfoCommand contactInfo) {

    /** 공통 주소 Command. */
    public record AddressCommand(String zipCode, String line1, String line2) {}

    /** 담당자 연락처 Command. */
    public record ContactInfoCommand(String name, String phone) {}
}
