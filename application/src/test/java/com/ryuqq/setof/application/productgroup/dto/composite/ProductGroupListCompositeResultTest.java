package com.ryuqq.setof.application.productgroup.dto.composite;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupListCompositeResult 단위 테스트")
class ProductGroupListCompositeResultTest {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("ofBase() - 기본값 생성 검증")
    class OfBaseTest {

        @Test
        @DisplayName("ofBase()로 생성 시 minPrice, maxPrice, maxDiscountRate는 0이다")
        void ofBase_PriceEnrichmentFields_DefaultToZero() {
            // given / when
            ProductGroupListCompositeResult result = createBaseResult(1L);

            // then
            assertThat(result.minPrice()).isZero();
            assertThat(result.maxPrice()).isZero();
            assertThat(result.maxDiscountRate()).isZero();
        }

        @Test
        @DisplayName("ofBase()로 생성 시 optionGroups는 빈 리스트이다")
        void ofBase_OptionGroups_DefaultToEmptyList() {
            // given / when
            ProductGroupListCompositeResult result = createBaseResult(1L);

            // then
            assertThat(result.optionGroups()).isNotNull();
            assertThat(result.optionGroups()).isEmpty();
        }

        @Test
        @DisplayName("ofBase()로 생성 시 base 필드들이 올바르게 설정된다")
        void ofBase_BaseFields_SetCorrectly() {
            // given / when
            ProductGroupListCompositeResult result = createBaseResult(1L);

            // then
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.sellerId()).isEqualTo(10L);
            assertThat(result.sellerName()).isEqualTo("테스트셀러");
            assertThat(result.brandId()).isEqualTo(20L);
            assertThat(result.brandName()).isEqualTo("테스트브랜드");
            assertThat(result.categoryId()).isEqualTo(30L);
            assertThat(result.categoryName()).isEqualTo("의류");
            assertThat(result.categoryPath()).isEqualTo("1,2,30");
            assertThat(result.categoryDepth()).isEqualTo(3);
            assertThat(result.productGroupName()).isEqualTo("테스트 상품 그룹");
            assertThat(result.optionType()).isEqualTo("COMBINATION");
            assertThat(result.status()).isEqualTo("ACTIVE");
            assertThat(result.thumbnailUrl()).isEqualTo("http://example.com/thumb.jpg");
            assertThat(result.productCount()).isEqualTo(5);
            assertThat(result.createdAt()).isEqualTo(FIXED_NOW);
            assertThat(result.updatedAt()).isEqualTo(FIXED_NOW);
        }
    }

    @Nested
    @DisplayName("withEnrichment() - enrichment 교체 검증")
    class WithEnrichmentTest {

        @Test
        @DisplayName("withEnrichment()는 enrichment 필드만 교체한다")
        void withEnrichment_ReplacesEnrichmentFieldsOnly() {
            // given
            ProductGroupListCompositeResult base = createBaseResult(1L);
            List<OptionGroupSummaryResult> optionGroups =
                    List.of(
                            new OptionGroupSummaryResult("색상", List.of("블랙", "화이트")),
                            new OptionGroupSummaryResult("사이즈", List.of("M", "L")));

            // when
            ProductGroupListCompositeResult enriched =
                    base.withEnrichment(10000, 50000, 20, optionGroups);

            // then
            assertThat(enriched.minPrice()).isEqualTo(10000);
            assertThat(enriched.maxPrice()).isEqualTo(50000);
            assertThat(enriched.maxDiscountRate()).isEqualTo(20);
            assertThat(enriched.optionGroups()).hasSize(2);
        }

        @Test
        @DisplayName("withEnrichment()는 기존 base 필드들을 그대로 유지한다")
        void withEnrichment_RetainsBaseFields() {
            // given
            ProductGroupListCompositeResult base = createBaseResult(1L);

            // when
            ProductGroupListCompositeResult enriched =
                    base.withEnrichment(10000, 50000, 20, List.of());

            // then
            assertThat(enriched.id()).isEqualTo(base.id());
            assertThat(enriched.sellerId()).isEqualTo(base.sellerId());
            assertThat(enriched.sellerName()).isEqualTo(base.sellerName());
            assertThat(enriched.brandId()).isEqualTo(base.brandId());
            assertThat(enriched.brandName()).isEqualTo(base.brandName());
            assertThat(enriched.categoryId()).isEqualTo(base.categoryId());
            assertThat(enriched.categoryName()).isEqualTo(base.categoryName());
            assertThat(enriched.categoryPath()).isEqualTo(base.categoryPath());
            assertThat(enriched.categoryDepth()).isEqualTo(base.categoryDepth());
            assertThat(enriched.productGroupName()).isEqualTo(base.productGroupName());
            assertThat(enriched.optionType()).isEqualTo(base.optionType());
            assertThat(enriched.status()).isEqualTo(base.status());
            assertThat(enriched.thumbnailUrl()).isEqualTo(base.thumbnailUrl());
            assertThat(enriched.productCount()).isEqualTo(base.productCount());
            assertThat(enriched.createdAt()).isEqualTo(base.createdAt());
            assertThat(enriched.updatedAt()).isEqualTo(base.updatedAt());
        }

        @Test
        @DisplayName("withEnrichment()는 원본 인스턴스를 변경하지 않는다")
        void withEnrichment_DoesNotMutateOriginal() {
            // given
            ProductGroupListCompositeResult base = createBaseResult(1L);

            // when
            base.withEnrichment(
                    10000, 50000, 20, List.of(new OptionGroupSummaryResult("색상", List.of("블랙"))));

            // then
            assertThat(base.minPrice()).isZero();
            assertThat(base.maxPrice()).isZero();
            assertThat(base.optionGroups()).isEmpty();
        }

        @Test
        @DisplayName("withEnrichment()에 전달한 optionGroups 외부 변경은 내부에 영향을 주지 않는다")
        void withEnrichment_ExternalOptionGroupsMutation_DoesNotAffectInternal() {
            // given
            ProductGroupListCompositeResult base = createBaseResult(1L);
            List<OptionGroupSummaryResult> mutable =
                    new ArrayList<>(List.of(new OptionGroupSummaryResult("색상", List.of("블랙"))));
            ProductGroupListCompositeResult enriched =
                    base.withEnrichment(10000, 50000, 20, mutable);

            // when
            mutable.add(new OptionGroupSummaryResult("사이즈", List.of("M")));

            // then
            assertThat(enriched.optionGroups()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("compact constructor - defensive copy 검증")
    class CompactConstructorTest {

        @Test
        @DisplayName("null optionGroups로 직접 생성 시 빈 리스트로 대체된다")
        void constructor_NullOptionGroups_ReplacedWithEmptyList() {
            // given / when
            ProductGroupListCompositeResult result =
                    new ProductGroupListCompositeResult(
                            1L,
                            10L,
                            "셀러",
                            20L,
                            "브랜드",
                            30L,
                            "의류",
                            "1,2,30",
                            3,
                            "상품명",
                            "COMBINATION",
                            "ACTIVE",
                            "http://thumb.jpg",
                            5,
                            0,
                            0,
                            0,
                            null,
                            FIXED_NOW,
                            FIXED_NOW);

            // then
            assertThat(result.optionGroups()).isNotNull();
            assertThat(result.optionGroups()).isEmpty();
        }
    }

    // ===== Helper =====

    private ProductGroupListCompositeResult createBaseResult(Long productGroupId) {
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
}
