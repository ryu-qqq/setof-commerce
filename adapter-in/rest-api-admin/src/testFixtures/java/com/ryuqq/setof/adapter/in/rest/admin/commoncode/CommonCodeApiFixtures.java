package com.ryuqq.setof.adapter.in.rest.admin.commoncode;

import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.ChangeActiveStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.RegisterCommonCodeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.UpdateCommonCodeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.query.SearchCommonCodesPageApiRequest;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodePageResult;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodeResult;
import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.time.Instant;
import java.util.List;

/**
 * CommonCode API 테스트 Fixtures.
 *
 * <p>공통코드 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CommonCodeApiFixtures {

    private CommonCodeApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_CODE_TYPE_ID = 1L;
    public static final Long DEFAULT_CODE_ID = 100L;
    public static final String DEFAULT_CODE = "CARD";
    public static final String DEFAULT_DISPLAY_NAME = "신용카드";
    public static final int DEFAULT_DISPLAY_ORDER = 1;

    // ===== Register Request Fixtures =====
    public static RegisterCommonCodeApiRequest registerRequest() {
        return new RegisterCommonCodeApiRequest(
                DEFAULT_CODE_TYPE_ID, DEFAULT_CODE, DEFAULT_DISPLAY_NAME, DEFAULT_DISPLAY_ORDER);
    }

    public static RegisterCommonCodeApiRequest registerRequest(
            Long commonCodeTypeId, String code, String displayName) {
        return new RegisterCommonCodeApiRequest(commonCodeTypeId, code, displayName, 1);
    }

    public static RegisterCommonCodeApiRequest invalidRegisterRequest() {
        return new RegisterCommonCodeApiRequest(
                null, // null - 유효성 검증 실패
                "", // 빈 코드 - 유효성 검증 실패
                "", // 빈 표시명 - 유효성 검증 실패
                -1); // 음수 - 유효성 검증 실패
    }

    // ===== Update Request Fixtures =====
    public static UpdateCommonCodeApiRequest updateRequest() {
        return new UpdateCommonCodeApiRequest("현금", 2);
    }

    public static UpdateCommonCodeApiRequest updateRequest(String displayName, int displayOrder) {
        return new UpdateCommonCodeApiRequest(displayName, displayOrder);
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
    public static SearchCommonCodesPageApiRequest searchRequest() {
        return new SearchCommonCodesPageApiRequest(
                DEFAULT_CODE_TYPE_ID, null, null, "CREATED_AT", "DESC", 0, 20);
    }

    public static SearchCommonCodesPageApiRequest searchRequest(Long commonCodeTypeId) {
        return new SearchCommonCodesPageApiRequest(
                commonCodeTypeId, null, null, "CREATED_AT", "DESC", 0, 20);
    }

    public static SearchCommonCodesPageApiRequest searchRequest(
            Long commonCodeTypeId, int page, int size) {
        return new SearchCommonCodesPageApiRequest(
                commonCodeTypeId, null, null, "CREATED_AT", "DESC", page, size);
    }

    // ===== Result Fixtures (Application Layer 응답) =====
    public static CommonCodeResult codeResult() {
        return codeResult(DEFAULT_CODE_ID, DEFAULT_CODE, DEFAULT_DISPLAY_NAME, true);
    }

    public static CommonCodeResult codeResult(
            Long id, String code, String displayName, boolean active) {
        return new CommonCodeResult(
                id,
                DEFAULT_CODE_TYPE_ID,
                code,
                displayName,
                1,
                active,
                Instant.parse("2025-01-26T10:30:00Z"),
                Instant.parse("2025-01-26T10:30:00Z"));
    }

    public static CommonCodePageResult pageResult() {
        return pageResult(List.of(codeResult()), 0, 20, 1);
    }

    public static CommonCodePageResult pageResult(
            List<CommonCodeResult> results, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new CommonCodePageResult(results, pageMeta);
    }

    public static CommonCodePageResult emptyPageResult() {
        PageMeta pageMeta = PageMeta.of(0, 20, 0);
        return new CommonCodePageResult(List.of(), pageMeta);
    }

    public static List<CommonCodeResult> multipleResults() {
        return List.of(
                codeResult(1L, "CARD", "신용카드", true),
                codeResult(2L, "CASH", "현금", true),
                codeResult(3L, "TRANSFER", "계좌이체", false));
    }
}
