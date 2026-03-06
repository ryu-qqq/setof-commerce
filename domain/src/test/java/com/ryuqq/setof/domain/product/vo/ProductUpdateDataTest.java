package com.ryuqq.setof.domain.product.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.setof.commerce.domain.product.ProductFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductUpdateData Value Object 테스트")
class ProductUpdateDataTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 값으로 ProductUpdateData를 생성한다")
        void createWithValidValues() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<ProductUpdateData.Entry> entries =
                    List.of(ProductFixtures.existingProductEntry(1L));
            Instant updatedAt = CommonVoFixtures.now();

            // when
            ProductUpdateData data = new ProductUpdateData(productGroupId, entries, updatedAt);

            // then
            assertThat(data.productGroupId()).isEqualTo(productGroupId);
            assertThat(data.entries()).hasSize(1);
            assertThat(data.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("entries는 불변 복사본으로 저장된다")
        void entriesAreCopiedToImmutableList() {
            // given
            ProductUpdateData data = ProductFixtures.defaultProductUpdateData(1L);

            // when & then
            assertThatUnsupportedOperationExceptionIsThrown(data.entries());
        }

        private void assertThatUnsupportedOperationExceptionIsThrown(
                List<ProductUpdateData.Entry> entries) {
            try {
                entries.add(ProductFixtures.newProductEntry());
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }

        @Test
        @DisplayName("빈 entries로 ProductUpdateData를 생성할 수 있다")
        void createWithEmptyEntries() {
            // when
            ProductUpdateData data =
                    new ProductUpdateData(ProductGroupId.of(1L), List.of(), CommonVoFixtures.now());

            // then
            assertThat(data.entries()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Entry 생성 테스트")
    class EntryCreationTest {

        @Test
        @DisplayName("기존 상품 수정 엔트리를 생성한다 (productId non-null)")
        void createExistingProductEntry() {
            // when
            ProductUpdateData.Entry entry = ProductFixtures.existingProductEntry(10L);

            // then
            assertThat(entry.productId()).isEqualTo(10L);
            assertThat(entry.skuCode()).isNotNull();
            assertThat(entry.regularPrice().value())
                    .isEqualTo(ProductFixtures.DEFAULT_REGULAR_PRICE);
            assertThat(entry.currentPrice().value())
                    .isEqualTo(ProductFixtures.DEFAULT_CURRENT_PRICE);
            assertThat(entry.stockQuantity()).isEqualTo(ProductFixtures.DEFAULT_STOCK_QUANTITY);
            assertThat(entry.sortOrder()).isEqualTo(ProductFixtures.DEFAULT_SORT_ORDER);
        }

        @Test
        @DisplayName("신규 상품 엔트리를 생성한다 (productId null)")
        void createNewProductEntry() {
            // when
            ProductUpdateData.Entry entry = ProductFixtures.newProductEntry();

            // then
            assertThat(entry.productId()).isNull();
            assertThat(entry.resolvedOptionValueIds()).isNotEmpty();
        }

        @Test
        @DisplayName("Entry의 resolvedOptionValueIds는 불변 복사본으로 저장된다")
        void entryOptionValueIdsAreCopied() {
            // given
            ProductUpdateData.Entry entry =
                    new ProductUpdateData.Entry(
                            1L,
                            SkuCode.of("SKU-001"),
                            Money.of(10000),
                            Money.of(10000),
                            100,
                            0,
                            List.of(SellerOptionValueId.of(1L)));

            // when & then
            try {
                entry.resolvedOptionValueIds().add(SellerOptionValueId.of(99L));
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }

        @Test
        @DisplayName("기존 상품 엔트리에는 resolvedOptionValueIds가 비어있다")
        void existingEntryHasEmptyOptionValueIds() {
            // when
            ProductUpdateData.Entry entry = ProductFixtures.existingProductEntry(1L);

            // then
            assertThat(entry.resolvedOptionValueIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("productId 구분 테스트")
    class ProductIdDistinctionTest {

        @Test
        @DisplayName("productId가 null이면 신규 상품 엔트리이다")
        void nullProductIdIsNewEntry() {
            // when
            ProductUpdateData.Entry entry = ProductFixtures.newProductEntry();

            // then
            assertThat(entry.productId()).isNull();
        }

        @Test
        @DisplayName("productId가 non-null이면 기존 상품 엔트리이다")
        void nonNullProductIdIsExistingEntry() {
            // when
            ProductUpdateData.Entry entry = ProductFixtures.existingProductEntry(5L);

            // then
            assertThat(entry.productId()).isNotNull();
            assertThat(entry.productId()).isEqualTo(5L);
        }
    }
}
