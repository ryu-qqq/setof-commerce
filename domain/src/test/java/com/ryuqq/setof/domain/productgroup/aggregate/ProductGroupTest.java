package com.ryuqq.setof.domain.productgroup.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroup Aggregate 테스트")
class ProductGroupTest {

    @Nested
    @DisplayName("forNew() - 신규 상품그룹 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 상품그룹을 생성한다")
        void createNewProductGroup() {
            // when
            var productGroup = ProductGroupFixtures.newProductGroup();

            // then
            assertThat(productGroup.isNew()).isTrue();
            assertThat(productGroup.productGroupNameValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_PRODUCT_GROUP_NAME);
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DRAFT);
            assertThat(productGroup.optionType()).isEqualTo(OptionType.SINGLE);
        }

        @Test
        @DisplayName("신규 생성 시 DRAFT 상태이다")
        void initialStatusIsDraft() {
            // when
            var productGroup = ProductGroupFixtures.newProductGroup();

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DRAFT);
            assertThat(productGroup.isDisplayed()).isFalse();
            assertThat(productGroup.isSoldOut()).isFalse();
            assertThat(productGroup.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 시 모든 필드가 설정된다")
        void allFieldsAreSet() {
            // when
            var productGroup = ProductGroupFixtures.newProductGroup();

            // then
            assertThat(productGroup.sellerId()).isNotNull();
            assertThat(productGroup.sellerIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_SELLER_ID);
            assertThat(productGroup.brandId()).isNotNull();
            assertThat(productGroup.brandIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_BRAND_ID);
            assertThat(productGroup.categoryId()).isNotNull();
            assertThat(productGroup.categoryIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_CATEGORY_ID);
            assertThat(productGroup.shippingPolicyId()).isNotNull();
            assertThat(productGroup.shippingPolicyIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_SHIPPING_POLICY_ID);
            assertThat(productGroup.refundPolicyId()).isNotNull();
            assertThat(productGroup.refundPolicyIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_REFUND_POLICY_ID);
            assertThat(productGroup.regularPriceValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_REGULAR_PRICE);
            assertThat(productGroup.currentPriceValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_CURRENT_PRICE);
            assertThat(productGroup.salePriceValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_SALE_PRICE);
            assertThat(productGroup.images()).isEmpty();
            assertThat(productGroup.createdAt()).isNotNull();
            assertThat(productGroup.updatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("ACTIVE 상태의 상품그룹을 복원한다")
        void reconstituteActiveProductGroup() {
            // when
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // then
            assertThat(productGroup.isNew()).isFalse();
            assertThat(productGroup.idValue()).isEqualTo(1L);
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
            assertThat(productGroup.isDisplayed()).isTrue();
        }

        @Test
        @DisplayName("특정 ID로 ACTIVE 상태의 상품그룹을 복원한다")
        void reconstituteActiveProductGroupWithId() {
            // when
            var productGroup = ProductGroupFixtures.activeProductGroup(99L);

            // then
            assertThat(productGroup.idValue()).isEqualTo(99L);
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
        }

        @Test
        @DisplayName("INACTIVE 상태의 상품그룹을 복원한다")
        void reconstituteInactiveProductGroup() {
            // when
            var productGroup = ProductGroupFixtures.inactiveProductGroup();

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.INACTIVE);
            assertThat(productGroup.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("SOLDOUT 상태의 상품그룹을 복원한다")
        void reconstituteSoldOutProductGroup() {
            // when
            var productGroup = ProductGroupFixtures.soldOutProductGroup();

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.SOLDOUT);
            assertThat(productGroup.isSoldOut()).isTrue();
        }

        @Test
        @DisplayName("DELETED 상태의 상품그룹을 복원한다")
        void reconstituteDeletedProductGroup() {
            // when
            var productGroup = ProductGroupFixtures.deletedProductGroup();

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DELETED);
            assertThat(productGroup.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("DRAFT 상태의 상품그룹을 복원한다")
        void reconstituteDraftProductGroup() {
            // when
            var productGroup = ProductGroupFixtures.draftProductGroup();

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DRAFT);
        }

        @Test
        @DisplayName("복원된 상품그룹은 이미지를 포함한다")
        void reconstituteWithImages() {
            // when
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // then
            assertThat(productGroup.images()).hasSize(2);
            assertThat(productGroup.hasThumbnailImage()).isTrue();
        }
    }

    @Nested
    @DisplayName("activate() - 상품그룹 활성화")
    class ActivateTest {

        @Test
        @DisplayName("DRAFT 상태에서 활성화할 수 있다")
        void activateFromDraft() {
            // given
            var productGroup = ProductGroupFixtures.draftProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.activate(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("INACTIVE 상태에서 재활성화할 수 있다")
        void activateFromInactive() {
            // given
            var productGroup = ProductGroupFixtures.inactiveProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.activate(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("SOLDOUT 상태에서 재활성화할 수 있다")
        void activateFromSoldOut() {
            // given
            var productGroup = ProductGroupFixtures.soldOutProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.activate(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
        }

        @Test
        @DisplayName("ACTIVE 상태에서 활성화하면 예외가 발생한다")
        void activateFromActiveThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.activate(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("활성화할 수 없습니다");
        }

        @Test
        @DisplayName("DELETED 상태에서 활성화하면 예외가 발생한다")
        void activateFromDeletedThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.deletedProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.activate(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("활성화할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("deactivate() - 상품그룹 비활성화")
    class DeactivateTest {

        @Test
        @DisplayName("ACTIVE 상태에서 비활성화할 수 있다")
        void deactivateFromActive() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.deactivate(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.INACTIVE);
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("DRAFT 상태에서 비활성화하면 예외가 발생한다")
        void deactivateFromDraftThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.draftProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.deactivate(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("비활성화할 수 없습니다");
        }

        @Test
        @DisplayName("INACTIVE 상태에서 비활성화하면 예외가 발생한다")
        void deactivateFromInactiveThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.inactiveProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.deactivate(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("비활성화할 수 없습니다");
        }

        @Test
        @DisplayName("DELETED 상태에서 비활성화하면 예외가 발생한다")
        void deactivateFromDeletedThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.deletedProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.deactivate(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("비활성화할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("markSoldOut() - 품절 처리")
    class MarkSoldOutTest {

        @Test
        @DisplayName("ACTIVE 상태에서 품절 처리할 수 있다")
        void markSoldOutFromActive() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.markSoldOut(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.SOLDOUT);
            assertThat(productGroup.isSoldOut()).isTrue();
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("DRAFT 상태에서 품절 처리하면 예외가 발생한다")
        void markSoldOutFromDraftThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.draftProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.markSoldOut(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("품절 처리할 수 없습니다");
        }

        @Test
        @DisplayName("INACTIVE 상태에서 품절 처리하면 예외가 발생한다")
        void markSoldOutFromInactiveThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.inactiveProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.markSoldOut(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("품절 처리할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("delete() - 삭제 처리")
    class DeleteTest {

        @Test
        @DisplayName("ACTIVE 상태에서 삭제할 수 있다")
        void deleteFromActive() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.delete(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DELETED);
            assertThat(productGroup.isDeleted()).isTrue();
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("DRAFT 상태에서 삭제할 수 있다")
        void deleteFromDraft() {
            // given
            var productGroup = ProductGroupFixtures.draftProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.delete(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DELETED);
        }

        @Test
        @DisplayName("INACTIVE 상태에서 삭제할 수 있다")
        void deleteFromInactive() {
            // given
            var productGroup = ProductGroupFixtures.inactiveProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.delete(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DELETED);
        }

        @Test
        @DisplayName("SOLDOUT 상태에서 삭제할 수 있다")
        void deleteFromSoldOut() {
            // given
            var productGroup = ProductGroupFixtures.soldOutProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.delete(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DELETED);
        }

        @Test
        @DisplayName("이미 삭제된 상품그룹을 삭제하면 예외가 발생한다")
        void deleteAlreadyDeletedThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.deletedProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.delete(now))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 삭제된 상품그룹입니다");
        }
    }

    @Nested
    @DisplayName("updateBasicInfo() - 기본 정보 수정")
    class UpdateBasicInfoTest {

        @Test
        @DisplayName("기본 정보를 수정한다")
        void updateBasicInfo() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            ProductGroupName newName = ProductGroupName.of("수정된 상품그룹명");
            BrandId newBrandId = BrandId.of(99L);
            CategoryId newCategoryId = CategoryId.of(99L);
            ShippingPolicyId newShippingPolicyId = ShippingPolicyId.of(99L);
            RefundPolicyId newRefundPolicyId = RefundPolicyId.of(99L);
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.updateBasicInfo(
                    newName,
                    newBrandId,
                    newCategoryId,
                    newShippingPolicyId,
                    newRefundPolicyId,
                    now);

            // then
            assertThat(productGroup.productGroupNameValue()).isEqualTo("수정된 상품그룹명");
            assertThat(productGroup.brandIdValue()).isEqualTo(99L);
            assertThat(productGroup.categoryIdValue()).isEqualTo(99L);
            assertThat(productGroup.shippingPolicyIdValue()).isEqualTo(99L);
            assertThat(productGroup.refundPolicyIdValue()).isEqualTo(99L);
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("updatePrices() - 가격 수정")
    class UpdatePricesTest {

        @Test
        @DisplayName("가격 정보를 수정한다")
        void updatePrices() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            var newRegularPrice = CommonVoFixtures.money(100000);
            var newCurrentPrice = CommonVoFixtures.money(90000);
            var newSalePrice = CommonVoFixtures.money(80000);
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.updatePrices(newRegularPrice, newCurrentPrice, newSalePrice, now);

            // then
            assertThat(productGroup.regularPriceValue()).isEqualTo(100000);
            assertThat(productGroup.currentPriceValue()).isEqualTo(90000);
            assertThat(productGroup.salePriceValue()).isEqualTo(80000);
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("replaceImages() - 이미지 교체")
    class ReplaceImagesTest {

        @Test
        @DisplayName("이미지를 교체한다")
        void replaceImages() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            var newImage =
                    ProductGroupImage.forNew(
                            ImageType.THUMBNAIL, ImageUrl.of("https://new-image.com/img.png"), 1);
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.replaceImages(List.of(newImage), now);

            // then
            assertThat(productGroup.images()).hasSize(1);
            assertThat(productGroup.images().get(0).imageUrlValue())
                    .isEqualTo("https://new-image.com/img.png");
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("null로 교체하면 이미지가 비워진다")
        void replaceWithNullClearsImages() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.replaceImages(null, now);

            // then
            assertThat(productGroup.images()).isEmpty();
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("빈 목록으로 교체하면 이미지가 비워진다")
        void replaceWithEmptyListClearsImages() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.replaceImages(List.of(), now);

            // then
            assertThat(productGroup.images()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Query Methods 테스트")
    class QueryMethodTest {

        @Test
        @DisplayName("hasThumbnailImage() - 썸네일 이미지가 존재하면 true를 반환한다")
        void hasThumbnailImageReturnsTrue() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when & then
            assertThat(productGroup.hasThumbnailImage()).isTrue();
        }

        @Test
        @DisplayName("hasThumbnailImage() - 썸네일 이미지가 없으면 false를 반환한다")
        void hasThumbnailImageReturnsFalse() {
            // given
            var productGroup = ProductGroupFixtures.newProductGroup();

            // when & then
            assertThat(productGroup.hasThumbnailImage()).isFalse();
        }

        @Test
        @DisplayName("discountRate() - 할인율을 계산한다")
        void discountRateCalculation() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when
            int discountRate = productGroup.discountRate();

            // then
            // regularPrice=50000, salePrice=40000 (isOnSale true), (1 - 40000/50000) * 100 = 20
            assertThat(discountRate).isEqualTo(20);
        }

        @Test
        @DisplayName("discountRate() - 정상가가 0이면 할인율은 0이다")
        void discountRateWhenRegularPriceIsZero() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();
            productGroup.updatePrices(
                    CommonVoFixtures.zeroMoney(),
                    CommonVoFixtures.zeroMoney(),
                    CommonVoFixtures.zeroMoney(),
                    now);

            // when
            int discountRate = productGroup.discountRate();

            // then
            assertThat(discountRate).isZero();
        }

        @Test
        @DisplayName("effectivePrice() - 세일 중이면 salePrice를 반환한다")
        void effectivePriceReturnsSalePriceOnSale() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when
            var effectivePrice = productGroup.effectivePrice();

            // then
            assertThat(effectivePrice.value()).isEqualTo(ProductGroupFixtures.DEFAULT_SALE_PRICE);
        }

        @Test
        @DisplayName("effectivePrice() - 세일 중이 아니면 currentPrice를 반환한다")
        void effectivePriceReturnsCurrentPriceNotOnSale() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();
            // salePrice를 currentPrice보다 크게 설정 -> 세일 아님
            productGroup.updatePrices(
                    CommonVoFixtures.money(50000),
                    CommonVoFixtures.money(45000),
                    CommonVoFixtures.money(50000),
                    now);

            // when
            var effectivePrice = productGroup.effectivePrice();

            // then
            assertThat(effectivePrice.value()).isEqualTo(45000);
        }

        @Test
        @DisplayName("isOnSale() - salePrice가 currentPrice보다 작으면 true를 반환한다")
        void isOnSaleReturnsTrue() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when & then
            assertThat(productGroup.isOnSale()).isTrue();
        }

        @Test
        @DisplayName("isOnSale() - salePrice가 0이면 false를 반환한다")
        void isOnSaleReturnsFalseWhenSalePriceIsZero() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();
            productGroup.updatePrices(
                    CommonVoFixtures.money(50000),
                    CommonVoFixtures.money(45000),
                    CommonVoFixtures.zeroMoney(),
                    now);

            // when & then
            assertThat(productGroup.isOnSale()).isFalse();
        }

        @Test
        @DisplayName("isSoldOut() - SOLDOUT 상태이면 true를 반환한다")
        void isSoldOutReturnsTrue() {
            // given
            var productGroup = ProductGroupFixtures.soldOutProductGroup();

            // when & then
            assertThat(productGroup.isSoldOut()).isTrue();
        }

        @Test
        @DisplayName("isSoldOut() - SOLDOUT 상태가 아니면 false를 반환한다")
        void isSoldOutReturnsFalse() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when & then
            assertThat(productGroup.isSoldOut()).isFalse();
        }

        @Test
        @DisplayName("isDisplayed() - ACTIVE 상태이면 true를 반환한다")
        void isDisplayedReturnsTrue() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when & then
            assertThat(productGroup.isDisplayed()).isTrue();
        }

        @Test
        @DisplayName("isDisplayed() - ACTIVE 상태가 아니면 false를 반환한다")
        void isDisplayedReturnsFalse() {
            // given
            var productGroup = ProductGroupFixtures.inactiveProductGroup();

            // when & then
            assertThat(productGroup.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("isDeleted() - DELETED 상태이면 true를 반환한다")
        void isDeletedReturnsTrue() {
            // given
            var productGroup = ProductGroupFixtures.deletedProductGroup();

            // when & then
            assertThat(productGroup.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("isDeleted() - DELETED 상태가 아니면 false를 반환한다")
        void isDeletedReturnsFalse() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when & then
            assertThat(productGroup.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("Accessor Methods 테스트")
    class AccessorMethodTest {

        @Test
        @DisplayName("id()는 ProductGroupId를 반환한다")
        void returnsId() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.id()).isNotNull();
            assertThat(productGroup.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("sellerId()는 SellerId를 반환한다")
        void returnsSellerId() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.sellerId()).isNotNull();
            assertThat(productGroup.sellerIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_SELLER_ID);
        }

        @Test
        @DisplayName("brandId()는 BrandId를 반환한다")
        void returnsBrandId() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.brandId()).isNotNull();
            assertThat(productGroup.brandIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_BRAND_ID);
        }

        @Test
        @DisplayName("categoryId()는 CategoryId를 반환한다")
        void returnsCategoryId() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.categoryId()).isNotNull();
            assertThat(productGroup.categoryIdValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_CATEGORY_ID);
        }

        @Test
        @DisplayName("productGroupName()은 ProductGroupName을 반환한다")
        void returnsProductGroupName() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.productGroupName()).isNotNull();
            assertThat(productGroup.productGroupNameValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_PRODUCT_GROUP_NAME);
        }

        @Test
        @DisplayName("optionType()은 OptionType을 반환한다")
        void returnsOptionType() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.optionType()).isEqualTo(OptionType.SINGLE);
        }

        @Test
        @DisplayName("regularPrice()는 Money를 반환한다")
        void returnsRegularPrice() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.regularPrice()).isNotNull();
            assertThat(productGroup.regularPriceValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_REGULAR_PRICE);
        }

        @Test
        @DisplayName("currentPrice()는 Money를 반환한다")
        void returnsCurrentPrice() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.currentPrice()).isNotNull();
            assertThat(productGroup.currentPriceValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_CURRENT_PRICE);
        }

        @Test
        @DisplayName("salePrice()는 Money를 반환한다")
        void returnsSalePrice() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.salePrice()).isNotNull();
            assertThat(productGroup.salePriceValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_SALE_PRICE);
        }

        @Test
        @DisplayName("images()는 불변 리스트를 반환한다")
        void returnsUnmodifiableImages() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();

            // when & then
            assertThatThrownBy(
                            () -> productGroup.images().add(ProductGroupFixtures.thumbnailImage()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("createdAt()은 생성 시각을 반환한다")
        void returnsCreatedAt() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("updatedAt()은 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.updatedAt()).isNotNull();
        }
    }
}
