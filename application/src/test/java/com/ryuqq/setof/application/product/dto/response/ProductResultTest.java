package com.ryuqq.setof.application.product.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductResult 단위 테스트")
class ProductResultTest {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("record 생성 - 필드 매핑 검증")
    class RecordConstructionTest {

        @Test
        @DisplayName("모든 필드를 포함하여 올바르게 생성된다")
        void constructor_AllFields_MappedCorrectly() {
            // given
            List<ProductOptionMappingResult> optionMappings =
                    List.of(ProductOptionMappingResult.withOptionNames(1L, 10L, 100L, "색상", "블랙"));

            // when
            ProductResult result =
                    new ProductResult(
                            10L,
                            1L,
                            "SKU-001",
                            30000,
                            25000,
                            null,
                            17,
                            10,
                            "ACTIVE",
                            1,
                            optionMappings,
                            FIXED_NOW,
                            FIXED_NOW);

            // then
            assertThat(result.id()).isEqualTo(10L);
            assertThat(result.productGroupId()).isEqualTo(1L);
            assertThat(result.skuCode()).isEqualTo("SKU-001");
            assertThat(result.regularPrice()).isEqualTo(30000);
            assertThat(result.currentPrice()).isEqualTo(25000);
            assertThat(result.salePrice()).isNull();
            assertThat(result.discountRate()).isEqualTo(17);
            assertThat(result.stockQuantity()).isEqualTo(10);
            assertThat(result.status()).isEqualTo("ACTIVE");
            assertThat(result.sortOrder()).isEqualTo(1);
            assertThat(result.optionMappings()).hasSize(1);
            assertThat(result.createdAt()).isEqualTo(FIXED_NOW);
            assertThat(result.updatedAt()).isEqualTo(FIXED_NOW);
        }

        @Test
        @DisplayName("salePrice가 있는 경우 올바르게 매핑된다")
        void constructor_WithSalePrice_MappedCorrectly() {
            // given / when
            ProductResult result =
                    new ProductResult(
                            10L,
                            1L,
                            "SKU-SALE-001",
                            30000,
                            25000,
                            20000,
                            33,
                            5,
                            "ACTIVE",
                            1,
                            List.of(),
                            FIXED_NOW,
                            FIXED_NOW);

            // then
            assertThat(result.salePrice()).isEqualTo(20000);
            assertThat(result.discountRate()).isEqualTo(33);
        }

        @Test
        @DisplayName("빈 optionMappings로 생성 시 빈 리스트를 반환한다")
        void constructor_EmptyOptionMappings_ReturnsEmptyList() {
            // given / when
            ProductResult result =
                    new ProductResult(
                            10L,
                            1L,
                            "SKU-NO-OPT",
                            30000,
                            25000,
                            null,
                            17,
                            0,
                            "SOLD_OUT",
                            2,
                            List.of(),
                            FIXED_NOW,
                            FIXED_NOW);

            // then
            assertThat(result.optionMappings()).isEmpty();
            assertThat(result.stockQuantity()).isZero();
            assertThat(result.status()).isEqualTo("SOLD_OUT");
        }
    }
}
