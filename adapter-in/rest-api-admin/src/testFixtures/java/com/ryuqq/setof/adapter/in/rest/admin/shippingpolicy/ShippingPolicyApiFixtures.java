package com.ryuqq.setof.adapter.in.rest.admin.shippingpolicy;

import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.ChangeShippingPolicyStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyApiRequest.LeadTimeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.query.SearchShippingPoliciesPageApiRequest;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyPageResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import com.ryuqq.setof.domain.common.vo.PageMeta;
import java.time.Instant;
import java.util.List;

/**
 * ShippingPolicy API 테스트 Fixtures.
 *
 * <p>배송정책 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class ShippingPolicyApiFixtures {

    private ShippingPolicyApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_SELLER_ID = 1L;
    public static final Long DEFAULT_POLICY_ID = 200L;
    public static final String DEFAULT_POLICY_NAME = "기본 배송정책";
    public static final Long DEFAULT_BASE_FEE = 3000L;
    public static final Long DEFAULT_FREE_THRESHOLD = 50000L;

    // ===== Register Request Fixtures =====
    public static RegisterShippingPolicyApiRequest registerRequest() {
        return new RegisterShippingPolicyApiRequest(
                DEFAULT_POLICY_NAME,
                true,
                "CONDITIONAL_FREE",
                DEFAULT_BASE_FEE,
                DEFAULT_FREE_THRESHOLD,
                3000L,
                5000L,
                3000L,
                6000L,
                new LeadTimeApiRequest(1, 3, "14:00"));
    }

    public static RegisterShippingPolicyApiRequest registerRequest(String policyName) {
        return new RegisterShippingPolicyApiRequest(
                policyName,
                false,
                "PAID",
                3000L,
                0L,
                3000L,
                5000L,
                3000L,
                6000L,
                new LeadTimeApiRequest(1, 3, null));
    }

    public static RegisterShippingPolicyApiRequest invalidRegisterRequest() {
        return new RegisterShippingPolicyApiRequest(
                "", // 빈 정책명 - 유효성 검증 실패
                null, null, -1L, // 음수 - 유효성 검증 실패
                null, null, null, null, null, null);
    }

    // ===== Update Request Fixtures =====
    public static UpdateShippingPolicyApiRequest updateRequest() {
        return new UpdateShippingPolicyApiRequest(
                "수정된 배송정책",
                false,
                "FREE",
                0L,
                0L,
                2500L,
                4000L,
                2500L,
                5000L,
                new LeadTimeApiRequest(1, 2, "15:00"));
    }

    public static UpdateShippingPolicyApiRequest updateRequest(String policyName) {
        return new UpdateShippingPolicyApiRequest(
                policyName, false, "PAID", 3000L, 0L, 3000L, 5000L, 3000L, 6000L, null);
    }

    // ===== Change Status Request Fixtures =====
    public static ChangeShippingPolicyStatusApiRequest changeStatusRequest(
            List<Long> policyIds, boolean active) {
        return new ChangeShippingPolicyStatusApiRequest(policyIds, active);
    }

    public static ChangeShippingPolicyStatusApiRequest activateRequest(List<Long> policyIds) {
        return new ChangeShippingPolicyStatusApiRequest(policyIds, true);
    }

    public static ChangeShippingPolicyStatusApiRequest deactivateRequest(List<Long> policyIds) {
        return new ChangeShippingPolicyStatusApiRequest(policyIds, false);
    }

    // ===== Search Request Fixtures =====
    public static SearchShippingPoliciesPageApiRequest searchRequest() {
        return new SearchShippingPoliciesPageApiRequest("CREATED_AT", "DESC", 0, 20);
    }

    public static SearchShippingPoliciesPageApiRequest searchRequest(int page, int size) {
        return new SearchShippingPoliciesPageApiRequest("CREATED_AT", "DESC", page, size);
    }

    // ===== Result Fixtures (Application Layer 응답) =====
    public static ShippingPolicyResult policyResult() {
        return policyResult(DEFAULT_POLICY_ID, DEFAULT_POLICY_NAME, true);
    }

    public static ShippingPolicyResult policyResult(
            Long policyId, String policyName, boolean active) {
        return new ShippingPolicyResult(
                policyId,
                policyName,
                true,
                active,
                "CONDITIONAL_FREE",
                "조건부 무료배송",
                DEFAULT_BASE_FEE,
                DEFAULT_FREE_THRESHOLD,
                Instant.parse("2025-01-26T10:30:00Z"));
    }

    public static ShippingPolicyPageResult pageResult() {
        return pageResult(List.of(policyResult()), 0, 20, 1);
    }

    public static ShippingPolicyPageResult pageResult(
            List<ShippingPolicyResult> results, int page, int size, long totalElements) {
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return new ShippingPolicyPageResult(results, pageMeta);
    }

    public static ShippingPolicyPageResult emptyPageResult() {
        PageMeta pageMeta = PageMeta.of(0, 20, 0);
        return new ShippingPolicyPageResult(List.of(), pageMeta);
    }

    public static List<ShippingPolicyResult> multipleResults() {
        return List.of(
                policyResult(1L, "기본 배송정책", true),
                policyResult(2L, "프리미엄 배송정책", true),
                policyResult(3L, "무료 배송정책", false));
    }
}
