package com.ryuqq.setof.application.productgroup;

import com.ryuqq.setof.application.productgroup.dto.composite.OptionGroupSummaryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupEnrichmentResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupExcelBaseBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.NonReturnableConditionResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * ProductGroup Application Composite Query 테스트 Fixtures.
 *
 * <p>ProductGroup 조회 관련 Composite Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupCompositeQueryFixtures {

    private ProductGroupCompositeQueryFixtures() {}

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    // ===== OptionGroupSummaryResult =====

    public static OptionGroupSummaryResult colorOptionGroup() {
        return new OptionGroupSummaryResult("색상", List.of("블랙", "화이트", "레드"));
    }

    public static OptionGroupSummaryResult sizeOptionGroup() {
        return new OptionGroupSummaryResult("사이즈", List.of("S", "M", "L", "XL"));
    }

    public static OptionGroupSummaryResult optionGroupWithNullValues() {
        return new OptionGroupSummaryResult("소재", null);
    }

    // ===== ProductGroupListCompositeResult (ofBase) =====

    public static ProductGroupListCompositeResult baseCompositeResult(Long productGroupId) {
        return ProductGroupListCompositeResult.ofBase(
                productGroupId,
                10L,
                "테스트셀러",
                20L,
                "테스트브랜드",
                30L,
                "의류",
                "1,2,30",
                3,
                "테스트 상품 그룹",
                "COMBINATION",
                "ACTIVE",
                "http://example.com/thumb.jpg",
                5,
                FIXED_NOW,
                FIXED_NOW);
    }

    public static ProductGroupListCompositeResult enrichedCompositeResult(Long productGroupId) {
        return baseCompositeResult(productGroupId)
                .withEnrichment(10000, 50000, 20, List.of(colorOptionGroup(), sizeOptionGroup()));
    }

    public static List<ProductGroupListCompositeResult> baseCompositeResults() {
        return List.of(baseCompositeResult(1L), baseCompositeResult(2L));
    }

    public static List<ProductGroupListCompositeResult> enrichedCompositeResults() {
        return List.of(enrichedCompositeResult(1L), enrichedCompositeResult(2L));
    }

    // ===== ProductGroupEnrichmentResult =====

    public static ProductGroupEnrichmentResult enrichmentResult(Long productGroupId) {
        return new ProductGroupEnrichmentResult(
                productGroupId, 10000, 50000, 20, List.of(colorOptionGroup(), sizeOptionGroup()));
    }

    public static ProductGroupEnrichmentResult enrichmentResultWithNullOptions(
            Long productGroupId) {
        return new ProductGroupEnrichmentResult(productGroupId, 5000, 30000, 10, null);
    }

    public static List<ProductGroupEnrichmentResult> enrichmentResults() {
        return List.of(enrichmentResult(1L), enrichmentResult(2L));
    }

    // ===== ProductGroupDetailCompositeQueryResult =====

    public static ProductGroupDetailCompositeQueryResult detailCompositeQueryResult(
            Long productGroupId) {
        return new ProductGroupDetailCompositeQueryResult(
                productGroupId,
                10L,
                "테스트셀러",
                20L,
                "테스트브랜드",
                "https://example.com/brand-icon.png",
                30L,
                "의류",
                "1,2,30",
                "테스트 상품 그룹",
                "COMBINATION",
                "ACTIVE",
                FIXED_NOW,
                FIXED_NOW,
                shippingPolicyResult(),
                refundPolicyResult());
    }

    public static ProductGroupDetailCompositeQueryResult detailCompositeQueryResultWithoutPolicies(
            Long productGroupId) {
        return new ProductGroupDetailCompositeQueryResult(
                productGroupId,
                10L,
                "테스트셀러",
                20L,
                "테스트브랜드",
                "https://example.com/brand-icon.png",
                30L,
                "의류",
                "1,2,30",
                "테스트 상품 그룹",
                "COMBINATION",
                "ACTIVE",
                FIXED_NOW,
                FIXED_NOW,
                null,
                null);
    }

    // ===== ProductGroupExcelBaseBundle =====

    public static ProductGroupExcelBaseBundle excelBaseBundle() {
        return new ProductGroupExcelBaseBundle(
                enrichedCompositeResults(),
                Map.of(
                        1L,
                        "http://cdn.example.com/desc/1.jpg",
                        2L,
                        "http://cdn.example.com/desc/2.jpg"),
                2L);
    }

    public static ProductGroupExcelBaseBundle emptyExcelBaseBundle() {
        return new ProductGroupExcelBaseBundle(null, null, 0L);
    }

    // ===== ShippingPolicyResult (보조 Fixtures) =====

    public static ShippingPolicyResult shippingPolicyResult() {
        return new ShippingPolicyResult(
                100L, "기본 배송 정책", true, true, "FIXED", "고정 배송비", 3000L, 50000L, FIXED_NOW);
    }

    // ===== RefundPolicyResult (보조 Fixtures) =====

    public static RefundPolicyResult refundPolicyResult() {
        return new RefundPolicyResult(
                200L,
                "기본 환불 정책",
                true,
                true,
                7,
                7,
                List.of(new NonReturnableConditionResult("USED", "사용한 상품")),
                FIXED_NOW);
    }
}
