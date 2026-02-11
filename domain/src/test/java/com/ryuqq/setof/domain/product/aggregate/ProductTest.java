package com.ryuqq.setof.domain.product.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.setof.commerce.domain.product.ProductFixtures;
import java.time.Instant;
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
            assertThat(product.option1Name()).isEqualTo(ProductFixtures.DEFAULT_OPTION1_NAME);
            assertThat(product.option1Value()).isEqualTo(ProductFixtures.DEFAULT_OPTION1_VALUE);
            assertThat(product.option2Name()).isEqualTo(ProductFixtures.DEFAULT_OPTION2_NAME);
            assertThat(product.option2Value()).isEqualTo(ProductFixtures.DEFAULT_OPTION2_VALUE);
            assertThat(product.stockQuantity()).isEqualTo(ProductFixtures.DEFAULT_STOCK);
            assertThat(product.additionalPriceValue())
                    .isEqualTo(ProductFixtures.DEFAULT_ADDITIONAL_PRICE);
            assertThat(product.status()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(product.isActive()).isTrue();
        }

        @Test
        @DisplayName("additionalPrice가 null이면 Money.zero()로 설정된다")
        void createWithNullAdditionalPrice() {
            // when
            var product =
                    Product.forNew(
                            ProductGroupId.of(1L),
                            "색상",
                            "블랙",
                            null,
                            null,
                            null,
                            10,
                            CommonVoFixtures.now());

            // then
            assertThat(product.additionalPrice()).isEqualTo(Money.zero());
            assertThat(product.additionalPriceValue()).isZero();
        }

        @Test
        @DisplayName("재고가 음수이면 IllegalArgumentException이 발생한다")
        void throwExceptionForNegativeStock() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    Product.forNew(
                                            ProductGroupId.of(1L),
                                            "색상",
                                            "블랙",
                                            "사이즈",
                                            "M",
                                            Money.zero(),
                                            -1,
                                            CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("재고 수량은 0 이상이어야 합니다");
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
            assertThat(product.status()).isEqualTo(ProductStatus.SOLDOUT);
            assertThat(product.isSoldOut()).isTrue();
            assertThat(product.stockQuantity()).isZero();
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
    @DisplayName("updateStock() - 재고 수량 업데이트")
    class UpdateStockTest {

        @Test
        @DisplayName("재고를 정상적으로 업데이트한다")
        void updateStockSuccessfully() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateStock(50, now);

            // then
            assertThat(product.stockQuantity()).isEqualTo(50);
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("재고를 0으로 업데이트하면 품절 상태로 전환된다")
        void updateStockToZeroTriggersSoldOut() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateStock(0, now);

            // then
            assertThat(product.stockQuantity()).isZero();
            assertThat(product.status()).isEqualTo(ProductStatus.SOLDOUT);
            assertThat(product.isSoldOut()).isTrue();
        }

        @Test
        @DisplayName("음수 재고로 업데이트하면 IllegalArgumentException이 발생한다")
        void throwExceptionForNegativeStock() {
            // given
            var product = ProductFixtures.activeProduct();

            // when & then
            assertThatThrownBy(() -> product.updateStock(-1, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("재고 수량은 0 이상이어야 합니다");
        }

        @Test
        @DisplayName("비활성 상태에서 재고를 0으로 업데이트해도 품절로 전환되지 않는다")
        void updateStockToZeroDoesNotTriggerSoldOutWhenInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateStock(0, now);

            // then
            assertThat(product.stockQuantity()).isZero();
            assertThat(product.status()).isEqualTo(ProductStatus.INACTIVE);
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
            assertThat(product.status()).isEqualTo(ProductStatus.SOLDOUT);
            assertThat(product.stockQuantity()).isZero();
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성 상태에서 품절 처리하면 IllegalStateException이 발생한다")
        void throwExceptionWhenMarkSoldOutFromInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();

            // when & then
            assertThatThrownBy(() -> product.markSoldOut(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("품절 처리할 수 없습니다");
        }

        @Test
        @DisplayName("삭제된 상태에서 품절 처리하면 IllegalStateException이 발생한다")
        void throwExceptionWhenMarkSoldOutFromDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.markSoldOut(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("품절 처리할 수 없습니다");
        }

        @Test
        @DisplayName("이미 품절된 상태에서 품절 처리하면 IllegalStateException이 발생한다")
        void throwExceptionWhenAlreadySoldOut() {
            // given
            var product = ProductFixtures.soldOutProduct();

            // when & then
            assertThatThrownBy(() -> product.markSoldOut(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("품절 처리할 수 없습니다");
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
        @DisplayName("이미 활성 상태에서 활성화하면 IllegalStateException이 발생한다")
        void throwExceptionWhenAlreadyActive() {
            // given
            var product = ProductFixtures.activeProduct();

            // when & then
            assertThatThrownBy(() -> product.activate(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("활성화할 수 없습니다");
        }

        @Test
        @DisplayName("삭제된 상태에서 활성화하면 IllegalStateException이 발생한다")
        void throwExceptionWhenActivateFromDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.activate(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("활성화할 수 없습니다");
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
        @DisplayName("비활성 상태에서 비활성화하면 IllegalStateException이 발생한다")
        void throwExceptionWhenAlreadyInactive() {
            // given
            var product = ProductFixtures.inactiveProduct();

            // when & then
            assertThatThrownBy(() -> product.deactivate(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("비활성화할 수 없습니다");
        }

        @Test
        @DisplayName("품절 상태에서 비활성화하면 IllegalStateException이 발생한다")
        void throwExceptionWhenDeactivateFromSoldOut() {
            // given
            var product = ProductFixtures.soldOutProduct();

            // when & then
            assertThatThrownBy(() -> product.deactivate(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("비활성화할 수 없습니다");
        }

        @Test
        @DisplayName("삭제된 상태에서 비활성화하면 IllegalStateException이 발생한다")
        void throwExceptionWhenDeactivateFromDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.deactivate(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("비활성화할 수 없습니다");
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
        @DisplayName("이미 삭제된 상태에서 삭제하면 IllegalStateException이 발생한다")
        void throwExceptionWhenAlreadyDeleted() {
            // given
            var product = ProductFixtures.deletedProduct();

            // when & then
            assertThatThrownBy(() -> product.delete(CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 삭제된 상품입니다");
        }
    }

    @Nested
    @DisplayName("updateOptions() - 옵션 정보 수정")
    class UpdateOptionsTest {

        @Test
        @DisplayName("옵션 정보를 수정한다")
        void updateOptionsSuccessfully() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateOptions("컬러", "화이트", "사이즈", "L", now);

            // then
            assertThat(product.option1Name()).isEqualTo("컬러");
            assertThat(product.option1Value()).isEqualTo("화이트");
            assertThat(product.option2Name()).isEqualTo("사이즈");
            assertThat(product.option2Value()).isEqualTo("L");
            assertThat(product.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("옵션을 null로 수정할 수 있다")
        void updateOptionsToNull() {
            // given
            var product = ProductFixtures.activeProduct();
            Instant now = CommonVoFixtures.now();

            // when
            product.updateOptions(null, null, null, null, now);

            // then
            assertThat(product.option1Name()).isNull();
            assertThat(product.option1Value()).isNull();
            assertThat(product.option2Name()).isNull();
            assertThat(product.option2Value()).isNull();
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드 테스트")
    class BusinessMethodTest {

        @Test
        @DisplayName("hasStock()은 재고가 있으면 true를 반환한다")
        void hasStockReturnsTrue() {
            // given
            var product = ProductFixtures.productWithStock(10);

            // then
            assertThat(product.hasStock()).isTrue();
        }

        @Test
        @DisplayName("hasStock()은 재고가 0이면 false를 반환한다")
        void hasStockReturnsFalseWhenZero() {
            // given
            var product = ProductFixtures.productWithStock(0);

            // then
            assertThat(product.hasStock()).isFalse();
        }

        @Test
        @DisplayName("isSoldOut()은 SOLDOUT 상태이면 true를 반환한다")
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
        @DisplayName("hasOptions()은 option1Name이 존재하면 true를 반환한다")
        void hasOptionsReturnsTrue() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.hasOptions()).isTrue();
        }

        @Test
        @DisplayName("hasOptions()은 option1Name이 null이면 false를 반환한다")
        void hasOptionsReturnsFalseWhenNull() {
            // given
            var product = ProductFixtures.productWithoutOptions();

            // then
            assertThat(product.hasOptions()).isFalse();
        }

        @Test
        @DisplayName("hasCombinationOptions()은 option1Name과 option2Name이 모두 존재하면 true를 반환한다")
        void hasCombinationOptionsReturnsTrue() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.hasCombinationOptions()).isTrue();
        }

        @Test
        @DisplayName("hasCombinationOptions()은 option2Name이 null이면 false를 반환한다")
        void hasCombinationOptionsReturnsFalseWhenOption2Null() {
            // given
            var product = ProductFixtures.productWithoutOptions();

            // then
            assertThat(product.hasCombinationOptions()).isFalse();
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
        @DisplayName("additionalPrice()는 Money를 반환한다")
        void returnsAdditionalPrice() {
            // given
            var product = ProductFixtures.activeProduct();

            // then
            assertThat(product.additionalPrice()).isNotNull();
            assertThat(product.additionalPriceValue())
                    .isEqualTo(ProductFixtures.DEFAULT_ADDITIONAL_PRICE);
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
