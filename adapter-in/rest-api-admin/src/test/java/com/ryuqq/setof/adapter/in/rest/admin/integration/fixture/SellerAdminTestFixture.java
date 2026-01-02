package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerApprovalStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerInfoContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerInfoContextV1ApiRequest.SellerBusinessInfoV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerInfoContextV1ApiRequest.SellerInfoInsertV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerInfoContextV1ApiRequest.SellerShippingInfoV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerUpdateDetailV1ApiRequest;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.response.BusinessInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Seller Admin 통합 테스트 Fixture
 *
 * <p>Admin API 통합 테스트에서 사용하는 Seller 관련 상수 및 Request/Response 빌더를 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public final class SellerAdminTestFixture {

    private SellerAdminTestFixture() {
        // Utility class
    }

    // ============================================================
    // Seller Constants
    // ============================================================

    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final Long NON_EXISTENT_SELLER_ID = 999999L;
    public static final String DEFAULT_SELLER_NAME = "테스트 셀러";
    public static final String DEFAULT_SELLER_LOGO_URL = "https://example.com/logo.jpg";
    public static final String DEFAULT_SELLER_DESCRIPTION = "테스트 셀러 설명입니다.";
    public static final Double DEFAULT_COMMISSION_RATE = 5.0;

    // ============================================================
    // Business Info Constants
    // ============================================================

    public static final String DEFAULT_REGISTRATION_NUMBER = "123-45-67890";
    public static final String DEFAULT_COMPANY_NAME = "테스트 회사";
    public static final String DEFAULT_BUSINESS_ADDRESS_LINE1 = "서울시 강남구 테헤란로 123";
    public static final String DEFAULT_BUSINESS_ADDRESS_LINE2 = "1층";
    public static final String DEFAULT_BUSINESS_ZIP_CODE = "12345";
    public static final String DEFAULT_BANK_NAME = "신한은행";
    public static final String DEFAULT_ACCOUNT_NUMBER = "110123456789";
    public static final String DEFAULT_ACCOUNT_HOLDER_NAME = "홍길동";
    public static final String DEFAULT_SALE_REPORT_NUMBER = "2024-서울강남-1234";
    public static final String DEFAULT_REPRESENTATIVE = "홍길동";

    // ============================================================
    // CS Info Constants
    // ============================================================

    public static final String DEFAULT_CS_NUMBER = "1234";
    public static final String DEFAULT_CS_PHONE_NUMBER = "1588-0000";
    public static final String DEFAULT_CS_EMAIL = "cs@example.com";

    // ============================================================
    // Shipping Info Constants
    // ============================================================

    public static final String DEFAULT_RETURN_ADDRESS_LINE1 = "서울시 강남구 테헤란로 456";
    public static final String DEFAULT_RETURN_ADDRESS_LINE2 = "2층";
    public static final String DEFAULT_RETURN_ZIP_CODE = "12346";

    // ============================================================
    // Status Constants
    // ============================================================

    public static final String DEFAULT_APPROVAL_STATUS = "PENDING";
    public static final String APPROVED_STATUS = "APPROVED";
    public static final String REJECTED_STATUS = "REJECTED";

    // ============================================================
    // V1 Request Builders
    // ============================================================

    /**
     * V1 셀러 등록 요청을 생성합니다.
     *
     * @return SellerInfoContextV1ApiRequest
     */
    public static SellerInfoContextV1ApiRequest createSellerInfoContextV1Request() {
        return createSellerInfoContextV1Request(DEFAULT_SELLER_NAME);
    }

    /**
     * 커스텀 셀러명으로 V1 셀러 등록 요청을 생성합니다.
     *
     * @param sellerName 셀러명
     * @return SellerInfoContextV1ApiRequest
     */
    public static SellerInfoContextV1ApiRequest createSellerInfoContextV1Request(
            String sellerName) {
        return new SellerInfoContextV1ApiRequest(
                createSellerInfoInsertV1Request(sellerName),
                createSellerBusinessInfoV1Request(),
                createSellerShippingInfoV1Request());
    }

    /** V1 셀러 기본 정보 요청을 생성합니다. */
    public static SellerInfoInsertV1ApiRequest createSellerInfoInsertV1Request(String sellerName) {
        return new SellerInfoInsertV1ApiRequest(
                sellerName,
                DEFAULT_SELLER_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                DEFAULT_COMMISSION_RATE,
                "Y",
                "Y");
    }

    /** V1 셀러 사업자 정보 요청을 생성합니다. */
    public static SellerBusinessInfoV1ApiRequest createSellerBusinessInfoV1Request() {
        return new SellerBusinessInfoV1ApiRequest(
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_COMPANY_NAME,
                DEFAULT_BUSINESS_ADDRESS_LINE1,
                DEFAULT_BUSINESS_ADDRESS_LINE2,
                DEFAULT_BUSINESS_ZIP_CODE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER_NAME,
                DEFAULT_CS_NUMBER,
                DEFAULT_CS_PHONE_NUMBER,
                DEFAULT_CS_EMAIL,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE);
    }

    /** V1 셀러 배송 정보 요청을 생성합니다. */
    public static SellerShippingInfoV1ApiRequest createSellerShippingInfoV1Request() {
        return new SellerShippingInfoV1ApiRequest(
                DEFAULT_RETURN_ADDRESS_LINE1,
                DEFAULT_RETURN_ADDRESS_LINE2,
                DEFAULT_RETURN_ZIP_CODE);
    }

    /**
     * V1 셀러 수정 요청을 생성합니다.
     *
     * @return SellerUpdateDetailV1ApiRequest
     */
    public static SellerUpdateDetailV1ApiRequest createSellerUpdateDetailV1Request() {
        return createSellerUpdateDetailV1Request("수정된 셀러명");
    }

    /**
     * 커스텀 값으로 V1 셀러 수정 요청을 생성합니다.
     *
     * @param sellerName 셀러명
     * @return SellerUpdateDetailV1ApiRequest
     */
    public static SellerUpdateDetailV1ApiRequest createSellerUpdateDetailV1Request(
            String sellerName) {
        return new SellerUpdateDetailV1ApiRequest(
                sellerName,
                DEFAULT_CS_EMAIL,
                DEFAULT_CS_NUMBER,
                DEFAULT_COMMISSION_RATE,
                DEFAULT_RETURN_ADDRESS_LINE1,
                DEFAULT_RETURN_ADDRESS_LINE2,
                DEFAULT_RETURN_ZIP_CODE,
                List.of(1L, 2L));
    }

    /**
     * V1 셀러 승인 상태 변경 요청을 생성합니다.
     *
     * @param sellerIds 셀러 ID 목록
     * @param status 승인 상태
     * @return SellerApprovalStatusV1ApiRequest
     */
    public static SellerApprovalStatusV1ApiRequest createSellerApprovalStatusV1Request(
            List<Long> sellerIds, String status) {
        return new SellerApprovalStatusV1ApiRequest(sellerIds, status);
    }

    // ============================================================
    // Response Builders for Mocking
    // ============================================================

    /**
     * SellerResponse Mock 데이터를 생성합니다.
     *
     * @return SellerResponse
     */
    public static SellerResponse createSellerResponse() {
        return createSellerResponse(DEFAULT_SELLER_ID, APPROVED_STATUS);
    }

    /**
     * 커스텀 값으로 SellerResponse Mock 데이터를 생성합니다.
     *
     * @param sellerId 셀러 ID
     * @param approvalStatus 승인 상태
     * @return SellerResponse
     */
    public static SellerResponse createSellerResponse(Long sellerId, String approvalStatus) {
        return SellerResponse.of(
                sellerId,
                DEFAULT_SELLER_NAME,
                DEFAULT_SELLER_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                approvalStatus,
                createBusinessInfoResponse(),
                createCsInfoResponse());
    }

    /**
     * BusinessInfoResponse Mock 데이터를 생성합니다.
     *
     * @return BusinessInfoResponse
     */
    public static BusinessInfoResponse createBusinessInfoResponse() {
        return BusinessInfoResponse.of(
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BUSINESS_ADDRESS_LINE1,
                DEFAULT_BUSINESS_ADDRESS_LINE2,
                DEFAULT_BUSINESS_ZIP_CODE);
    }

    /**
     * CsInfoResponse Mock 데이터를 생성합니다.
     *
     * @return CsInfoResponse
     */
    public static CsInfoResponse createCsInfoResponse() {
        return CsInfoResponse.of(DEFAULT_CS_EMAIL, DEFAULT_CS_PHONE_NUMBER, DEFAULT_CS_NUMBER);
    }

    /**
     * SellerSummaryResponse Mock 데이터를 생성합니다.
     *
     * @return SellerSummaryResponse
     */
    public static SellerSummaryResponse createSellerSummaryResponse() {
        return createSellerSummaryResponse(DEFAULT_SELLER_ID);
    }

    /**
     * 커스텀 ID로 SellerSummaryResponse Mock 데이터를 생성합니다.
     *
     * @param sellerId 셀러 ID
     * @return SellerSummaryResponse
     */
    public static SellerSummaryResponse createSellerSummaryResponse(Long sellerId) {
        return SellerSummaryResponse.of(
                sellerId, DEFAULT_SELLER_NAME, DEFAULT_SELLER_LOGO_URL, APPROVED_STATUS);
    }

    /**
     * 여러 SellerSummaryResponse를 생성합니다.
     *
     * @param count 개수
     * @return SellerSummaryResponse 목록
     */
    public static List<SellerSummaryResponse> createSellerSummaryResponses(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> createSellerSummaryResponse((long) i))
                .toList();
    }

    /**
     * PageResponse 형태의 SellerSummaryResponse Mock 데이터를 생성합니다.
     *
     * @param count 개수
     * @param totalCount 전체 개수
     * @return PageResponse
     */
    public static PageResponse<SellerSummaryResponse> createSellerSummaryPageResponse(
            int count, long totalCount) {
        List<SellerSummaryResponse> content = createSellerSummaryResponses(count);
        int page = 0;
        int size = 20;
        int totalPages = (int) Math.ceil((double) totalCount / size);
        boolean first = page == 0;
        boolean last = page >= totalPages - 1;
        return PageResponse.of(content, page, size, totalCount, totalPages, first, last);
    }
}
