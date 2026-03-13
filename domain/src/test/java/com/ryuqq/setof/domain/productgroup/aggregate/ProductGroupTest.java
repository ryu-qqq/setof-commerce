package com.ryuqq.setof.domain.productgroup.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupInvalidOptionStructureException;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupInvalidStatusTransitionException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImages;
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
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
            assertThat(productGroup.optionType()).isEqualTo(OptionType.SINGLE);
        }

        @Test
        @DisplayName("지정 ID를 전달하면 해당 ID로 생성된다 - 레거시 ID 동기화 시나리오")
        void createWithSpecifiedIdSyncsLegacyId() {
            // when
            var productGroup =
                    ProductGroup.forNew(
                            999L,
                            CommonVoFixtures.defaultSellerId(),
                            BrandId.of(ProductGroupFixtures.DEFAULT_BRAND_ID),
                            CategoryId.of(ProductGroupFixtures.DEFAULT_CATEGORY_ID),
                            ShippingPolicyId.of(ProductGroupFixtures.DEFAULT_SHIPPING_POLICY_ID),
                            RefundPolicyId.of(ProductGroupFixtures.DEFAULT_REFUND_POLICY_ID),
                            ProductGroupFixtures.defaultProductGroupName(),
                            OptionType.SINGLE,
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_REGULAR_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_CURRENT_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_SALE_PRICE),
                            CommonVoFixtures.now());

            // then
            assertThat(productGroup.isNew()).isFalse();
            assertThat(productGroup.idValue()).isEqualTo(999L);
        }

        @Test
        @DisplayName("지정 ID를 null로 전달하면 신규 ID로 생성된다")
        void createWithNullIdCreatesNewId() {
            // when
            var productGroup =
                    ProductGroup.forNew(
                            null,
                            CommonVoFixtures.defaultSellerId(),
                            BrandId.of(ProductGroupFixtures.DEFAULT_BRAND_ID),
                            CategoryId.of(ProductGroupFixtures.DEFAULT_CATEGORY_ID),
                            ShippingPolicyId.of(ProductGroupFixtures.DEFAULT_SHIPPING_POLICY_ID),
                            RefundPolicyId.of(ProductGroupFixtures.DEFAULT_REFUND_POLICY_ID),
                            ProductGroupFixtures.defaultProductGroupName(),
                            OptionType.SINGLE,
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_REGULAR_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_CURRENT_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_SALE_PRICE),
                            CommonVoFixtures.now());

            // then
            assertThat(productGroup.isNew()).isTrue();
        }

        @Test
        @DisplayName("신규 생성 시 ACTIVE 상태이다")
        void initialStatusIsActive() {
            // when
            var productGroup = ProductGroupFixtures.newProductGroup();

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
            assertThat(productGroup.isDisplayed()).isTrue();
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
            assertThat(productGroup.sellerOptionGroups()).isEmpty();
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
        @DisplayName("SOLD_OUT 상태의 상품그룹을 복원한다")
        void reconstituteSoldOutProductGroup() {
            // when
            var productGroup = ProductGroupFixtures.soldOutProductGroup();

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.SOLD_OUT);
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
    @DisplayName("changeStatus() - 통합 상태 전이")
    class ChangeStatusTest {

        @Test
        @DisplayName("SOLD_OUT에서 ACTIVE로 전환한다")
        void changeStatusToActive() {
            // given
            var productGroup = ProductGroupFixtures.soldOutProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.changeStatus(ProductGroupStatus.ACTIVE, now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("ACTIVE에서 SOLD_OUT으로 전환한다")
        void changeStatusToSoldOut() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.changeStatus(ProductGroupStatus.SOLD_OUT, now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.SOLD_OUT);
        }

        @Test
        @DisplayName("ACTIVE에서 DELETED로 전환한다")
        void changeStatusToDeleted() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.changeStatus(ProductGroupStatus.DELETED, now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.DELETED);
        }
    }

    @Nested
    @DisplayName("activate() - 상품그룹 활성화")
    class ActivateTest {

        @Test
        @DisplayName("SOLD_OUT 상태에서 활성화할 수 있다")
        void activateFromSoldOut() {
            // given
            var productGroup = ProductGroupFixtures.soldOutProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.activate(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("ACTIVE 상태에서 멱등 활성화가 가능하다")
        void activateFromActiveIdempotent() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();

            // when
            productGroup.activate(now);

            // then
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.ACTIVE);
        }

        @Test
        @DisplayName("DELETED 상태에서 활성화하면 예외가 발생한다")
        void activateFromDeletedThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.deletedProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.activate(now))
                    .isInstanceOf(ProductGroupInvalidStatusTransitionException.class);
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
            assertThat(productGroup.status()).isEqualTo(ProductGroupStatus.SOLD_OUT);
            assertThat(productGroup.isSoldOut()).isTrue();
            assertThat(productGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("SOLD_OUT 상태에서 품절 처리하면 예외가 발생한다")
        void markSoldOutFromSoldOutThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.soldOutProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.markSoldOut(now))
                    .isInstanceOf(ProductGroupInvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("DELETED 상태에서 품절 처리하면 예외가 발생한다")
        void markSoldOutFromDeletedThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.deletedProductGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThatThrownBy(() -> productGroup.markSoldOut(now))
                    .isInstanceOf(ProductGroupInvalidStatusTransitionException.class);
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
        @DisplayName("SOLD_OUT 상태에서 삭제할 수 있다")
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
                    .isInstanceOf(ProductGroupInvalidStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("update() - 기본 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("기본 정보를 수정한다")
        void updateBasicInfo() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            Instant now = CommonVoFixtures.now();
            var updateData =
                    ProductGroupUpdateData.of(
                            ProductGroupId.of(1L),
                            ProductGroupName.of("수정된 상품그룹명"),
                            BrandId.of(99L),
                            CategoryId.of(99L),
                            ShippingPolicyId.of(99L),
                            RefundPolicyId.of(99L),
                            OptionType.COMBINATION,
                            now);

            // when
            productGroup.update(updateData);

            // then
            assertThat(productGroup.productGroupNameValue()).isEqualTo("수정된 상품그룹명");
            assertThat(productGroup.brandIdValue()).isEqualTo(99L);
            assertThat(productGroup.categoryIdValue()).isEqualTo(99L);
            assertThat(productGroup.shippingPolicyIdValue()).isEqualTo(99L);
            assertThat(productGroup.refundPolicyIdValue()).isEqualTo(99L);
            assertThat(productGroup.optionType()).isEqualTo(OptionType.COMBINATION);
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
        @DisplayName("totalOptionValueCount() - 옵션 값 수를 반환한다")
        void totalOptionValueCount() {
            // given
            var productGroup = ProductGroupFixtures.newProductGroup();

            // when & then
            assertThat(productGroup.totalOptionValueCount()).isZero();
        }
    }

    @Nested
    @DisplayName("replaceSellerOptionGroups() - 옵션 구조 검증")
    class ReplaceSellerOptionGroupsTest {

        @Test
        @DisplayName("SINGLE optionType에 그룹 1개를 전달하면 교체에 성공한다")
        void replaceSingleOptionGroupSucceeds() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            SellerOptionGroups singleGroup =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            org.assertj.core.api.Assertions.assertThatCode(
                            () -> productGroup.replaceSellerOptionGroups(singleGroup))
                    .doesNotThrowAnyException();
            assertThat(productGroup.sellerOptionGroups()).hasSize(1);
        }

        @Test
        @DisplayName("SINGLE optionType인데 그룹 2개를 전달하면 예외가 발생한다")
        void replaceSingleOptionGroupWithTwoGroupsThrowsException() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            SellerOptionGroups twoGroups =
                    SellerOptionGroups.of(ProductGroupFixtures.combinationSellerOptionGroups());

            // when & then
            assertThatThrownBy(() -> productGroup.replaceSellerOptionGroups(twoGroups))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("COMBINATION optionType인데 그룹 1개만 전달하면 예외가 발생한다")
        void replaceCombinationOptionGroupWithOneGroupThrowsException() {
            // given
            var productGroup =
                    ProductGroup.reconstitute(
                            ProductGroupId.of(10L),
                            CommonVoFixtures.defaultSellerId(),
                            BrandId.of(ProductGroupFixtures.DEFAULT_BRAND_ID),
                            CategoryId.of(ProductGroupFixtures.DEFAULT_CATEGORY_ID),
                            ShippingPolicyId.of(ProductGroupFixtures.DEFAULT_SHIPPING_POLICY_ID),
                            RefundPolicyId.of(ProductGroupFixtures.DEFAULT_REFUND_POLICY_ID),
                            ProductGroupFixtures.defaultProductGroupName(),
                            OptionType.COMBINATION,
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_REGULAR_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_CURRENT_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_SALE_PRICE),
                            ProductGroupStatus.ACTIVE,
                            ProductGroupFixtures.defaultImages(),
                            List.of(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());
            SellerOptionGroups oneGroup =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            assertThatThrownBy(() -> productGroup.replaceSellerOptionGroups(oneGroup))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("NONE optionType인데 그룹이 존재하면 예외가 발생한다")
        void replaceNoneOptionTypeWithGroupsThrowsException() {
            // given
            var productGroup =
                    ProductGroup.reconstitute(
                            ProductGroupId.of(11L),
                            CommonVoFixtures.defaultSellerId(),
                            BrandId.of(ProductGroupFixtures.DEFAULT_BRAND_ID),
                            CategoryId.of(ProductGroupFixtures.DEFAULT_CATEGORY_ID),
                            ShippingPolicyId.of(ProductGroupFixtures.DEFAULT_SHIPPING_POLICY_ID),
                            RefundPolicyId.of(ProductGroupFixtures.DEFAULT_REFUND_POLICY_ID),
                            ProductGroupFixtures.defaultProductGroupName(),
                            OptionType.NONE,
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_REGULAR_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_CURRENT_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_SALE_PRICE),
                            ProductGroupStatus.ACTIVE,
                            ProductGroupFixtures.defaultImages(),
                            List.of(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());
            SellerOptionGroups groupsWithOne =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            assertThatThrownBy(() -> productGroup.replaceSellerOptionGroups(groupsWithOne))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("NONE optionType에 빈 그룹 목록을 전달하면 교체에 성공한다")
        void replaceNoneOptionTypeWithEmptyGroupsSucceeds() {
            // given
            var productGroup =
                    ProductGroup.reconstitute(
                            ProductGroupId.of(12L),
                            CommonVoFixtures.defaultSellerId(),
                            BrandId.of(ProductGroupFixtures.DEFAULT_BRAND_ID),
                            CategoryId.of(ProductGroupFixtures.DEFAULT_CATEGORY_ID),
                            ShippingPolicyId.of(ProductGroupFixtures.DEFAULT_SHIPPING_POLICY_ID),
                            RefundPolicyId.of(ProductGroupFixtures.DEFAULT_REFUND_POLICY_ID),
                            ProductGroupFixtures.defaultProductGroupName(),
                            OptionType.NONE,
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_REGULAR_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_CURRENT_PRICE),
                            CommonVoFixtures.money(ProductGroupFixtures.DEFAULT_SALE_PRICE),
                            ProductGroupStatus.ACTIVE,
                            ProductGroupFixtures.defaultImages(),
                            List.of(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());
            SellerOptionGroups emptyGroups = SellerOptionGroups.of(List.of());

            // when & then
            org.assertj.core.api.Assertions.assertThatCode(
                            () -> productGroup.replaceSellerOptionGroups(emptyGroups))
                    .doesNotThrowAnyException();
            assertThat(productGroup.sellerOptionGroups()).isEmpty();
        }
    }

    @Nested
    @DisplayName("replaceImages() - 이미지 교체")
    class ReplaceImagesTest {

        @Test
        @DisplayName("이미지를 교체하면 썸네일을 포함한 새 이미지 목록으로 변경된다")
        void replaceImagesIncludesThumbnail() {
            // given
            var productGroup = ProductGroupFixtures.newProductGroup();
            var thumbnail = ProductGroupFixtures.thumbnailImage();
            var detail = ProductGroupFixtures.detailImage();
            ProductGroupImages newImages = ProductGroupImages.of(List.of(thumbnail, detail));

            // when
            productGroup.replaceImages(newImages);

            // then
            assertThat(productGroup.images()).hasSize(2);
            assertThat(productGroup.hasThumbnailImage()).isTrue();
        }

        @Test
        @DisplayName("이미지를 교체하면 이전 이미지는 제거된다")
        void replaceImagesRemovesPreviousImages() {
            // given
            var productGroup = ProductGroupFixtures.activeProductGroup();
            assertThat(productGroup.images()).hasSize(2);

            var newThumbnail = ProductGroupFixtures.thumbnailImage();
            ProductGroupImages newImages = ProductGroupImages.of(List.of(newThumbnail));

            // when
            productGroup.replaceImages(newImages);

            // then
            assertThat(productGroup.images()).hasSize(1);
            assertThat(productGroup.hasThumbnailImage()).isTrue();
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
