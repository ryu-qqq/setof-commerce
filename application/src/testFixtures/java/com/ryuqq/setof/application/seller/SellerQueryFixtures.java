package com.ryuqq.setof.application.seller;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Seller Application Query 테스트 Fixtures.
 *
 * <p>Seller 조회 관련 Query 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class SellerQueryFixtures {

    private SellerQueryFixtures() {}

    // ===== SellerSearchParams =====

    public static SellerSearchParams searchParams() {
        return SellerSearchParams.of(null, null, null, defaultCommonSearchParams());
    }

    public static SellerSearchParams searchParams(Boolean active) {
        return SellerSearchParams.of(active, null, null, defaultCommonSearchParams());
    }

    public static SellerSearchParams searchParams(String searchField, String searchWord) {
        return SellerSearchParams.of(null, searchField, searchWord, defaultCommonSearchParams());
    }

    public static SellerSearchParams searchParams(int page, int size) {
        return SellerSearchParams.of(null, null, null, commonSearchParams(page, size));
    }

    // ===== CommonSearchParams =====

    public static CommonSearchParams defaultCommonSearchParams() {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", 0, 20);
    }

    public static CommonSearchParams commonSearchParams(int page, int size) {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", page, size);
    }

    // ===== SellerResult =====

    public static SellerResult sellerResult(Long id) {
        Instant now = Instant.parse("2024-01-01T00:00:00Z");
        return new SellerResult(
                id, "테스트셀러", "테스트스토어", "http://example.com/logo.png", "테스트 설명", true, now, now);
    }

    public static List<SellerResult> sellerResults() {
        return List.of(sellerResult(1L), sellerResult(2L));
    }

    // ===== SellerPageResult =====

    public static SellerPageResult sellerPageResult() {
        return SellerPageResult.of(sellerResults(), 0, 20, 2L);
    }

    public static SellerPageResult emptySellerPageResult() {
        return SellerPageResult.of(Collections.emptyList(), 0, 20, 0L);
    }

    // ===== SellerCompositeResult =====

    public static SellerCompositeResult sellerCompositeResult(Long sellerId) {
        Instant now = Instant.parse("2024-01-01T00:00:00Z");

        SellerCompositeResult.SellerInfo sellerInfo =
                new SellerCompositeResult.SellerInfo(
                        sellerId,
                        "테스트 셀러",
                        "테스트 스토어",
                        "http://example.com/logo.png",
                        "테스트 설명",
                        true,
                        now,
                        now);

        SellerCompositeResult.BusinessInfo businessInfo =
                new SellerCompositeResult.BusinessInfo(
                        1L,
                        "123-45-67890",
                        "테스트 주식회사",
                        "홍길동",
                        "2024-서울-0001",
                        "12345",
                        "서울시 강남구 테헤란로 1",
                        "101호");

        SellerCompositeResult.CsInfo csInfo =
                new SellerCompositeResult.CsInfo(
                        1L,
                        "02-1234-5678",
                        "010-1234-5678",
                        "cs@test.com",
                        Instant.parse("1970-01-01T00:00:00Z"),
                        Instant.parse("1970-01-01T09:00:00Z"),
                        "MON,TUE,WED,THU,FRI",
                        null);

        return new SellerCompositeResult(sellerInfo, null, businessInfo, csInfo);
    }

    public static SellerCompositeResult minimalSellerCompositeResult(Long sellerId) {
        Instant now = Instant.parse("2024-01-01T00:00:00Z");

        SellerCompositeResult.SellerInfo sellerInfo =
                new SellerCompositeResult.SellerInfo(
                        sellerId,
                        "테스트 셀러",
                        "테스트 스토어",
                        "http://example.com/logo.png",
                        "테스트 설명",
                        true,
                        now,
                        now);

        return new SellerCompositeResult(sellerInfo, null, null, null);
    }

    // ===== SellerPolicyCompositeResult =====

    public static SellerPolicyCompositeResult sellerPolicyCompositeResult(Long sellerId) {
        return new SellerPolicyCompositeResult(
                sellerId, Collections.emptyList(), Collections.emptyList());
    }
}
