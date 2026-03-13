package com.ryuqq.setof.domain.product.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.exception.ProductInvalidPriceException;
import com.ryuqq.setof.domain.product.exception.ProductInvalidStatusTransitionException;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import com.ryuqq.setof.domain.product.vo.SkuCode;
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
@DisplayName("Product Aggregate 테스트")
class ProductTest {

    @Nested
    @DisplayName("forNew() - 신규 상품(SKU) 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 상품을 생성한다")
        void createNewProduct() {
            // when
            var product = ProductFixtures.newProduct();

            // then
            assertThat(product.isNew()).isTrue();
            assertThat(product.optionMappings()).hasSize(2);
            assertThat(product.regularPriceValue())
                    .isEqualTo(ProductFixtures.DEFAULT_REGULAR_PRICE);
            assertThat(product.currentPriceValue())
                    .isEqualTo(ProductFixtures.DEFAULT_CURRENT_PRICE);
            assertThat(product.stockQuantity()).isEqualTo(ProductFixtures.DEFAULT_STOCK_QUANTITY);
            assertThat(product.sortOrder()).isEqualTo(ProductFixtures.DEFAULT_SORT_ORDER);
            assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(product.isActive()).isTrue();
        }

        @Test
        @DisplayName("옵션 매핑 없이 상품을 생성할 수 있다")
        void createWithoutOptionMappings() {
            // when
            var product =
                    Product.forNew(
                            ProductGroupId.of(1L),
                            null,
                            Money.of(10000),
                            Money.of(10000),
                            100,
                            0,
                            List.of(),
                            CommonVoFixtures.now());

            // then
            assertThat(product.optionMappings()).isEmpty();
        }

        @Test
        @DisplayName("currentPrice가 regularPrice보다 크면 ProductInvalidPriceException이 발생한다")
        void throwExceptionWhenCurrentPriceExceedsRegularPrice() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    Product.forNew(
                                            ProductGroupId.of(1L),
                                            null,
                                            Money.of(5000),
                                            Money.of(10000),
                                            100,
                                            0,
                                            List.of(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidPriceException.class);
        }

        @Test
        @DisplayName("재고 수량이 음수이면 IllegalArgumentException이 발생한다")
        void throwExceptionWhenNegativeStockQuantity() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    Product.forNew(
                                            ProductGroupId.of(1L),
                                            null,
                                            Money.of(10000),
                                            Money.of(10000),
                                            -1,
                                            0,
                                            List.of(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("재고 수량은 0 이상이어야 합니다");
        }

        @Test
        @DisplayName("할인율이 자동 계산된다")
        void discountRateIsAutoCalculated() {
            // when
            var product =
                    Product.forNew(
                            ProductGroupId.of(1L),
                            null,
                            Money.of(10000),
                            Money.of(8000),
                            100,
                            0,
                            List.of(),
                            CommonVoFixtures.now());

            // then
            assertThat(product.discountRate()).isEqualTo(20);
            assertThat(product.salePriceValue()).isEqualTo(8000);
        }

        @Test
        @DisplayName("SKU 코드를 지정하여 상품을 생성할 수 있다")
        void createWithSkuCode() {
            // when
            var product =
                    Product.forNew(
                            ProductGroupId.of(1L),
                            SkuCode.of("SKU-001"),
                            Money.of(10000),
                            Money.of(10000),
                            50,
                            1,
                            List.of(),
                            CommonVoFixtures.now());

            // then
            assertThat(product.skuCodeValue()).isEqualTo("SKU-001");
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 상품을 복원한다")
        void reconstituteActiveProduct() {
            // when
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.isNew()).isFalse();
            assertThat(product.idValue()).isEqualTo(1L);
            assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(product.isActive()).isTrue();
        }

        @Test
        @DisplayName("비활성 상태의 상품을 복원한다")
        void reconstituteInactiveProduct() {
            // when
            var product = ProductFixtures.inactiveProduct();

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.INACTIVE);
            assertThat(product.isActive()).isFalse();
        }

