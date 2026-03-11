package com.ryuqq.setof.adapter.out.persistence.composite.seller.mapper;

import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.RefundPolicyDto;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.SellerCompositeDto;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.SellerPolicyCompositeDto;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.ShippingPolicyDto;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import org.springframework.stereotype.Component;

/**
 * SellerCompositeMapper - Composite DTO 변환 Mapper.
 *
 * <p>Persistence DTO를 Application Result로 변환.
 *
 * <p>LocalTime → String 변환 등 타입 변환 처리.
 */
@Component
public class SellerCompositeMapper {

    public SellerCompositeResult toResult(SellerCompositeDto dto) {
        return new SellerCompositeResult(
                new SellerCompositeResult.SellerInfo(
                        dto.sellerId(),
                        dto.sellerName(),
                        dto.displayName(),
                        dto.logoUrl(),
                        dto.description(),
                        dto.active(),
                        dto.sellerCreatedAt(),
                        dto.sellerUpdatedAt()),
                new SellerCompositeResult.AddressInfo(
                        dto.addressId(),
                        dto.addressType(),
                        dto.addressName(),
                        dto.zipcode(),
                        dto.address(),
                        dto.addressDetail(),
                        dto.contactName(),
                        dto.contactPhone(),
                        dto.defaultAddress()),
                new SellerCompositeResult.BusinessInfo(
                        dto.businessInfoId(),
                        dto.registrationNumber(),
                        dto.companyName(),
                        dto.representative(),
                        dto.saleReportNumber(),
                        dto.businessZipcode(),
                        dto.businessAddress(),
                        dto.businessAddressDetail()),
                new SellerCompositeResult.CsInfo(
                        dto.csId(),
                        dto.csPhone(),
                        dto.csMobile(),
                        dto.csEmail(),
                        dto.operatingStartTime(),
                        dto.operatingEndTime(),
                        dto.operatingDays(),
                        dto.kakaoChannelUrl()));
    }

    public SellerPolicyCompositeResult toPolicyResult(SellerPolicyCompositeDto dto) {
        return new SellerPolicyCompositeResult(
                dto.sellerId(),
                dto.shippingPolicies().stream().map(this::toShippingPolicyInfo).toList(),
                dto.refundPolicies().stream().map(this::toRefundPolicyInfo).toList());
    }

    private SellerPolicyCompositeResult.ShippingPolicyInfo toShippingPolicyInfo(
            ShippingPolicyDto dto) {
        return new SellerPolicyCompositeResult.ShippingPolicyInfo(
                dto.id(),
                dto.sellerId(),
                dto.policyName(),
                dto.defaultPolicy(),
                dto.active(),
                dto.shippingFeeType(),
                dto.baseFee(),
                dto.freeThreshold(),
                dto.jejuExtraFee(),
                dto.islandExtraFee(),
                dto.returnFee(),
                dto.exchangeFee(),
                dto.leadTimeMinDays(),
                dto.leadTimeMaxDays(),
                dto.leadTimeCutoffTime(),
                dto.createdAt(),
                dto.updatedAt());
    }

    private SellerPolicyCompositeResult.RefundPolicyInfo toRefundPolicyInfo(RefundPolicyDto dto) {
        return new SellerPolicyCompositeResult.RefundPolicyInfo(
                dto.id(),
                dto.sellerId(),
                dto.policyName(),
                dto.defaultPolicy(),
                dto.active(),
                dto.returnPeriodDays(),
                dto.exchangePeriodDays(),
                dto.nonReturnableConditions(),
                dto.partialRefundEnabled(),
                dto.inspectionRequired(),
                dto.inspectionPeriodDays(),
                dto.additionalInfo(),
                dto.createdAt(),
                dto.updatedAt());
    }
}
