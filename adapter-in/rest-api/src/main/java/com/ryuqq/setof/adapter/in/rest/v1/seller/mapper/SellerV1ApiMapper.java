package com.ryuqq.setof.adapter.in.rest.v1.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
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
 * <p>레거시 필드 매핑 규칙:
 *
 * <ul>
 *   <li>sellerName = seller_business_info.company_name (BusinessInfo.companyName)
 *   <li>address = businessAddress + " " + businessAddressDetail + " " + businessZipcode
 *   <li>csPhoneNumber = COALESCE(cs_number, cs_phone_number) → CsInfo.csPhone
 *   <li>alimTalkPhoneNumber = cs_phone_number → CsInfo.csMobile
 *   <li>email = cs_email → CsInfo.csEmail
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerV1ApiMapper {

    /**
     * SellerCompositeResult → SellerV1ApiResponse 변환.
     *
     * <p>레거시 flat 구조(SellerInfo)와 동일한 필드 순서 및 매핑 규칙을 적용합니다.
     *
     * @param result SellerCompositeResult
     * @return SellerV1ApiResponse
     */
    public SellerV1ApiResponse toResponse(SellerCompositeResult result) {
        SellerCompositeResult.SellerInfo seller = result.seller();
        SellerCompositeResult.BusinessInfo businessInfo = result.businessInfo();
        SellerCompositeResult.CsInfo csInfo = result.csInfo();

        long sellerId = seller.id() != null ? seller.id() : 0L;

        // sellerName = seller_business_info.company_name
        String sellerName = businessInfo != null ? businessInfo.companyName() : "";

        String logoUrl = seller.logoUrl();

        // sellerDescription = COALESCE(seller_description, "")
        String sellerDescription = seller.description() != null ? seller.description() : "";

        // address = businessAddress + " " + businessAddressDetail + " " + businessZipcode
        String address = buildAddress(businessInfo);

        // csPhoneNumber = COALESCE(cs_number, cs_phone_number) → CsInfo.csPhone
        String csPhoneNumber = csInfo != null ? csInfo.csPhone() : "";

        // alimTalkPhoneNumber = cs_phone_number → CsInfo.csMobile
        String alimTalkPhoneNumber = csInfo != null ? csInfo.csMobile() : "";

        String registrationNumber = businessInfo != null ? businessInfo.registrationNumber() : "";
        String saleReportNumber = businessInfo != null ? businessInfo.saleReportNumber() : "";
        String representative = businessInfo != null ? businessInfo.representative() : "";

        // email = cs_email → CsInfo.csEmail
        String email = csInfo != null ? csInfo.csEmail() : "";

        return new SellerV1ApiResponse(
                sellerId,
                sellerName,
                logoUrl,
                sellerDescription,
                address,
                csPhoneNumber,
                alimTalkPhoneNumber,
                registrationNumber,
                saleReportNumber,
                representative,
                email);
    }

    /**
     * 사업장 주소 조합.
     *
     * <p>레거시: business_address_line1 + " " + business_address_line2 + " " + zip_code
     *
     * <p>신규: businessAddress + " " + businessAddressDetail + " " + businessZipcode
     *
     * @param businessInfo BusinessInfo
     * @return 조합된 주소 문자열
     */
    private String buildAddress(SellerCompositeResult.BusinessInfo businessInfo) {
        if (businessInfo == null) {
            return "";
        }
        String address =
                businessInfo.businessAddress() != null ? businessInfo.businessAddress() : "";
        String detail =
                businessInfo.businessAddressDetail() != null
                        ? businessInfo.businessAddressDetail()
                        : "";
        String zipcode =
                businessInfo.businessZipcode() != null ? businessInfo.businessZipcode() : "";
        return (address + " " + detail + " " + zipcode).trim();
    }
}