        @Test
        @DisplayName("품절 상태의 상품을 복원한다")
        void reconstituteSoldOutProduct() {
            // when
            var product = ProductFixtures.soldOutProduct();

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.SOLD_OUT);
            assertThat(product.isSoldOut()).isTrue();
        }

        @Test
        @DisplayName("삭제된 상태의 상품을 복원한다")
        void reconstituteDeletedProduct() {
            // when
            var product = ProductFixtures.deletedProduct();

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.DELETED);
            assertThat(product.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("markSoldOut() - 품절 처리")
    class MarkSoldOutTest {

        @Test
        @DisplayName("활성 상태에서 품절 처리한다")
        void markSoldOutFromActive() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.markSoldOut(now);

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.SOLD_OUT);
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성 상태에서 품절 처리하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenMarkSoldOutFromInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();

            // when & then
            assertThatThrownBy(() -> product.markSoldOut(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("삭제된 상태에서 품절 처리하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenMarkSoldOutFromDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.markSoldOut(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("이미 품절된 상태에서 품절 처리하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenAlreadySoldOut() {
            // given
            var product = ProductFixtures.soldOutProduct();

            // when & then
            assertThatThrownBy(() -> product.markSoldOut(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("activate() - 활성화")
    class ActivateTest {

        @Test
        @DisplayName("비활성 상태에서 활성화한다")
        void activateFromInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.activate(now);

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(product.isActive()).isTrue();
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("품절 상태에서 활성화한다")
        void activateFromSoldOut() {
            // given
            var product = ProductFixtures.soldOutProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.activate(now);

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(product.isActive()).isTrue();
        }

        @Test
        @DisplayName("이미 활성 상태에서 활성화하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenAlreadyActive() {
            // given
            var product = ProductFixtures.activeProduct();

            // when & then
            assertThatThrownBy(() -> product.activate(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("삭제된 상태에서 활성화하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenActivateFromDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.activate(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("deactivate() - 비활성화")
    class DeactivateTest {

        @Test
        @DisplayName("활성 상태에서 비활성화한다")
        void deactivateFromActive() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.deactivate(now);

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.INACTIVE);
            assertThat(product.isActive()).isFalse();
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성 상태에서 비활성화하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenAlreadyInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();

            // when & then
            assertThatThrownBy(() -> product.deactivate(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("품절 상태에서 비활성화하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenDeactivateFromSoldOut() {
            // given
            var product = ProductFixtures.soldOutProduct();

            // when & then
            assertThatThrownBy(() -> product.deactivate(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("삭제된 상태에서 비활성화하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenDeactivateFromDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.deactivate(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("delete() - 삭제 처리")
    class DeleteTest {

        @Test
        @DisplayName("활성 상태에서 삭제한다")
        void deleteFromActive() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.delete(now);

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.DELETED);
            assertThat(product.isDeleted()).isTrue();
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성 상태에서 삭제한다")
        void deleteFromInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.delete(now);

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.DELETED);
            assertThat(product.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("품절 상태에서 삭제한다")
        void deleteFromSoldOut() {
            // given
            var product = ProductFixtures.soldOutProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.delete(now);

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.DELETED);
            assertThat(product.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("이미 삭제된 상태에서 삭제하면 ProductInvalidStatusTransitionException이 발생한다")
        void throwExceptionWhenAlreadyDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.delete(CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("삭제 시 옵션 매핑도 함께 삭제된다")
        void deleteAlsoDeletesOptionMappings() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.delete(now);

            // then
            assertThat(product.optionMappings()).allMatch(ProductOptionMapping::isDeleted);
        }
    }

    @Nested
    @DisplayName("updatePrice() - 가격 수정")
    class UpdatePriceTest {

        @Test
        @DisplayName("가격을 수정한다")
        void updatePriceSuccessfully() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updatePrice(Money.of(20000), Money.of(15000), now);

            // then
            assertThat(product.regularPriceValue()).isEqualTo(20000);
            assertThat(product.currentPriceValue()).isEqualTo(15000);
            assertThat(product.salePriceValue()).isEqualTo(15000);
            assertThat(product.discountRate()).isEqualTo(25);
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("currentPrice가 regularPrice보다 크면 ProductInvalidPriceException이 발생한다")
        void throwExceptionWhenCurrentPriceExceedsRegularPrice() {
            // given
            var product = ProductFixtures.activeProduct();

            // when & then
            assertThatThrownBy(
                            () ->
                                    product.updatePrice(
                                            Money.of(5000),
                                            Money.of(10000),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(ProductInvalidPriceException.class);
        }

        @Test
        @DisplayName("동일 가격으로 수정하면 할인율이 0이 된다")
        void updatePriceWithSameValues() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updatePrice(Money.of(10000), Money.of(10000), now);

            // then
            assertThat(product.discountRate()).isZero();
        }
    }

    @Nested
    @DisplayName("updateStock() - 재고 수정")
    class UpdateStockTest {

        @Test
        @DisplayName("재고를 수정한다")
        void updateStockSuccessfully() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateStock(200, now);

            // then
            assertThat(product.stockQuantity()).isEqualTo(200);
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("재고를 0으로 수정할 수 있다")
        void updateStockToZero() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateStock(0, now);

            // then
            assertThat(product.stockQuantity()).isZero();
            assertThat(product.hasStock()).isFalse();
        }

        @Test
        @DisplayName("음수 재고로 수정하면 IllegalArgumentException이 발생한다")
        void throwExceptionWhenNegativeStock() {
            // given
            var product = ProductFixtures.activeProduct();

            // when & then
            assertThatThrownBy(() -> product.updateStock(-1, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("재고 수량은 0 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("updateSkuCode() - SKU 코드 수정")
    class UpdateSkuCodeTest {

        @Test
        @DisplayName("SKU 코드를 수정한다")
        void updateSkuCodeSuccessfully() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateSkuCode(SkuCode.of("NEW-SKU-001"), now);

            // then
            assertThat(product.skuCodeValue()).isEqualTo("NEW-SKU-001");
            assertThat(product.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("updateSortOrder() - 정렬 순서 수정")
    class UpdateSortOrderTest {

        @Test
        @DisplayName("정렬 순서를 수정한다")
        void updateSortOrderSuccessfully() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateSortOrder(5, now);

            // then
            assertThat(product.sortOrder()).isEqualTo(5);
            assertThat(product.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("update() - 전체 속성 일괄 수정")
    class UpdateTest {

        @Test
        @DisplayName("전체 속성을 일괄 수정한다")
        void updateAllPropertiesSuccessfully() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.update(SkuCode.of("BULK-SKU"), Money.of(20000), Money.of(16000), 50, 3, now);

            // then
            assertThat(product.skuCodeValue()).isEqualTo("BULK-SKU");
            assertThat(product.regularPriceValue()).isEqualTo(20000);
            assertThat(product.currentPriceValue()).isEqualTo(16000);
            assertThat(product.stockQuantity()).isEqualTo(50);
            assertThat(product.sortOrder()).isEqualTo(3);
            assertThat(product.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드 테스트")
    class BusinessMethodTest {

        @Test
        @DisplayName("isSoldOut()은 SOLD_OUT 상태이면 true를 반환한다")
        void isSoldOutReturnsTrue() {
            // given
            var product = ProductFixtures.soldOutProduct();

            // then
            assertThat(product.isSoldOut()).isTrue();
        }

        @Test
        @DisplayName("isSoldOut()은 ACTIVE 상태이면 false를 반환한다")
        void isSoldOutReturnsFalseWhenActive() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.isSoldOut()).isFalse();
        }

        @Test
        @DisplayName("isActive()는 ACTIVE 상태이면 true를 반환한다")
        void isActiveReturnsTrue() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.isActive()).isTrue();
        }

        @Test
        @DisplayName("isActive()는 INACTIVE 상태이면 false를 반환한다")
        void isActiveReturnsFalseWhenInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();

            // then
            assertThat(product.isActive()).isFalse();
        }

        @Test
        @DisplayName("hasStock()은 재고가 있으면 true를 반환한다")
        void hasStockReturnsTrue() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.hasStock()).isTrue();
        }

        @Test
        @DisplayName("optionMappings()의 크기를 확인한다")
        void optionMappingsSize() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.optionMappings()).hasSize(2);
        }

        @Test
        @DisplayName("옵션 매핑이 없는 상품의 optionMappings()는 비어있다")
        void optionMappingsEmptyWhenNoOptions() {
            // given
            var product = ProductFixtures.productWithoutOptions();

            // then
            assertThat(product.optionMappings()).isEmpty();
        }

        @Test
        @DisplayName("isOnSale() - salePrice가 null이면 false를 반환한다")
        void isOnSaleReturnsFalseWhenSalePriceIsNull() {
            // given: salePrice를 null로 세팅한 상품 reconstitute
            var product =
                    Product.reconstitute(
                            com.ryuqq.setof.domain.product.id.ProductId.of(10L),
                            com.ryuqq.setof.domain.productgroup.id.ProductGroupId.of(1L),
                            null,
                            Money.of(10000),
                            Money.of(8000),
                            null,
                            20,
                            100,
                            com.ryuqq.setof.domain.product.vo.ProductStatus.ACTIVE,
                            0,
                            List.of(),
                            CommonVoFixtures.now(),
                            CommonVoFixtures.now());

            // then
            assertThat(product.isOnSale()).isFalse();
        }

        @Test
        @DisplayName("effectivePrice() - discountRate > 0이지만 salePrice가 null이면 currentPrice를 반환한다")
        void effectivePriceReturnsCurrentPriceWhenSalePriceIsNullButDiscountRatePositive() {
            // given: discountRate > 0이지만 salePrice가 null인 상품
            var product =
                    Product.reconstitute(
                            com.ryuqq.setof.domain.product.id.ProductId.of(11L),
                            com.ryuqq.setof.domain.productgroup.id.ProductGroupId.of(1L),
                            null,
                            Money.of(10000),
                            Money.of(8000),
                            null,
                            20,
                            100,
                            com.ryuqq.setof.domain.product.vo.ProductStatus.ACTIVE,
                            0,
                            List.of(),
                            CommonVoFixtures.now(),
                            CommonVoFixtures.now());

            // when
            var effectivePrice = product.effectivePrice();

            // then: salePrice가 null이므로 isOnSale() = false → currentPrice 반환
            assertThat(effectivePrice.value()).isEqualTo(8000);
        }
    }

    @Nested
    @DisplayName("changeStatus() - 통합 상태 전이")
    class ChangeStatusTest {

        @Test
        @DisplayName("ACTIVE 상태로 변경한다")
        void changeStatusToActive() {
            // given
            var product = ProductFixtures.inactiveProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.changeStatus(com.ryuqq.setof.domain.product.vo.ProductStatus.ACTIVE, now);

            // then
            assertThat(product.status())
                    .isEqualTo(com.ryuqq.setof.domain.product.vo.ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("INACTIVE 상태로 변경한다")
        void changeStatusToInactive() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.changeStatus(com.ryuqq.setof.domain.product.vo.ProductStatus.INACTIVE, now);

            // then
            assertThat(product.status())
                    .isEqualTo(com.ryuqq.setof.domain.product.vo.ProductStatus.INACTIVE);
        }

        @Test
        @DisplayName("SOLD_OUT 상태로 변경한다")
        void changeStatusToSoldOut() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.changeStatus(com.ryuqq.setof.domain.product.vo.ProductStatus.SOLD_OUT, now);

            // then
            assertThat(product.status())
                    .isEqualTo(com.ryuqq.setof.domain.product.vo.ProductStatus.SOLD_OUT);
        }

        @Test
        @DisplayName("DELETED 상태로 변경한다")
        void changeStatusToDeleted() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.changeStatus(com.ryuqq.setof.domain.product.vo.ProductStatus.DELETED, now);

            // then
            assertThat(product.status())
                    .isEqualTo(com.ryuqq.setof.domain.product.vo.ProductStatus.DELETED);
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("id()는 ProductId를 반환한다")
        void returnsId() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.id()).isNotNull();
            assertThat(product.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("productGroupId()는 ProductGroupId를 반환한다")
        void returnsProductGroupId() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.productGroupId()).isNotNull();
            assertThat(product.productGroupIdValue())
                    .isEqualTo(ProductFixtures.DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("optionMappings()는 불변 리스트를 반환한다")
        void returnsUnmodifiableOptionMappings() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.optionMappings()).isNotNull();
            assertThat(product.optionMappings()).hasSize(2);
            assertThatThrownBy(
                            () ->
                                    product.optionMappings()
                                            .add(
                                                    ProductOptionMapping.forNew(
                                                            ProductId.forNew(),
                                                            SellerOptionValueId.of(99L))))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("regularPrice()는 Money를 반환한다")
        void returnsRegularPrice() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.regularPrice()).isNotNull();
            assertThat(product.regularPriceValue())
                    .isEqualTo(ProductFixtures.DEFAULT_REGULAR_PRICE);
        }

        @Test
        @DisplayName("currentPrice()는 Money를 반환한다")
        void returnsCurrentPrice() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.currentPrice()).isNotNull();
            assertThat(product.currentPriceValue())
                    .isEqualTo(ProductFixtures.DEFAULT_CURRENT_PRICE);
        }

        @Test
        @DisplayName("stockQuantity()는 재고 수량을 반환한다")
        void returnsStockQuantity() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.stockQuantity()).isEqualTo(ProductFixtures.DEFAULT_STOCK_QUANTITY);
        }

        @Test
        @DisplayName("sortOrder()는 정렬 순서를 반환한다")
        void returnsSortOrder() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.sortOrder()).isEqualTo(ProductFixtures.DEFAULT_SORT_ORDER);
        }

        @Test
        @DisplayName("status()는 ProductStatus를 반환한다")
        void returnsStatus() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("createdAt()은 생성 시각을 반환한다")
        void returnsCreatedAt() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("updatedAt()은 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("특정 ID로 생성한 상품의 idValue()가 해당 ID를 반환한다")
        void returnsSpecificId() {
            // given
            var product = ProductFixtures.activeProduct(99L);

            // then
            assertThat(product.idValue()).isEqualTo(99L);
        }
    }
}
