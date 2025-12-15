package com.ryuqq.setof.adapter.in.rest.v1.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.seller.dto.response.BusinessInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import org.springframework.stereotype.Component;

/**
 * Seller V2 → V1 API 응답 변환 매퍼
 *
 * <p>Application 레이어 응답(SellerResponse)을 V1 응답(SellerV1ApiResponse)으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerV1ApiMapper {

    /**
     * SellerResponse를 V1 응답으로 변환
     *
     * @param response Application 레이어 셀러 응답
     * @return V1 셀러 응답
     */
    public SellerV1ApiResponse toV1Response(SellerResponse response) {
        BusinessInfoResponse businessInfo = response.businessInfo();
        CsInfoResponse csInfo = response.csInfo();

        String address = buildAddress(businessInfo);
        String csPhoneNumber = csInfo != null ? csInfo.landlinePhone() : null;
        String alimTalkPhoneNumber = csInfo != null ? csInfo.mobilePhone() : null;
        String email = csInfo != null ? csInfo.email() : null;

        String registrationNumber = businessInfo != null ? businessInfo.registrationNumber() : null;
        String saleReportNumber = businessInfo != null ? businessInfo.saleReportNumber() : null;
        String representative = businessInfo != null ? businessInfo.representative() : null;

        return new SellerV1ApiResponse(
                response.id(),
                response.sellerName(),
                response.logoUrl(),
                response.description(),
                address,
                csPhoneNumber,
                alimTalkPhoneNumber,
                registrationNumber,
                saleReportNumber,
                representative,
                email);
    }

    private String buildAddress(BusinessInfoResponse businessInfo) {
        if (businessInfo == null) {
            return null;
        }

        String addressLine1 = businessInfo.addressLine1();
        String addressLine2 = businessInfo.addressLine2();

        if (addressLine1 == null && addressLine2 == null) {
            return null;
        }

        if (addressLine2 == null || addressLine2.isBlank()) {
            return addressLine1;
        }

        if (addressLine1 == null || addressLine1.isBlank()) {
            return addressLine2;
        }

        return addressLine1 + " " + addressLine2;
    }
}
