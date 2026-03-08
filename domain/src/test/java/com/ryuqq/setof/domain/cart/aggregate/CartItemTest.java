package com.ryuqq.setof.domain.cart.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.setof.commerce.domain.cart.CartFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartItem Aggregate 단위 테스트")
class CartItemTest {

    @Nested
    @DisplayName("forNew() - 신규 CartItem 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 CartItem을 생성하면 ID가 null이다")
        void createNewCartItemIsNew() {
            // when
            CartItem item = CartFixtures.newCartItem();

            // then
            assertThat(item.isNew()).isTrue();
            assertThat(item.idValue()).isNull();
        }

        @Test
        @DisplayName("신규 CartItem은 활성 상태로 생성된다")
        void createNewCartItemIsNotDeleted() {
            // when
            CartItem item = CartFixtures.newCartItem();

            // then
            assertThat(item.isDeleted()).isFalse();
            assertThat(item.deletionStatus()).isNotNull();
            assertThat(item.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 CartItem의 멤버 정보가 올바르게 설정된다")
        void createNewCartItemHasCorrectMemberInfo() {
            // when
            CartItem item = CartFixtures.newCartItem();

            // then
            assertThat(item.memberIdValue()).isEqualTo(CartFixtures.DEFAULT_MEMBER_ID);
            assertThat(item.legacyUserIdValue()).isEqualTo(CartFixtures.DEFAULT_USER_ID);
        }

        @Test
        @DisplayName("신규 CartItem의 상품 정보가 올바르게 설정된다")
        void createNewCartItemHasCorrectProductInfo() {
            // when
            CartItem item = CartFixtures.newCartItem();

            // then
            assertThat(item.productGroupIdValue()).isEqualTo(CartFixtures.DEFAULT_PRODUCT_GROUP_ID);
            assertThat(item.productIdValue()).isEqualTo(CartFixtures.DEFAULT_PRODUCT_ID);
        }

        @Test
        @DisplayName("신규 CartItem의 수량이 올바르게 설정된다")
        void createNewCartItemHasCorrectQuantity() {
            // when
            CartItem item = CartFixtures.newCartItem();

            // then
            assertThat(item.quantityValue()).isEqualTo(CartFixtures.DEFAULT_QUANTITY);
        }

        @Test
        @DisplayName("신규 CartItem의 createdAt과 updatedAt은 동일하다")
        void createNewCartItemHasSameCreatedAndUpdatedAt() {
            // when
            CartItem item = CartFixtures.newCartItem();

            // then
            assertThat(item.createdAt()).isNotNull();
            assertThat(item.updatedAt()).isNotNull();
            assertThat(item.createdAt()).isEqualTo(item.updatedAt());
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 CartItem을 복원하면 isNew가 false이다")
        void reconstituteActiveCartItemIsNotNew() {
            // when
            CartItem item = CartFixtures.activeCartItem();

            // then
            assertThat(item.isNew()).isFalse();
            assertThat(item.idValue()).isEqualTo(CartFixtures.DEFAULT_CART_ITEM_ID);
        }

        @Test
        @DisplayName("활성 CartItem을 복원하면 삭제되지 않은 상태이다")
        void reconstituteActiveCartItemIsNotDeleted() {
            // when
            CartItem item = CartFixtures.activeCartItem();

            // then
            assertThat(item.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 CartItem을 복원하면 삭제 상태이다")
        void reconstituteDeletedCartItemIsDeleted() {
            // when
            CartItem item = CartFixtures.deletedCartItem();

            // then
            assertThat(item.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("update() - UpdateData 기반 수정")
    class UpdateTest {

        @Test
        @DisplayName("UpdateData로 수량과 updatedAt을 수정한다")
        void updateChangesQuantityAndUpdatedAt() {
            // given
            CartItem item = CartFixtures.activeCartItem();
            int originalQuantity = item.quantityValue();
            var updateData = CartFixtures.updateData(10);
            Instant before = item.updatedAt();

            // when
            item.update(updateData);

            // then
            assertThat(item.quantityValue()).isEqualTo(10);
            assertThat(item.quantityValue()).isNotEqualTo(originalQuantity);
            assertThat(item.updatedAt()).isAfterOrEqualTo(before);
        }

        @Test
        @DisplayName("update() 후 updatedAt이 updateData의 occurredAt과 일치한다")
        void updateSetsUpdatedAtToOccurredAt() {
            // given
            CartItem item = CartFixtures.activeCartItem();
            Instant now = CommonVoFixtures.now();
            var updateData =
                    com.ryuqq.setof.domain.cart.vo.CartItemUpdateData.of(CartQuantity.of(7), now);

            // when
            item.update(updateData);

            // then
            assertThat(item.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("changeQuantity() - 수량 직접 변경")
    class ChangeQuantityTest {

        @Test
        @DisplayName("수량을 새 값으로 변경한다")
        void changeQuantitySetsNewValue() {
            // given
            CartItem item = CartFixtures.activeCartItem();
            Instant now = CommonVoFixtures.now();

            // when
            item.changeQuantity(CartQuantity.of(5), now);

            // then
            assertThat(item.quantityValue()).isEqualTo(5);
            assertThat(item.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("increaseQuantity() - 수량 증가 (Upsert 패턴)")
    class IncreaseQuantityTest {

        @Test
        @DisplayName("기존 수량에 추가 수량을 더한다")
        void increaseQuantityAddsToExisting() {
            // given
            CartItem item = CartFixtures.activeCartItem(CartFixtures.DEFAULT_PRODUCT_ID, 3);
            Instant now = CommonVoFixtures.now();

            // when
            item.increaseQuantity(2, now);

            // then
            assertThat(item.quantityValue()).isEqualTo(5);
            assertThat(item.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("수량 증가 후 updatedAt이 갱신된다")
        void increaseQuantityUpdatesUpdatedAt() {
            // given
            CartItem item = CartFixtures.activeCartItem();
            Instant now = CommonVoFixtures.now();

            // when
            item.increaseQuantity(1, now);

            // then
            assertThat(item.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("remove() - Soft Delete")
    class RemoveTest {

        @Test
        @DisplayName("remove()를 호출하면 삭제 상태로 변경된다")
        void removeSetsDeletedState() {
            // given
            CartItem item = CartFixtures.activeCartItem();
            assertThat(item.isDeleted()).isFalse();
            Instant now = CommonVoFixtures.now();

            // when
            item.remove(now);

            // then
            assertThat(item.isDeleted()).isTrue();
            assertThat(item.deletionStatus().deletedAt()).isEqualTo(now);
            assertThat(item.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("isSameProduct() - 동일 상품 확인")
    class IsSameProductTest {

        @Test
        @DisplayName("같은 productGroupId와 productId면 true를 반환한다")
        void isSameProductReturnsTrueForMatchingIds() {
            // given
            CartItem item = CartFixtures.activeCartItem();

            // when & then
            assertThat(
                            item.isSameProduct(
                                    CartFixtures.defaultProductGroupId(),
                                    CartFixtures.defaultProductId()))
                    .isTrue();
        }

        @Test
        @DisplayName("다른 productId면 false를 반환한다")
        void isSameProductReturnsFalseForDifferentProductId() {
            // given
            CartItem item = CartFixtures.activeCartItem();

            // when & then
            assertThat(
                            item.isSameProduct(
                                    CartFixtures.defaultProductGroupId(),
                                    CartFixtures.productId(999L)))
                    .isFalse();
        }

        @Test
        @DisplayName("다른 productGroupId면 false를 반환한다")
        void isSameProductReturnsFalseForDifferentProductGroupId() {
            // given
            CartItem item = CartFixtures.activeCartItem();

            // when & then
            assertThat(
                            item.isSameProduct(
                                    com.ryuqq.setof.domain.productgroup.id.ProductGroupId.of(999L),
                                    CartFixtures.defaultProductId()))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("동등성(equals/hashCode) 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 ID를 가진 CartItem은 동등하다")
        void cartItemsWithSameIdAreEqual() {
            // given
            CartItem item1 = CartFixtures.activeCartItem(1L, CartFixtures.DEFAULT_PRODUCT_ID);
            CartItem item2 = CartFixtures.activeCartItem(1L, 999L); // productId는 달라도 id가 같으면 동등

            // then
            assertThat(item1).isEqualTo(item2);
            assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        }

        @Test
        @DisplayName("다른 ID를 가진 CartItem은 동등하지 않다")
        void cartItemsWithDifferentIdsAreNotEqual() {
            // given
            CartItem item1 = CartFixtures.activeCartItem(1L, CartFixtures.DEFAULT_PRODUCT_ID);
            CartItem item2 = CartFixtures.activeCartItem(2L, CartFixtures.DEFAULT_PRODUCT_ID);

            // then
            assertThat(item1).isNotEqualTo(item2);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("모든 Getter가 올바른 값을 반환한다")
        void allGettersReturnCorrectValues() {
            // when
            CartItem item = CartFixtures.activeCartItem();

            // then
            assertThat(item.id()).isNotNull();
            assertThat(item.memberId()).isNotNull();
            assertThat(item.legacyUserId()).isNotNull();
            assertThat(item.productGroupId()).isNotNull();
            assertThat(item.productId()).isNotNull();
            assertThat(item.quantity()).isNotNull();
            assertThat(item.deletionStatus()).isNotNull();
            assertThat(item.createdAt()).isNotNull();
            assertThat(item.updatedAt()).isNotNull();
        }
    }
}
