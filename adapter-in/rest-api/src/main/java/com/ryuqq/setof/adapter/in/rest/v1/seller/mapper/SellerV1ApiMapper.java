package com.ryuqq.setof.adapter.in.rest.v1.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse.BusinessInfoResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import org.springframework.stereotype.Component;

/**
 * SellerV1ApiMapper - 셀러 V1 Public API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>레거시 SellerController.fetchSeller 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerV1ApiMapper {

    /**
     * SellerCompositeResult → SellerV1ApiResponse 변환.
     *
     * <p>고객용 API에서 셀러 기본 정보 + CS 정보 + 사업자 정보를 제공합니다.
     *
     * @param result SellerCompositeResult
     * @return SellerV1ApiResponse
     */
    public SellerV1ApiResponse toResponse(SellerCompositeResult result) {
        SellerCompositeResult.SellerInfo seller = result.seller();
        CsInfoResponse csInfo = toCsInfoResponse(result.csInfo());
        BusinessInfoResponse businessInfo = toBusinessInfoResponse(result.businessInfo());

        return new SellerV1ApiResponse(
                seller.id() != null ? seller.id() : 0L,
                seller.sellerName(),
                seller.displayName(),
                seller.logoUrl(),
                seller.description(),
                csInfo,
                businessInfo);
    }

    private CsInfoResponse toCsInfoResponse(SellerCompositeResult.CsInfo csInfo) {
        if (csInfo == null) {
            return null;
        }
        return new CsInfoResponse(
                csInfo.csPhone(),
                csInfo.csEmail(),
                csInfo.operatingStartTime(),
                csInfo.operatingEndTime(),
                csInfo.operatingDays(),
                csInfo.kakaoChannelUrl());
    }

    private BusinessInfoResponse toBusinessInfoResponse(
            SellerCompositeResult.BusinessInfo businessInfo) {
        if (businessInfo == null) {
            return null;
        }
        return new BusinessInfoResponse(
                businessInfo.registrationNumber(),
                businessInfo.companyName(),
                businessInfo.representative(),
                businessInfo.saleReportNumber());
    }
}
