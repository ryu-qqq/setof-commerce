package com.ryuqq.setof.application.productgroup.dto.composite;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupExcelBaseBundle 단위 테스트")
class ProductGroupExcelBaseBundleTest {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("compact constructor - defensive copy 검증")
    class CompactConstructorTest {

        @Test
        @DisplayName("null composites는 빈 리스트로 대체된다")
        void constructor_NullComposites_ReplacedWithEmptyList() {
            // given / when
            ProductGroupExcelBaseBundle bundle =
                    new ProductGroupExcelBaseBundle(
                            null, Map.of(1L, "http://cdn.example.com/1.jpg"), 1L);

            // then
            assertThat(bundle.composites()).isNotNull();
            assertThat(bundle.composites()).isEmpty();
        }

        @Test
        @DisplayName("null descriptionCdnUrlByProductGroupId는 빈 Map으로 대체된다")
        void constructor_NullDescriptionMap_ReplacedWithEmptyMap() {
            // given / when
            ProductGroupExcelBaseBundle bundle =
                    new ProductGroupExcelBaseBundle(List.of(), null, 0L);

            // then
            assertThat(bundle.descriptionCdnUrlByProductGroupId()).isNotNull();
            assertThat(bundle.descriptionCdnUrlByProductGroupId()).isEmpty();
        }

        @Test
        @DisplayName("null composites와 null descriptionMap 모두 기본값으로 대체된다")
        void constructor_BothNull_ReplacedWithDefaults() {
            // given / when
            ProductGroupExcelBaseBundle bundle = new ProductGroupExcelBaseBundle(null, null, 0L);

            // then
            assertThat(bundle.composites()).isEmpty();
            assertThat(bundle.descriptionCdnUrlByProductGroupId()).isEmpty();
            assertThat(bundle.totalElements()).isZero();
        }

        @Test
        @DisplayName("정상 필드로 생성 시 모든 값이 유지된다")
        void constructor_ValidFields_RetainedCorrectly() {
            // given
            List<ProductGroupListCompositeResult> composites =
                    List.of(baseCompositeResult(1L), baseCompositeResult(2L));
            Map<Long, String> descMap =
                    Map.of(1L, "http://cdn.example.com/1.jpg", 2L, "http://cdn.example.com/2.jpg");

            // when
            ProductGroupExcelBaseBundle bundle =
                    new ProductGroupExcelBaseBundle(composites, descMap, 2L);

            // then
            assertThat(bundle.composites()).hasSize(2);
            assertThat(bundle.descriptionCdnUrlByProductGroupId()).hasSize(2);
            assertThat(bundle.totalElements()).isEqualTo(2L);
        }

        @Test
        @DisplayName("외부 composites 리스트를 변경해도 내부 상태에 영향을 주지 않는다")
        void constructor_ExternalCompositesMutation_DoesNotAffectInternal() {
            // given
            List<ProductGroupListCompositeResult> mutable =
                    new ArrayList<>(List.of(baseCompositeResult(1L)));
            ProductGroupExcelBaseBundle bundle =
                    new ProductGroupExcelBaseBundle(mutable, Map.of(), 1L);

            // when
            mutable.add(baseCompositeResult(2L));

            // then
            assertThat(bundle.composites()).hasSize(1);
        }

        @Test
        @DisplayName("외부 descriptionMap을 변경해도 내부 상태에 영향을 주지 않는다")
        void constructor_ExternalDescriptionMapMutation_DoesNotAffectInternal() {
            // given
            Map<Long, String> mutable = new HashMap<>();
            mutable.put(1L, "http://cdn.example.com/1.jpg");
            ProductGroupExcelBaseBundle bundle =
                    new ProductGroupExcelBaseBundle(List.of(), mutable, 0L);

            // when
            mutable.put(2L, "http://cdn.example.com/2.jpg");

            // then
            assertThat(bundle.descriptionCdnUrlByProductGroupId()).hasSize(1);
        }
    }

    // ===== Helper =====

    private ProductGroupListCompositeResult baseCompositeResult(Long productGroupId) {
        return ProductGroupListCompositeResult.ofBase(
                productGroupId,
                10L,
                "테스트셀러",
                20L,
                "테스트브랜드",
                "테스트브랜드",
                "TestBrand",
                "https://example.com/icon.png",
                30L,
                "의류",
                "1,2,30",
                3,
                "테스트 상품 그룹",
                "COMBINATION",
                "ACTIVE",
                "http://example.com/thumb.jpg",
                5,
                50000,
                45000,
                40000,
                20,
                FIXED_NOW,
                FIXED_NOW);
    }
}
