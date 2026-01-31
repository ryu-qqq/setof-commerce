package com.ryuqq.setof.application.seller.dto.composite;

import java.time.Instant;

/**
 * SellerCompositeResult - 셀러 Composite 조회 결과.
 *
 * <p>Seller + SellerAddress + SellerBusinessInfo + SellerCs 조인 결과.
 */
public record SellerCompositeResult(
        SellerInfo seller, AddressInfo address, BusinessInfo businessInfo, CsInfo csInfo) {

    public record SellerInfo(
            Long id,
            String sellerName,
            String displayName,
            String logoUrl,
            String description,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {}

    public record AddressInfo(
            Long id,
            String addressType,
            String addressName,
            String zipcode,
            String address,
            String addressDetail,
            String contactName,
            String contactPhone,
            boolean defaultAddress) {}

    public record BusinessInfo(
            Long id,
            String registrationNumber,
            String companyName,
            String representative,
            String saleReportNumber,
            String businessZipcode,
            String businessAddress,
            String businessAddressDetail) {}

    public record CsInfo(
            Long id,
            String csPhone,
            String csMobile,
            String csEmail,
            String operatingStartTime,
            String operatingEndTime,
            String operatingDays,
            String kakaoChannelUrl) {}
}
