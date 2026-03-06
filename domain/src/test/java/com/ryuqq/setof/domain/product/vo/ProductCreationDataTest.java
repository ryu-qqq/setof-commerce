package com.ryuqq.setof.domain.product.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.aggregate.Product;
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
@DisplayName("ProductCreationData Value Object 테스트")
class ProductCreationDataTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 값으로 ProductCreationData를 생성한다")
        void createWithValidValues() {
            // when
            ProductCreationData data = ProductFixtures.defaultProductCreationData();

            // then
            assertThat(data).isNotNull();
            assertThat(data.skuCode()).isNotNull();
            assertThat(data.regularPrice().value())
                    .isEqualTo(ProductFixtures.DEFAULT_REGULAR_PRICE);
            assertThat(data.currentPrice().value())
                    .isEqualTo(ProductFixtures.DEFAULT_CURRENT_PRICE);
            assertThat(data.stockQuantity()).isEqualTo(ProductFixtures.DEFAULT_STOCK_QUANTITY);
            assertThat(data.sortOrder()).isEqualTo(ProductFixtures.DEFAULT_SORT_ORDER);
            assertThat(data.resolvedOptionValueIds()).hasSize(2);
        }

        @Test
        @DisplayName("옵션 없이 ProductCreationData를 생성할 수 있다")
        void createWithoutOptions() {
            // when
            ProductCreationData data = ProductFixtures.productCreationDataWithoutOptions();

            // then
            assertThat(data.resolvedOptionValueIds()).isEmpty();
        }

        @Test
        @DisplayName("record 타입으로 필드 직접 접근이 가능하다")
        void fieldAccessViaRecord() {
            // given
            SkuCode skuCode = SkuCode.of("SKU-TEST");
            Money regularPrice = Money.of(20000);
            Money currentPrice = Money.of(15000);
            int stockQuantity = 50;
            int sortOrder = 2;
            List<SellerOptionValueId> optionValueIds = List.of(SellerOptionValueId.of(10L));

            // when
            ProductCreationData data =
                    new ProductCreationData(
                            skuCode,
                            regularPrice,
                            currentPrice,
                            stockQuantity,
                            sortOrder,
                            optionValueIds);

            // then
            assertThat(data.skuCode()).isEqualTo(skuCode);
            assertThat(data.regularPrice()).isEqualTo(regularPrice);
            assertThat(data.currentPrice()).isEqualTo(currentPrice);
            assertThat(data.stockQuantity()).isEqualTo(stockQuantity);
            assertThat(data.sortOrder()).isEqualTo(sortOrder);
            assertThat(data.resolvedOptionValueIds()).containsExactly(SellerOptionValueId.of(10L));
        }
    }

    @Nested
    @DisplayName("toProduct() - Product 도메인 객체 생성")
    class ToProductTest {

        @Test
        @DisplayName("ProductGroupId와 시각으로 Product를 생성한다")
        void toProductCreatesProductSuccessfully() {
            // given
            ProductCreationData data = ProductFixtures.defaultProductCreationData();
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            Instant now = CommonVoFixtures.now();

            // when
            Product product = data.toProduct(productGroupId, now);

            // then
            assertThat(product).isNotNull();
            assertThat(product.isNew()).isTrue();
            assertThat(product.productGroupIdValue()).isEqualTo(1L);
            assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("ProductCreationData의 가격 정보가 Product에 반영된다")
        void toProductReflectsPriceData() {
            // given
            ProductCreationData data =
                    new ProductCreationData(
                            SkuCode.of("SKU-PRICE"),
                            Money.of(20000),
                            Money.of(16000),
                            100,
                            0,
                            List.of());
            Instant now = CommonVoFixtures.now();

            // when
            Product product = data.toProduct(ProductGroupId.of(1L), now);

            // then
            assertThat(product.regularPriceValue()).isEqualTo(20000);
            assertThat(product.currentPriceValue()).isEqualTo(16000);
            assertThat(product.discountRate()).isEqualTo(20);
        }

        @Test
        @DisplayName("ProductCreationData의 옵션 값 목록으로 옵션 매핑이 생성된다")
        void toProductCreatesOptionMappings() {
            // given
            List<SellerOptionValueId> optionValueIds =
                    List.of(
                            SellerOptionValueId.of(1L),
                            SellerOptionValueId.of(2L),
                            SellerOptionValueId.of(3L));
            ProductCreationData data =
                    new ProductCreationData(
                            SkuCode.of("SKU-OPT"),
                            Money.of(10000),
                            Money.of(10000),
                            50,
                            0,
                            optionValueIds);

            // when
            Product product = data.toProduct(ProductGroupId.of(1L), CommonVoFixtures.now());

            // then
            assertThat(product.optionMappings()).hasSize(3);
        }

        @Test
        @DisplayName("옵션 없는 ProductCreationData로 옵션 매핑 없는 Product를 생성한다")
        void toProductWithoutOptionsCreatesEmptyMappings() {
            // given
            ProductCreationData data = ProductFixtures.productCreationDataWithoutOptions();

            // when
            Product product = data.toProduct(ProductGroupId.of(1L), CommonVoFixtures.now());

            // then
            assertThat(product.optionMappings()).isEmpty();
        }

        @Test
        @DisplayName("ProductCreationData의 재고 및 정렬 순서가 Product에 반영된다")
        void toProductReflectsStockAndSortOrder() {
            // given
            ProductCreationData data =
                    new ProductCreationData(
                            null, Money.of(10000), Money.of(10000), 200, 5, List.of());

            // when
            Product product = data.toProduct(ProductGroupId.of(1L), CommonVoFixtures.now());

            // then
            assertThat(product.stockQuantity()).isEqualTo(200);
            assertThat(product.sortOrder()).isEqualTo(5);
        }
    }
}
