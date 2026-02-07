package com.ryuqq.setof.adapter.in.rest.admin.commoncodetype;

import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.ChangeActiveStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.RegisterCommonCodeTypeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.UpdateCommonCodeTypeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.query.SearchCommonCodeTypesPageApiRequest;
import com.ryuqq.setof.application.commoncodetype.dto.response.CommonCodeTypePageResult;
import com.ryuqq.setof.application.commoncodetype.dto.response.CommonCodeTypeResult;
import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.time.Instant;
import java.util.List;

/**
 * CommonCodeType API 테스트 Fixtures.
 *
 * <p>공통코드 타입 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CommonCodeTypeApiFixtures {

    private CommonCodeTypeApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_TYPE_ID = 1L;
    public static final String DEFAULT_CODE = "PAYMENT_METHOD";
    public static final String DEFAULT_NAME = "결제수단";
    public static final String DEFAULT_DESCRIPTION = "결제 시 사용 가능한 결제수단 목록";
    public static final int DEFAULT_DISPLAY_ORDER = 1;

    // ===== Register Request Fixtures =====
    public static RegisterCommonCodeTypeApiRequest registerRequest() {
        return new RegisterCommonCodeTypeApiRequest(
                DEFAULT_CODE, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DISPLAY_ORDER);
    }

    public static RegisterCommonCodeTypeApiRequest registerRequest(String code, String name) {
        return new RegisterCommonCodeTypeApiRequest(code, name, null, 1);
    }

    public static RegisterCommonCodeTypeApiRequest invalidRegisterRequest() {
        return new RegisterCommonCodeTypeApiRequest(
                "", // 빈 코드 - 유효성 검증 실패
                "", // 빈 이름 - 유효성 검증 실패
                null, -1); // 음수 - 유효성 검증 실패
    }

    // ===== Update Request Fixtures =====
    public static UpdateCommonCodeTypeApiRequest updateRequest() {
        return new UpdateCommonCodeTypeApiRequest("수정된 결제수단", "수정된 설명입니다.", 2);
    }

    public static UpdateCommonCodeTypeApiRequest updateRequest(String name, int displayOrder) {
        return new UpdateCommonCodeTypeApiRequest(name, null, displayOrder);
    }

    // ===== Change Status Request Fixtures =====
    public static ChangeActiveStatusApiRequest changeStatusRequest(List<Long> ids, boolean active) {
        return new ChangeActiveStatusApiRequest(ids, active);
    }

    public static ChangeActiveStatusApiRequest activateRequest(List<Long> ids) {
        return new ChangeActiveStatusApiRequest(ids, true);
    }

    public static ChangeActiveStatusApiRequest deactivateRequest(List<Long> ids) {
        return new ChangeActiveStatusApiRequest(ids, false);
    }

    // ===== Search Request Fixtures =====
    public static SearchCommonCodeTypesPageApiRequest searchRequest() {
        return new SearchCommonCodeTypesPageApiRequest(
                null, null, null, null, "CREATED_AT", "DESC", 0, 20);
    }

    public static SearchCommonCodeTypesPageApiRequest searchRequest(int page, int size) {
        return new SearchCommonCodeTypesPageApiRequest(
                null, null, null, null, "CREATED_AT", "DESC", page, size);
    }

    public static SearchCommonCodeTypesPageApiRequest searchRequestWithSearch(
            String searchField, String searchWord) {
        return new SearchCommonCodeTypesPageApiRequest(
                null, searchField, searchWord, null, "CREATED_AT", "DESC", 0, 20);
    }

    public static SearchCommonCodeTypesPageApiRequest searchRequestWithType(String type) {
        return new SearchCommonCodeTypesPageApiRequest(
                null, null, null, type, "CREATED_AT", "DESC", 0, 20);
    }

    // ===== Result Fixtures (Application Layer 응답) =====
    public static CommonCodeTypeResult typeResult() {
        return typeResult(DEFAULT_TYPE_ID, DEFAULT_CODE, DEFAULT_NAME, true);
    }

    public static CommonCodeTypeResult typeResult(
            Long id, String code, String name, boolean active) {
        return new CommonCodeTypeResult(
                id,
                code,
                name,
                DEFAULT_DESCRIPTION,
                DEFAULT_DISPLAY_ORDER,
                active,
                Instant.parse("2025-01-26T10:30:00Z"),
                Instant.parse("2025-01-26T10:30:00Z"));
    }

    public static CommonCodeTypePageResult pageResult() {
        return pageResult(List.of(typeResult()), 0, 20, 1);
    }

    public static CommonCodeTypePageResult pageResult(
            List<CommonCodeTypeResult> results, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new CommonCodeTypePageResult(results, pageMeta);
    }

    public static CommonCodeTypePageResult emptyPageResult() {
        PageMeta pageMeta = PageMeta.of(0, 20, 0);
        return new CommonCodeTypePageResult(List.of(), pageMeta);
    }

    public static List<CommonCodeTypeResult> multipleResults() {
        return List.of(
                typeResult(1L, "PAYMENT_METHOD", "결제수단", true),
                typeResult(2L, "DELIVERY_TYPE", "배송유형", true),
                typeResult(3L, "ORDER_STATUS", "주문상태", false));
    }
}
