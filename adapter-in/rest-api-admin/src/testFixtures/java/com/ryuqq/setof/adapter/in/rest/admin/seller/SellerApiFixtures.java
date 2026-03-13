package com.ryuqq.setof.adapter.in.rest.admin.seller;

import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SearchSellersApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.time.Instant;
import java.util.List;

/**
 * Seller API 테스트 Fixtures.
 *
 * <p>셀러 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class SellerApiFixtures {

    private SellerApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final String DEFAULT_SELLER_NAME = "테스트셀러";
    public static final String DEFAULT_DISPLAY_NAME = "테스트";
    public static final String DEFAULT_LOGO_URL = "https://example.com/logo.png";
    public static final String DEFAULT_DESCRIPTION = "테스트 셀러 설명";
    public static final String DEFAULT_REGISTRATION_NUMBER = "123-45-67890";
    public static final String DEFAULT_COMPANY_NAME = "테스트주식회사";
    public static final String DEFAULT_REPRESENTATIVE = "홍길동";
    public static final String DEFAULT_SALE_REPORT_NUMBER = "2025-서울강남-0001";
    public static final String DEFAULT_ZIP_CODE = "06236";
    public static final String DEFAULT_ADDRESS_LINE1 = "서울특별시 강남구 테헤란로 427";
    public static final String DEFAULT_ADDRESS_LINE2 = "위워크타워 10층";
    public static final String DEFAULT_CS_PHONE = "02-1234-5678";
    public static final String DEFAULT_CS_EMAIL = "cs@test.com";
    public static final String DEFAULT_CS_MOBILE = "010-1234-5678";
    public static final Instant DEFAULT_CREATED_AT = Instant.parse("2025-01-26T01:30:00Z");
    public static final Instant DEFAULT_UPDATED_AT = Instant.parse("2025-01-26T01:30:00Z");

    // ===== Register Request Fixtures =====

    public static RegisterSellerApiRequest registerRequest() {
        return new RegisterSellerApiRequest(
                new RegisterSellerApiRequest.SellerInfoRequest(
                        DEFAULT_SELLER_NAME,
                        DEFAULT_DISPLAY_NAME,
                        DEFAULT_LOGO_URL,
                        DEFAULT_DESCRIPTION),
                new RegisterSellerApiRequest.BusinessInfoRequest(
                        DEFAULT_REGISTRATION_NUMBER,
                        DEFAULT_COMPANY_NAME,
                        DEFAULT_REPRESENTATIVE,
                        DEFAULT_SALE_REPORT_NUMBER,
                        new RegisterSellerApiRequest.AddressRequest(
                                DEFAULT_ZIP_CODE, DEFAULT_ADDRESS_LINE1, DEFAULT_ADDRESS_LINE2),
                        new RegisterSellerApiRequest.CsContactRequest(
                                DEFAULT_CS_PHONE, DEFAULT_CS_EMAIL, DEFAULT_CS_MOBILE)));
    }

    // ===== Update Request Fixtures =====

    public static UpdateSellerApiRequest updateRequest() {
        return new UpdateSellerApiRequest(
                "수정된셀러",
                "수정된표시명",
                "https://example.com/new-logo.png",
                "수정된 설명",
                new UpdateSellerApiRequest.CsInfoRequest(
                        "02-9999-9999", "new-cs@test.com", "010-9999-9999"),
                new UpdateSellerApiRequest.BusinessInfoRequest(
                        DEFAULT_REGISTRATION_NUMBER,
                        DEFAULT_COMPANY_NAME,
                        DEFAULT_REPRESENTATIVE,
                        DEFAULT_SALE_REPORT_NUMBER,
                        new UpdateSellerApiRequest.AddressRequest(
                                DEFAULT_ZIP_CODE, DEFAULT_ADDRESS_LINE1, DEFAULT_ADDRESS_LINE2)));
    }

    public static UpdateSellerApiRequest updateRequestWithoutOptional() {
        return new UpdateSellerApiRequest(
                "수정된셀러", "수정된표시명", "https://example.com/logo.png", "수정된 설명", null, null);
    }

    // ===== Search Request Fixtures =====

    public static SearchSellersApiRequest searchRequest() {
        return new SearchSellersApiRequest(null, null, null, "CREATED_AT", "DESC", 0, 20);
    }

    public static SearchSellersApiRequest searchRequest(Boolean active, int page, int size) {
        return new SearchSellersApiRequest(active, null, null, "CREATED_AT", "DESC", page, size);
    }

    public static SearchSellersApiRequest searchRequestWithNullPage() {
        return new SearchSellersApiRequest(null, null, null, "CREATED_AT", "DESC", null, null);
    }

    // ===== Application Result Fixtures =====

    public static SellerResult sellerResult() {
        return sellerResult(DEFAULT_SELLER_ID, true);
    }

    public static SellerResult sellerResult(Long id, boolean active) {
        return new SellerResult(
                id,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                active,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static SellerPageResult sellerPageResult() {
        return sellerPageResult(List.of(sellerResult()), 0, 20, 1);
    }

    public static SellerPageResult sellerPageResult(
            List<SellerResult> results, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new SellerPageResult(results, pageMeta);
    }

    public static SellerPageResult emptySellerPageResult() {
        PageMeta pageMeta = PageMeta.of(0, 20, 0);
        return new SellerPageResult(List.of(), pageMeta);
    }

    public static List<SellerResult> multipleSellerResults() {
        return List.of(sellerResult(1L, true), sellerResult(2L, true), sellerResult(3L, false));
    }

    public static SellerCompositeResult sellerCompositeResult() {
        return sellerCompositeResult(DEFAULT_SELLER_ID);
    }

    public static SellerCompositeResult sellerCompositeResult(Long sellerId) {
        SellerCompositeResult.SellerInfo sellerInfo =
                new SellerCompositeResult.SellerInfo(
                        sellerId,
                        DEFAULT_SELLER_NAME,
                        DEFAULT_DISPLAY_NAME,
                        DEFAULT_LOGO_URL,
                        DEFAULT_DESCRIPTION,
                        true,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT);

        SellerCompositeResult.AddressInfo addressInfo =
                new SellerCompositeResult.AddressInfo(
                        10L,
                        "BUSINESS",
                        "본사",
                        DEFAULT_ZIP_CODE,
                        DEFAULT_ADDRESS_LINE1,
                        DEFAULT_ADDRESS_LINE2,
                        DEFAULT_REPRESENTATIVE,
                        DEFAULT_CS_PHONE,
                        true);

        SellerCompositeResult.BusinessInfo businessInfo =
                new SellerCompositeResult.BusinessInfo(
                        20L,
                        DEFAULT_REGISTRATION_NUMBER,
                        DEFAULT_COMPANY_NAME,
                        DEFAULT_REPRESENTATIVE,
                        DEFAULT_SALE_REPORT_NUMBER,
                        DEFAULT_ZIP_CODE,
                        DEFAULT_ADDRESS_LINE1,
                        DEFAULT_ADDRESS_LINE2);

        SellerCompositeResult.CsInfo csInfo =
                new SellerCompositeResult.CsInfo(
                        30L,
                        DEFAULT_CS_PHONE,
                        DEFAULT_CS_MOBILE,
                        DEFAULT_CS_EMAIL,
                        Instant.parse("2025-01-26T00:00:00Z"),
                        Instant.parse("2025-01-26T09:00:00Z"),
                        "MON,TUE,WED,THU,FRI",
                        "https://kakao.channel/test");

        return new SellerCompositeResult(sellerInfo, addressInfo, businessInfo, csInfo);
    }

    // ===== API Response Fixtures =====

    public static SellerApiResponse sellerApiResponse() {
        return sellerApiResponse(DEFAULT_SELLER_ID);
    }

    public static SellerApiResponse sellerApiResponse(Long id) {
        return new SellerApiResponse(
                id,
                DEFAULT_SELLER_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_DESCRIPTION,
                true,
                "2025-01-26T10:30:00+09:00",
                "2025-01-26T10:30:00+09:00");
    }

    public static SellerDetailApiResponse sellerDetailApiResponse() {
        return sellerDetailApiResponse(DEFAULT_SELLER_ID);
    }

    public static SellerDetailApiResponse sellerDetailApiResponse(Long sellerId) {
        SellerDetailApiResponse.SellerInfo sellerInfo =
                new SellerDetailApiResponse.SellerInfo(
                        sellerId,
                        DEFAULT_SELLER_NAME,
                        DEFAULT_DISPLAY_NAME,
                        DEFAULT_LOGO_URL,
                        DEFAULT_DESCRIPTION,
                        true,
                        "2025-01-26T10:30:00+09:00",
                        "2025-01-26T10:30:00+09:00");

        SellerDetailApiResponse.BusinessInfo businessInfo =
                new SellerDetailApiResponse.BusinessInfo(
                        20L,
                        DEFAULT_REGISTRATION_NUMBER,
                        DEFAULT_COMPANY_NAME,
                        DEFAULT_REPRESENTATIVE,
                        DEFAULT_SALE_REPORT_NUMBER,
                        DEFAULT_ZIP_CODE,
                        DEFAULT_ADDRESS_LINE1,
                        DEFAULT_ADDRESS_LINE2);

        SellerDetailApiResponse.CsInfo csInfo =
                new SellerDetailApiResponse.CsInfo(
                        30L,
                        DEFAULT_CS_PHONE,
                        DEFAULT_CS_MOBILE,
                        DEFAULT_CS_EMAIL,
                        "2025-01-26 09:00:00",
                        "2025-01-26 18:00:00",
                        "MON,TUE,WED,THU,FRI",
                        "https://kakao.channel/test");

        return new SellerDetailApiResponse(sellerInfo, businessInfo, csInfo);
    }
}
