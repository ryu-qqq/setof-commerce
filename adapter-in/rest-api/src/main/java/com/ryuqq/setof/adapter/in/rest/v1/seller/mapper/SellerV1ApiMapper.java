package com.ryuqq.setof.adapter.in.rest.v1.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerInfoV1ApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import org.springframework.stereotype.Component;

/**
 * SellerV1ApiMapper - V1 셀러 API 매퍼.
 *
 * <p>Application Layer의 결과를 V1 API 응답으로 변환합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 정의.
 *
 * <p>API-MAP-002: 순수 변환 로직만 포함 (비즈니스 로직 금지).
 *
 * <p>API-MAP-003: null-safe 변환 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerV1ApiMapper {

    /**
     * SellerCompositeResult를 V1 응답으로 변환.
     *
     * <p>레거시 SellerInfo 구조와 호환되도록 필드를 매핑합니다.
     *
     * @param result UseCase 실행 결과
     * @return V1 호환 셀러 정보 응답
     */
    public SellerInfoV1ApiResponse toResponse(SellerCompositeResult result) {
        var seller = result.seller();
        var address = result.address();
        var business = result.businessInfo();
        var cs = result.csInfo();

        return new SellerInfoV1ApiResponse(
                seller.id(),
                seller.sellerName(),
                seller.logoUrl(),
                seller.description(),
                buildFullAddress(address),
                getCsPhone(cs),
                getCsMobile(cs),
                getRegistrationNumber(business),
                getSaleReportNumber(business),
                getRepresentative(business),
                getCsEmail(cs));
    }

    private String getCsPhone(SellerCompositeResult.CsInfo cs) {
        return cs != null ? nullSafe(cs.csPhone()) : "";
    }

    private String getCsMobile(SellerCompositeResult.CsInfo cs) {
        return cs != null ? nullSafe(cs.csMobile()) : "";
    }

    private String getCsEmail(SellerCompositeResult.CsInfo cs) {
        return cs != null ? nullSafe(cs.csEmail()) : "";
    }

    private String getRegistrationNumber(SellerCompositeResult.BusinessInfo business) {
        return business != null ? nullSafe(business.registrationNumber()) : "";
    }

    private String getSaleReportNumber(SellerCompositeResult.BusinessInfo business) {
        return business != null ? nullSafe(business.saleReportNumber()) : "";
    }

    private String getRepresentative(SellerCompositeResult.BusinessInfo business) {
        return business != null ? nullSafe(business.representative()) : "";
    }

    /**
     * 주소 정보를 전체 주소 문자열로 변환.
     *
     * <p>레거시 호환: address + addressDetail을 합쳐서 반환합니다.
     *
     * @param address 주소 정보
     * @return 전체 주소 문자열
     */
    private String buildFullAddress(SellerCompositeResult.AddressInfo address) {
        if (address == null) {
            return null;
        }

        String baseAddress = nullSafe(address.address());
        String detail = nullSafe(address.addressDetail());

        if (baseAddress.isEmpty()) {
            return detail;
        }
        if (detail.isEmpty()) {
            return baseAddress;
        }
        return baseAddress + " " + detail;
    }

    /**
     * null을 빈 문자열로 변환.
     *
     * @param value 원본 값
     * @return null이면 빈 문자열, 아니면 원본 값
     */
    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
