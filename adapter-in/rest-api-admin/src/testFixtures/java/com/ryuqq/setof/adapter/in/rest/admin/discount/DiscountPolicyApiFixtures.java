package com.ryuqq.setof.adapter.in.rest.admin.discount;

import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountFromExcelV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountTargetV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.DiscountDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request.DiscountPolicySearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse.DiscountDetailsResponse;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyPageResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountTargetResult;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DiscountPolicy API 테스트 Fixtures.
 *
 * <p>할인 정책 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class DiscountPolicyApiFixtures {

    private DiscountPolicyApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_DISCOUNT_POLICY_ID = 1L;
    public static final String DEFAULT_POLICY_NAME = "신규 회원 할인";
    public static final String DEFAULT_MEMO = "신규 회원 대상 10% 할인";
    public static final String DEFAULT_DISCOUNT_TYPE_RATE = "RATE";
    public static final String DEFAULT_DISCOUNT_TYPE_PRICE = "PRICE";
    public static final String DEFAULT_PUBLISHER_TYPE_ADMIN = "ADMIN";
    public static final String DEFAULT_PUBLISHER_TYPE_SELLER = "SELLER";
    public static final String DEFAULT_ISSUE_TYPE_PRODUCT = "PRODUCT";
    public static final String DEFAULT_ISSUE_TYPE_SELLER = "SELLER";
    public static final String DEFAULT_ISSUE_TYPE_BRAND = "BRAND";
    public static final double DEFAULT_DISCOUNT_RATIO = 10.0;
    public static final long DEFAULT_MAX_DISCOUNT_PRICE = 10000L;
    public static final int DEFAULT_PRIORITY = 1;
    public static final LocalDateTime DEFAULT_START_DATE = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
    public static final LocalDateTime DEFAULT_END_DATE = LocalDateTime.of(2026, 12, 31, 23, 59, 59);
    public static final Instant DEFAULT_CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");
    public static final Instant DEFAULT_UPDATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    // ===== DiscountDetailsV1ApiRequest Fixtures =====

    public static DiscountDetailsV1ApiRequest discountDetails() {
        return discountDetailsRate();
    }

    public static DiscountDetailsV1ApiRequest discountDetailsRate() {
        return new DiscountDetailsV1ApiRequest(
                DEFAULT_POLICY_NAME,
                DEFAULT_DISCOUNT_TYPE_RATE,
                DEFAULT_PUBLISHER_TYPE_ADMIN,
                DEFAULT_ISSUE_TYPE_PRODUCT,
                "Y",
                DEFAULT_MAX_DISCOUNT_PRICE,
                "N",
                0.0,
                DEFAULT_DISCOUNT_RATIO,
                DEFAULT_START_DATE,
                DEFAULT_END_DATE,
                DEFAULT_MEMO,
                DEFAULT_PRIORITY,
                "Y");
    }

    public static DiscountDetailsV1ApiRequest discountDetailsPrice() {
        return new DiscountDetailsV1ApiRequest(
                "정액 할인 정책",
                DEFAULT_DISCOUNT_TYPE_PRICE,
                DEFAULT_PUBLISHER_TYPE_ADMIN,
                DEFAULT_ISSUE_TYPE_PRODUCT,
                "Y",
                DEFAULT_MAX_DISCOUNT_PRICE,
                "N",
                0.0,
                50.0,
                DEFAULT_START_DATE,
                DEFAULT_END_DATE,
                "정액 할인",
                DEFAULT_PRIORITY,
                "Y");
    }

    public static DiscountDetailsV1ApiRequest discountDetailsSeller() {
        return new DiscountDetailsV1ApiRequest(
                "판매자 할인",
                DEFAULT_DISCOUNT_TYPE_RATE,
                DEFAULT_PUBLISHER_TYPE_SELLER,
                DEFAULT_ISSUE_TYPE_SELLER,
                "N",
                0L,
                "N",
                0.0,
                5.0,
                DEFAULT_START_DATE,
                DEFAULT_END_DATE,
                "판매자 5% 할인",
                2,
                "Y");
    }

    // ===== Create Request Fixtures =====

    public static CreateDiscountV1ApiRequest createRequest() {
        return new CreateDiscountV1ApiRequest(null, discountDetailsRate());
    }

    public static CreateDiscountV1ApiRequest createRequestWithCopy(Long copyFromId) {
        return new CreateDiscountV1ApiRequest(copyFromId, discountDetailsRate());
    }

    public static CreateDiscountV1ApiRequest createRequestPrice() {
        return new CreateDiscountV1ApiRequest(null, discountDetailsPrice());
    }

    // ===== Update Request Fixtures =====

    public static UpdateDiscountV1ApiRequest updateRequest() {
        return new UpdateDiscountV1ApiRequest(discountDetailsRate(), List.of(101L, 102L));
    }

    public static UpdateDiscountV1ApiRequest updateRequestWithoutTargets() {
        return new UpdateDiscountV1ApiRequest(discountDetailsRate(), null);
    }

    // ===== Status Update Request Fixtures =====

    public static UpdateDiscountStatusV1ApiRequest updateStatusRequest() {
        return new UpdateDiscountStatusV1ApiRequest(List.of(1L, 2L, 3L), "Y");
    }

    public static UpdateDiscountStatusV1ApiRequest updateStatusRequestDeactivate() {
        return new UpdateDiscountStatusV1ApiRequest(List.of(4L, 5L), "N");
    }

    public static UpdateDiscountStatusV1ApiRequest updateStatusRequestSingle(
            Long id, String activeYn) {
        return new UpdateDiscountStatusV1ApiRequest(List.of(id), activeYn);
    }

    // ===== CreateDiscountTarget Request Fixtures =====

    public static CreateDiscountTargetV1ApiRequest createTargetRequest() {
        return new CreateDiscountTargetV1ApiRequest(
                DEFAULT_ISSUE_TYPE_PRODUCT, List.of(101L, 102L, 103L));
    }

    public static CreateDiscountTargetV1ApiRequest createTargetRequestSeller() {
        return new CreateDiscountTargetV1ApiRequest(DEFAULT_ISSUE_TYPE_SELLER, List.of(201L));
    }

    public static CreateDiscountTargetV1ApiRequest createTargetRequestBrand() {
        return new CreateDiscountTargetV1ApiRequest(DEFAULT_ISSUE_TYPE_BRAND, List.of(301L, 302L));
    }

    // ===== Excel Request Fixtures =====

    public static CreateDiscountFromExcelV1ApiRequest excelRequest() {
        return new CreateDiscountFromExcelV1ApiRequest(discountDetailsRate(), List.of(101L, 102L));
    }

    public static List<CreateDiscountFromExcelV1ApiRequest> excelRequests() {
        return List.of(
                new CreateDiscountFromExcelV1ApiRequest(discountDetailsRate(), List.of(101L)),
                new CreateDiscountFromExcelV1ApiRequest(
                        discountDetailsPrice(), List.of(201L, 202L)));
    }

    // ===== Search Request Fixtures =====

    public static DiscountPolicySearchV1ApiRequest searchRequest() {
        return new DiscountPolicySearchV1ApiRequest(
                null, null, null, null, null, null, null, null, null, null, 0, 20, "id", "DESC");
    }

    public static DiscountPolicySearchV1ApiRequest searchRequestWithActiveFilter(String activeYn) {
        return new DiscountPolicySearchV1ApiRequest(
                null, null, null, null, activeYn, null, null, null, null, null, 0, 20, "id",
                "DESC");
    }

    public static DiscountPolicySearchV1ApiRequest searchRequestWithPublisherType(
            String publisherType) {
        return new DiscountPolicySearchV1ApiRequest(
                null,
                null,
                null,
                null,
                null,
                publisherType,
                null,
                null,
                null,
                null,
                0,
                20,
                "id",
                "DESC");
    }

    public static DiscountPolicySearchV1ApiRequest searchRequestWithNullPage() {
        return new DiscountPolicySearchV1ApiRequest(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    // ===== Application Result Fixtures =====

    public static DiscountTargetResult discountTargetResult(Long id) {
        return new DiscountTargetResult(id, "PRODUCT_GROUP", 101L);
    }

    public static DiscountPolicyResult discountPolicyResult() {
        return discountPolicyResult(DEFAULT_DISCOUNT_POLICY_ID);
    }

    public static DiscountPolicyResult discountPolicyResult(Long id) {
        return new DiscountPolicyResult(
                id,
                DEFAULT_POLICY_NAME,
                DEFAULT_MEMO,
                "RATE",
                10.0,
                null,
                10000,
                true,
                null,
                "IMMEDIATE",
                "ADMIN",
                null,
                "PLATFORM_INSTANT",
                DEFAULT_PRIORITY,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                0,
                0,
                true,
                List.of(discountTargetResult(10L)),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static DiscountPolicyResult discountPolicyResultFixedAmount(Long id) {
        return new DiscountPolicyResult(
                id,
                "정액 할인 정책",
                "5000원 할인",
                "FIXED_AMOUNT",
                null,
                5000,
                10000,
                true,
                null,
                "IMMEDIATE",
                "ADMIN",
                null,
                "PLATFORM_INSTANT",
                DEFAULT_PRIORITY,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                0,
                0,
                true,
                List.of(discountTargetResult(20L)),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static DiscountPolicyResult discountPolicyResultEmptyTargets(Long id) {
        return new DiscountPolicyResult(
                id,
                DEFAULT_POLICY_NAME,
                DEFAULT_MEMO,
                "RATE",
                10.0,
                null,
                10000,
                true,
                null,
                "IMMEDIATE",
                "ADMIN",
                null,
                "PLATFORM_INSTANT",
                DEFAULT_PRIORITY,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                0,
                0,
                true,
                List.of(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static DiscountPolicyPageResult discountPolicyPageResult() {
        return DiscountPolicyPageResult.of(
                List.of(discountPolicyResult(1L), discountPolicyResult(2L)), 2L, 0, 20);
    }

    public static DiscountPolicyPageResult emptyDiscountPolicyPageResult() {
        return DiscountPolicyPageResult.of(List.of(), 0L, 0, 20);
    }

    // ===== API Response Fixtures =====

    public static DiscountPolicyV1ApiResponse discountPolicyApiResponse() {
        return discountPolicyApiResponse(DEFAULT_DISCOUNT_POLICY_ID);
    }

    public static DiscountPolicyV1ApiResponse discountPolicyApiResponse(Long id) {
        DiscountDetailsResponse details =
                DiscountDetailsResponse.of(
                        DEFAULT_POLICY_NAME,
                        DEFAULT_DISCOUNT_TYPE_RATE,
                        DEFAULT_PUBLISHER_TYPE_ADMIN,
                        DEFAULT_ISSUE_TYPE_PRODUCT,
                        "Y",
                        DEFAULT_MAX_DISCOUNT_PRICE,
                        "N",
                        0.0,
                        DEFAULT_DISCOUNT_RATIO,
                        DEFAULT_START_DATE,
                        DEFAULT_END_DATE,
                        DEFAULT_MEMO,
                        DEFAULT_PRIORITY,
                        "Y");
        return DiscountPolicyV1ApiResponse.of(
                id, details, DEFAULT_START_DATE, DEFAULT_END_DATE, "system", "system");
    }

    public static DiscountPolicyV1ApiResponse discountPolicyApiResponseFixedAmount(Long id) {
        DiscountDetailsResponse details =
                DiscountDetailsResponse.of(
                        "정액 할인 정책",
                        DEFAULT_DISCOUNT_TYPE_PRICE,
                        DEFAULT_PUBLISHER_TYPE_ADMIN,
                        DEFAULT_ISSUE_TYPE_PRODUCT,
                        "Y",
                        DEFAULT_MAX_DISCOUNT_PRICE,
                        "N",
                        0.0,
                        5000.0,
                        DEFAULT_START_DATE,
                        DEFAULT_END_DATE,
                        "5000원 할인",
                        DEFAULT_PRIORITY,
                        "Y");
        return DiscountPolicyV1ApiResponse.of(
                id, details, DEFAULT_START_DATE, DEFAULT_END_DATE, "system", "system");
    }
}
