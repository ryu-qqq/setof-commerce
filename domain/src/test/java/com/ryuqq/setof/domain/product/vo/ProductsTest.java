package com.ryuqq.setof.domain.product.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.exception.ProductNotFoundException;
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
@DisplayName("Products Value Object 테스트")
class ProductsTest {

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("상품 목록으로 Products를 복원한다")
        void reconstituteWithProductList() {
            // when
            Products products = ProductFixtures.singleProductsVO();

            // then
            assertThat(products).isNotNull();
            assertThat(products.size()).isEqualTo(1);
            assertThat(products.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("빈 상품 목록으로 Products를 복원한다")
        void reconstituteWithEmptyList() {
            // when
            Products products = ProductFixtures.emptyProductsVO();

            // then
            assertThat(products.isEmpty()).isTrue();
            assertThat(products.size()).isZero();
        }

        @Test
        @DisplayName("여러 상품으로 Products를 복원한다")
        void reconstituteWithMultipleProducts() {
            // when
            Products products = ProductFixtures.multiProductsVO();

            // then
            assertThat(products.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("reconstitute() 내부에서 불변 복사본으로 저장된다")
        void reconstituteStoresImmutableCopy() {
            // given
            Products products = ProductFixtures.singleProductsVO();

            // when & then
            assertThatThrownBy(() -> products.toList().add(ProductFixtures.newProduct()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("update() - ID 기반 diff 생성")
    class UpdateTest {

        @Test
        @DisplayName("기존 상품을 수정하면 retained에 포함된다")
        void existingProductIsRetained() {
            // given
            Products products = ProductFixtures.singleProductsVO();
            ProductUpdateData updateData = ProductFixtures.defaultProductUpdateData(1L);

            // when
            ProductDiff diff = products.update(updateData);

            // then
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
        }

        @Test
        @DisplayName("productId가 null인 엔트리는 신규 상품으로 added에 포함된다")
        void newEntryIsAdded() {
            // given
            Products products = ProductFixtures.emptyProductsVO();
            ProductUpdateData updateData =
                    new ProductUpdateData(
                            ProductGroupId.of(ProductFixtures.DEFAULT_PRODUCT_GROUP_ID),
                            List.of(ProductFixtures.newProductEntry()),
                            CommonVoFixtures.now());

            // when
            ProductDiff diff = products.update(updateData);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.retained()).isEmpty();
        }

        @Test
        @DisplayName("엔트리에 없는 기존 상품은 removed에 포함된다")
        void unmatchedProductIsRemoved() {
            // given
            Products products = ProductFixtures.multiProductsVO();
            // ID 1L만 엔트리에 포함, ID 2L은 제외
            ProductUpdateData updateData = ProductFixtures.defaultProductUpdateData(1L);

            // when
            ProductDiff diff = products.update(updateData);

            // then
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
        }

        @Test
        @DisplayName("존재하지 않는 productId로 수정하면 ProductNotFoundException이 발생한다")
        void throwExceptionWhenProductIdNotFound() {
            // given
            Products products = ProductFixtures.singleProductsVO(); // ID: 1L
            ProductUpdateData updateData = ProductFixtures.defaultProductUpdateData(999L); // 없는 ID

            // when & then
            assertThatThrownBy(() -> products.update(updateData))
                    .isInstanceOf(ProductNotFoundException.class);
        }

        @Test
        @DisplayName("skuCode가 null인 엔트리로 수정하면 기존 skuCode를 유지한다")
        void nullSkuCodeKeepsExistingSkuCode() {
            // given
            Products products = ProductFixtures.singleProductsVO();
            Instant now = CommonVoFixtures.now();
            ProductUpdateData updateData =
                    new ProductUpdateData(
                            ProductGroupId.of(ProductFixtures.DEFAULT_PRODUCT_GROUP_ID),
                            List.of(
                                    new ProductUpdateData.Entry(
                                            1L,
                                            null,
                                            Money.of(10000),
                                            Money.of(10000),
                                            100,
                                            0,
                                            List.of())),
                            now);

            // when
            ProductDiff diff = products.update(updateData);

            // then
            assertThat(diff.retained()).hasSize(1);
        }

        @Test
        @DisplayName("update 후 removed 상품은 deleted 상태가 된다")
        void removedProductIsDeleted() {
            // given
            Products products = ProductFixtures.multiProductsVO();
            ProductUpdateData updateData =
                    new ProductUpdateData(
                            ProductGroupId.of(ProductFixtures.DEFAULT_PRODUCT_GROUP_ID),
                            List.of(),
                            CommonVoFixtures.now());

            // when
            ProductDiff diff = products.update(updateData);

            // then
            assertThat(diff.removed()).hasSize(2);
            assertThat(diff.removed()).allMatch(Product::isDeleted);
        }

        @Test
        @DisplayName("신규 추가 상품은 ProductGroupId를 가진다")
        void addedProductHasProductGroupId() {
            // given
            long productGroupId = ProductFixtures.DEFAULT_PRODUCT_GROUP_ID;
            Products products = ProductFixtures.emptyProductsVO();
            ProductUpdateData updateData =
                    new ProductUpdateData(
                            ProductGroupId.of(productGroupId),
                            List.of(
                                    new ProductUpdateData.Entry(
                                            null,
                                            SkuCode.of("NEW-SKU"),
                                            Money.of(10000),
                                            Money.of(10000),
                                            50,
                                            0,
                                            List.of(SellerOptionValueId.of(1L)))),
                            CommonVoFixtures.now());

            // when
            ProductDiff diff = products.update(updateData);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.added().get(0).productGroupIdValue()).isEqualTo(productGroupId);
        }
    }

    @Nested
    @DisplayName("toList() - 상품 목록 반환")
    class ToListTest {

        @Test
        @DisplayName("toList()는 불변 리스트를 반환한다")
        void returnsUnmodifiableList() {
            // given
            Products products = ProductFixtures.singleProductsVO();

            // when & then
            assertThatThrownBy(() -> products.toList().add(ProductFixtures.newProduct()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("toList()는 포함된 모든 상품을 반환한다")
        void returnsAllProducts() {
            // given
            Products products = ProductFixtures.multiProductsVO();

            // then
            assertThat(products.toList()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("size() / isEmpty() 메서드 테스트")
    class SizeAndEmptyTest {

        @Test
        @DisplayName("size()는 상품 수를 반환한다")
        void sizeReturnsCount() {
            // given
            Products products = ProductFixtures.multiProductsVO();

            // then
            assertThat(products.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("isEmpty()는 상품이 없으면 true이다")
        void isEmptyReturnsTrueWhenEmpty() {
            // given
            Products products = ProductFixtures.emptyProductsVO();

            // then
            assertThat(products.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("isEmpty()는 상품이 있으면 false이다")
        void isEmptyReturnsFalseWhenNotEmpty() {
            // given
            Products products = ProductFixtures.singleProductsVO();

            // then
            assertThat(products.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("productGroupId() 메서드 테스트")
    class ProductGroupIdTest {

        @Test
        @DisplayName("productGroupId()는 ProductGroupId를 반환한다")
        void returnsProductGroupId() {
            // given
            Products products = ProductFixtures.singleProductsVO();

            // then
            assertThat(products.productGroupId()).isNotNull();
            assertThat(products.productGroupId().value())
                    .isEqualTo(ProductFixtures.DEFAULT_PRODUCT_GROUP_ID);
        }
    }
}
