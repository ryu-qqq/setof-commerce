package com.ryuqq.setof.adapter.out.persistence.composite.seller.dto;

import java.time.Instant;

/**
 * SellerCompositeDto - 셀러 Composite 조회 DTO.
 *
 * <p>Seller + SellerAddress + SellerBusinessInfo + SellerCs 조인 결과.
 *
 * <p>GetSellerForCustomerService, SearchSellerByOffsetService에서 사용.
 */
public record SellerCompositeDto(
        // Seller
        Long sellerId,
        String sellerName,
        String displayName,
        String logoUrl,
        String description,
        boolean active,
        Instant sellerCreatedAt,
        Instant sellerUpdatedAt,

        // SellerAddress
        Long addressId,
        String addressType,
        String addressName,
        String zipcode,
        String address,
        String addressDetail,
        String contactName,
        String contactPhone,
        boolean defaultAddress,

        // SellerBusinessInfo
        Long businessInfoId,
        String registrationNumber,
        String companyName,
        String representative,
        String saleReportNumber,
        String businessZipcode,
        String businessAddress,
        String businessAddressDetail,

        // SellerCs
        Long csId,
        String csPhone,
        String csMobile,
        String csEmail,
        Instant operatingStartTime,
        Instant operatingEndTime,
        String operatingDays,
        String kakaoChannelUrl) {}
