package com.ryuqq.setof.adapter.in.rest.admin.refundpolicy;

import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.ChangeRefundPolicyStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.query.SearchRefundPoliciesPageApiRequest;
import com.ryuqq.setof.application.refundpolicy.dto.response.NonReturnableConditionResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyPageResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.time.Instant;
import java.util.List;

/**
 * RefundPolicy API 테스트 Fixtures.
 *
 * <p>환불정책 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class RefundPolicyApiFixtures {

    private RefundPolicyApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final Long DEFAULT_POLICY_ID = 100L;
    public static final String DEFAULT_POLICY_NAME = "기본 환불정책";
    public static final int DEFAULT_RETURN_PERIOD_DAYS = 7;
    public static final int DEFAULT_EXCHANGE_PERIOD_DAYS = 14;
    public static final int DEFAULT_INSPECTION_PERIOD_DAYS = 3;

    // ===== Register Request Fixtures =====
    public static RegisterRefundPolicyApiRequest registerRequest() {
        return new RegisterRefundPolicyApiRequest(
                DEFAULT_POLICY_NAME,
                true,
                DEFAULT_RETURN_PERIOD_DAYS,
                DEFAULT_EXCHANGE_PERIOD_DAYS,
                List.of("OPENED_PACKAGING", "USED_PRODUCT"),
                true,
                true,
                DEFAULT_INSPECTION_PERIOD_DAYS,
                "교환/반품 시 상품 택이 제거되지 않은 상태여야 합니다.");
    }

    public static RegisterRefundPolicyApiRequest registerRequest(String policyName) {
        return new RegisterRefundPolicyApiRequest(
                policyName,
                false,
                DEFAULT_RETURN_PERIOD_DAYS,
                DEFAULT_EXCHANGE_PERIOD_DAYS,
                List.of("OPENED_PACKAGING"),
                true,
                false,
                0,
                null);
    }

    public static RegisterRefundPolicyApiRequest invalidRegisterRequest() {
        return new RegisterRefundPolicyApiRequest(
                "", // 빈 정책명 - 유효성 검증 실패
                null, 0, // 1 미만 - 유효성 검증 실패
                100, // 90 초과 - 유효성 검증 실패
                null, null, null, null, null);
    }

    // ===== Update Request Fixtures =====
    public static UpdateRefundPolicyApiRequest updateRequest() {
        return new UpdateRefundPolicyApiRequest(
                "수정된 환불정책",
                false,
                14,
                21,
                List.of("OPENED_PACKAGING", "USED_PRODUCT", "MISSING_TAG"),
                true,
                true,
                5,
                "수정된 안내 문구입니다.");
    }

    public static UpdateRefundPolicyApiRequest updateRequest(String policyName) {
        return new UpdateRefundPolicyApiRequest(
                policyName, false, 7, 7, List.of(), false, false, 0, null);
    }

    // ===== Change Status Request Fixtures =====
    public static ChangeRefundPolicyStatusApiRequest changeStatusRequest(
            List<Long> policyIds, boolean active) {
        return new ChangeRefundPolicyStatusApiRequest(policyIds, active);
    }

    public static ChangeRefundPolicyStatusApiRequest activateRequest(List<Long> policyIds) {
        return new ChangeRefundPolicyStatusApiRequest(policyIds, true);
    }

    public static ChangeRefundPolicyStatusApiRequest deactivateRequest(List<Long> policyIds) {
        return new ChangeRefundPolicyStatusApiRequest(policyIds, false);
    }

    // ===== Search Request Fixtures =====
    public static SearchRefundPoliciesPageApiRequest searchRequest() {
        return new SearchRefundPoliciesPageApiRequest("CREATED_AT", "DESC", 0, 20);
    }

    public static SearchRefundPoliciesPageApiRequest searchRequest(int page, int size) {
        return new SearchRefundPoliciesPageApiRequest("CREATED_AT", "DESC", page, size);
    }

    // ===== Result Fixtures (Application Layer 응답) =====
    public static RefundPolicyResult policyResult() {
        return policyResult(DEFAULT_POLICY_ID, DEFAULT_POLICY_NAME, true);
    }

    public static RefundPolicyResult policyResult(
            Long policyId, String policyName, boolean active) {
        return new RefundPolicyResult(
                policyId,
                policyName,
                true,
                active,
                DEFAULT_RETURN_PERIOD_DAYS,
                DEFAULT_EXCHANGE_PERIOD_DAYS,
                List.of(
                        new NonReturnableConditionResult("OPENED_PACKAGING", "포장 개봉"),
                        new NonReturnableConditionResult("USED_PRODUCT", "사용 흔적")),
                Instant.parse("2025-01-26T10:30:00Z"));
    }

    public static RefundPolicyPageResult pageResult() {
        return pageResult(List.of(policyResult()), 0, 20, 1);
    }

    public static RefundPolicyPageResult pageResult(
            List<RefundPolicyResult> results, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new RefundPolicyPageResult(results, pageMeta);
    }

    public static RefundPolicyPageResult emptyPageResult() {
        PageMeta pageMeta = PageMeta.of(0, 20, 0);
        return new RefundPolicyPageResult(List.of(), pageMeta);
    }

    public static List<RefundPolicyResult> multipleResults() {
        return List.of(
                policyResult(1L, "기본 환불정책", true),
                policyResult(2L, "프리미엄 환불정책", true),
                policyResult(3L, "간편 환불정책", false));
    }
}
