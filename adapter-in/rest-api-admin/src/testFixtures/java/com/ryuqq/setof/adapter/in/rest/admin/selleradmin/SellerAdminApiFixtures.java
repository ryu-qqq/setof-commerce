package com.ryuqq.setof.adapter.in.rest.admin.selleradmin;

import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.ApplySellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.BulkApproveSellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.BulkRejectSellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.query.SearchSellerAdminApplicationsApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.response.ApplySellerAdminApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.response.ApproveSellerAdminApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.response.BulkApproveSellerAdminApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.response.SellerAdminApplicationApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.response.SellerAdminApplicationPageApiResponse;
import com.ryuqq.setof.application.common.dto.result.BatchItemResult;
import com.ryuqq.setof.application.common.dto.result.BatchProcessingResult;
import com.ryuqq.setof.application.selleradmin.dto.response.SellerAdminApplicationPageResult;
import com.ryuqq.setof.application.selleradmin.dto.response.SellerAdminApplicationResult;
import java.time.Instant;
import java.util.List;

/**
 * SellerAdminApiFixtures - 셀러 관리자 가입 신청 API 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class SellerAdminApiFixtures {

    private SellerAdminApiFixtures() {}

    private static final String SELLER_ADMIN_ID = "01956f4a-2b3c-7d8e-9f0a-1b2c3d4e5f67";
    private static final String SELLER_ADMIN_ID_2 = "01956f4a-3c4d-8e9f-0a1b-2c3d4e5f6789";
    private static final Long SELLER_ID = 1L;

    // ===== Command Request Fixtures =====

    public static ApplySellerAdminApiRequest applyRequest() {
        return new ApplySellerAdminApiRequest(
                SELLER_ID, "admin@example.com", "홍길동", "010-1234-5678", "Password123!");
    }

    public static BulkApproveSellerAdminApiRequest bulkApproveRequest() {
        return new BulkApproveSellerAdminApiRequest(List.of(SELLER_ADMIN_ID, SELLER_ADMIN_ID_2));
    }

    public static BulkRejectSellerAdminApiRequest bulkRejectRequest() {
        return new BulkRejectSellerAdminApiRequest(List.of(SELLER_ADMIN_ID, SELLER_ADMIN_ID_2));
    }

    // ===== Query Request Fixtures =====

    public static SearchSellerAdminApplicationsApiRequest searchRequest() {
        return new SearchSellerAdminApplicationsApiRequest(
                null, List.of("PENDING_APPROVAL"), null, null, null, null, null, null, 0, 20);
    }

    public static SearchSellerAdminApplicationsApiRequest searchRequestWithSellerIds() {
        return new SearchSellerAdminApplicationsApiRequest(
                List.of(1L, 2L),
                List.of("PENDING_APPROVAL"),
                "loginId",
                "admin",
                "createdAt",
                "DESC",
                "2025-01-01",
                "2025-12-31",
                0,
                20);
    }

    // ===== Domain Result Fixtures =====

    public static SellerAdminApplicationResult applicationResult(String sellerAdminId) {
        return new SellerAdminApplicationResult(
                sellerAdminId,
                SELLER_ID,
                "admin@example.com",
                "홍길동",
                "010-1234-5678",
                "PENDING_APPROVAL",
                null,
                Instant.parse("2026-02-04T10:00:00Z"),
                Instant.parse("2026-02-04T10:00:00Z"));
    }

    public static SellerAdminApplicationResult applicationResult() {
        return applicationResult(SELLER_ADMIN_ID);
    }

    public static SellerAdminApplicationResult applicationResultActive(String sellerAdminId) {
        return new SellerAdminApplicationResult(
                sellerAdminId,
                SELLER_ID,
                "admin@example.com",
                "홍길동",
                "010-1234-5678",
                "ACTIVE",
                "auth-user-uuid-123",
                Instant.parse("2026-02-04T10:00:00Z"),
                Instant.parse("2026-02-04T11:00:00Z"));
    }

    public static SellerAdminApplicationPageResult applicationPageResult() {
        return SellerAdminApplicationPageResult.of(
                List.of(applicationResult(SELLER_ADMIN_ID), applicationResult(SELLER_ADMIN_ID_2)),
                0,
                20,
                2L);
    }

    // ===== API Response Fixtures =====

    public static SellerAdminApplicationApiResponse sellerAdminApplicationApiResponse() {
        return sellerAdminApplicationApiResponse(SELLER_ADMIN_ID);
    }

    public static SellerAdminApplicationApiResponse sellerAdminApplicationApiResponse(
            String sellerAdminId) {
        return new SellerAdminApplicationApiResponse(
                sellerAdminId,
                SELLER_ID,
                "admin@example.com",
                "홍길동",
                "010-1234-5678",
                "PENDING_APPROVAL",
                null,
                Instant.parse("2026-02-04T10:00:00Z"),
                Instant.parse("2026-02-04T10:00:00Z"));
    }

    public static SellerAdminApplicationPageApiResponse sellerAdminApplicationPageApiResponse() {
        return new SellerAdminApplicationPageApiResponse(
                List.of(
                        sellerAdminApplicationApiResponse(SELLER_ADMIN_ID),
                        sellerAdminApplicationApiResponse(SELLER_ADMIN_ID_2)),
                0,
                20,
                2L,
                1);
    }

    public static ApplySellerAdminApiResponse applyResponse() {
        return new ApplySellerAdminApiResponse(SELLER_ADMIN_ID);
    }

    public static ApproveSellerAdminApiResponse approveResponse() {
        return ApproveSellerAdminApiResponse.from(SELLER_ADMIN_ID);
    }

    public static BulkApproveSellerAdminApiResponse bulkApproveResponse() {
        return new BulkApproveSellerAdminApiResponse(
                2,
                2,
                0,
                List.of(
                        new BulkApproveSellerAdminApiResponse.ItemResult(
                                SELLER_ADMIN_ID, true, null, null),
                        new BulkApproveSellerAdminApiResponse.ItemResult(
                                SELLER_ADMIN_ID_2, true, null, null)));
    }

    public static BatchProcessingResult<String> batchProcessingResult() {
        return BatchProcessingResult.from(
                List.of(
                        BatchItemResult.success(SELLER_ADMIN_ID),
                        BatchItemResult.success(SELLER_ADMIN_ID_2)));
    }

    public static String sellerAdminId() {
        return SELLER_ADMIN_ID;
    }

    public static Long sellerId() {
        return SELLER_ID;
    }
}
