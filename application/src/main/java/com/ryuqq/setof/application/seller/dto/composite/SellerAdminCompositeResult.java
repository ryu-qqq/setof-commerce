package com.ryuqq.setof.application.seller.dto.composite;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * SellerAdminCompositeResult - Admin용 셀러 Composite 조회 결과.
 *
 * <p>Seller + SellerAddress + SellerBusinessInfo + SellerCs + SellerContract + SellerSettlement 조인
 * 결과.
 */
public record SellerAdminCompositeResult(
        SellerInfo seller,
        AddressInfo address,
        BusinessInfo businessInfo,
        CsInfo csInfo,
        ContractInfo contractInfo,
        SettlementInfo settlementInfo) {

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

    public record ContractInfo(
            Long id,
            BigDecimal commissionRate,
            LocalDate contractStartDate,
            LocalDate contractEndDate,
            String status,
            String specialTerms,
            Instant createdAt,
            Instant updatedAt) {}

    public record SettlementInfo(
            Long id,
            String bankCode,
            String bankName,
            String accountNumber,
            String accountHolderName,
            String settlementCycle,
            Integer settlementDay,
            boolean verified,
            Instant verifiedAt,
            Instant createdAt,
            Instant updatedAt) {}
}
