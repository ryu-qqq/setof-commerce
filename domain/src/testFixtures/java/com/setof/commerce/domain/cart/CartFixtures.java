package com.setof.commerce.domain.cart;

import com.ryuqq.setof.domain.cart.Cart;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.cart.query.CartSortKey;
import com.ryuqq.setof.domain.cart.vo.CartItemUpdateData;
import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.List;

/**
 * Cart 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Cart 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class CartFixtures {

    private CartFixtures() {}

    // ===== 상수 =====
    public static final Long DEFAULT_MEMBER_ID = 1L;
    public static final Long DEFAULT_USER_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final Long DEFAULT_PRODUCT_ID = 200L;
    public static final Long DEFAULT_CART_ITEM_ID = 1L;
    public static final int DEFAULT_QUANTITY = 2;
    public static final Long DEFAULT_SELLER_ID = 22L;

    // ===== MemberId Fixtures =====
    public static MemberId defaultMemberId() {
        return MemberId.of(DEFAULT_MEMBER_ID);
    }

    public static MemberId memberId(Long value) {
        return MemberId.of(value);
    }

    // ===== LegacyUserId Fixtures =====
    public static LegacyUserId defaultLegacyUserId() {
        return LegacyUserId.of(DEFAULT_USER_ID);
    }

    public static LegacyUserId legacyUserId(long value) {
        return LegacyUserId.of(value);
    }

    // ===== CartQuantity Fixtures =====
    public static CartQuantity defaultQuantity() {
        return CartQuantity.of(DEFAULT_QUANTITY);
    }

    public static CartQuantity quantity(int value) {
        return CartQuantity.of(value);
    }

    public static CartQuantity minQuantity() {
        return CartQuantity.of(1);
    }

    public static CartQuantity maxQuantity() {
        return CartQuantity.of(999);
    }

    // ===== CartItemId Fixtures =====
    public static CartItemId defaultCartItemId() {
        return CartItemId.of(DEFAULT_CART_ITEM_ID);
    }

    public static CartItemId cartItemId(Long value) {
        return CartItemId.of(value);
    }

    public static CartItemId newCartItemId() {
        return CartItemId.forNew();
    }

    // ===== ProductGroupId / ProductId Fixtures =====
    public static ProductGroupId defaultProductGroupId() {
        return ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID);
    }

    public static ProductId defaultProductId() {
        return ProductId.of(DEFAULT_PRODUCT_ID);
    }

    public static ProductId productId(Long value) {
        return ProductId.of(value);
    }

    // ===== SellerId Fixtures =====
    public static SellerId defaultSellerId() {
        return SellerId.of(DEFAULT_SELLER_ID);
    }

    // ===== CartItem Aggregate Fixtures =====

    /** 신규 CartItem (ID null, 활성 상태) */
    public static CartItem newCartItem() {
        return CartItem.forNew(
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultProductGroupId(),
                defaultProductId(),
                defaultSellerId(),
                defaultQuantity(),
                CommonVoFixtures.now());
    }

    /** 신규 CartItem (지정 productId) */
    public static CartItem newCartItem(Long productId) {
        return CartItem.forNew(
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultProductGroupId(),
                ProductId.of(productId),
                defaultSellerId(),
                defaultQuantity(),
                CommonVoFixtures.now());
    }

    /** 신규 CartItem (지정 productId + quantity) */
    public static CartItem newCartItem(Long productId, int quantity) {
        return CartItem.forNew(
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultProductGroupId(),
                ProductId.of(productId),
                defaultSellerId(),
                CartQuantity.of(quantity),
                CommonVoFixtures.now());
    }

    /** 활성 상태 CartItem (영속성 복원) */
    public static CartItem activeCartItem() {
        return CartItem.reconstitute(
                defaultCartItemId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultProductGroupId(),
                defaultProductId(),
                defaultSellerId(),
                defaultQuantity(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 활성 상태 CartItem (지정 ID + productId) */
    public static CartItem activeCartItem(Long id, Long productId) {
        return CartItem.reconstitute(
                CartItemId.of(id),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultProductGroupId(),
                ProductId.of(productId),
                defaultSellerId(),
                defaultQuantity(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 활성 상태 CartItem (지정 productId + quantity) */
    public static CartItem activeCartItem(Long productId, int quantity) {
        return CartItem.reconstitute(
                defaultCartItemId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultProductGroupId(),
                ProductId.of(productId),
                defaultSellerId(),
                CartQuantity.of(quantity),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 삭제된 CartItem (Soft Delete 상태) */
    public static CartItem deletedCartItem() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return CartItem.reconstitute(
                CartItemId.of(2L),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultProductGroupId(),
                defaultProductId(),
                defaultSellerId(),
                defaultQuantity(),
                DeletionStatus.deletedAt(deletedAt),
                CommonVoFixtures.yesterday(),
                deletedAt);
    }

    // ===== CartItemUpdateData Fixtures =====
    public static CartItemUpdateData defaultUpdateData() {
        return CartItemUpdateData.of(CartQuantity.of(5), CommonVoFixtures.now());
    }

    public static CartItemUpdateData updateData(int quantity) {
        return CartItemUpdateData.of(CartQuantity.of(quantity), CommonVoFixtures.now());
    }

    // ===== Cart 도메인 객체 Fixtures =====

    /** 기본 Cart (단일 아이템) */
    public static Cart defaultCart() {
        return Cart.of(
                DEFAULT_MEMBER_ID, DEFAULT_USER_ID, CommonVoFixtures.now(), List.of(newCartItem()));
    }

    /** Cart (지정 아이템 목록) */
    public static Cart cartWith(List<CartItem> items) {
        return Cart.of(DEFAULT_MEMBER_ID, DEFAULT_USER_ID, CommonVoFixtures.now(), items);
    }

    /** Cart (지정 productId 목록으로 단일 수량 아이템들) */
    public static Cart cartWithProductIds(List<Long> productIds) {
        List<CartItem> items = productIds.stream().map(CartFixtures::newCartItem).toList();
        return Cart.of(DEFAULT_MEMBER_ID, DEFAULT_USER_ID, CommonVoFixtures.now(), items);
    }

    // ===== CartSearchCriteria Fixtures =====
    public static CartSearchCriteria defaultSearchCriteria() {
        return CartSearchCriteria.of(
                DEFAULT_MEMBER_ID,
                DEFAULT_USER_ID,
                CursorQueryContext.defaultOf(CartSortKey.defaultKey()));
    }

    public static CartSearchCriteria searchCriteriaWithCursor(Long cursor) {
        return CartSearchCriteria.of(
                DEFAULT_MEMBER_ID,
                DEFAULT_USER_ID,
                CursorQueryContext.of(
                        CartSortKey.CREATED_AT,
                        com.ryuqq.setof.domain.common.vo.SortDirection.DESC,
                        com.ryuqq.setof.domain.common.vo.CursorPageRequest.afterId(cursor, 20)));
    }
}
