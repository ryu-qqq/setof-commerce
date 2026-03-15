package com.ryuqq.setof.application.cart;

import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import java.util.List;

/**
 * Cart Application Command 테스트 Fixtures.
 *
 * <p>장바구니 추가/수정/삭제 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class CartCommandFixtures {

    private CartCommandFixtures() {}

    // ===== 공통 상수 =====

    public static final Long MEMBER_ID = 1L;
    public static final Long USER_ID = 100L;
    public static final Long CART_ID = 1L;
    public static final Long PRODUCT_GROUP_ID = 10L;
    public static final Long PRODUCT_ID = 20L;
    public static final Long SELLER_ID = 5L;
    public static final int QUANTITY = 3;

    // ===== AddCartItemCommand =====

    public static AddCartItemCommand addCommand() {
        return AddCartItemCommand.of(MEMBER_ID, USER_ID, List.of(cartItemDetail()));
    }

    public static AddCartItemCommand addCommand(int quantity) {
        return AddCartItemCommand.of(
                MEMBER_ID,
                USER_ID,
                List.of(cartItemDetail(PRODUCT_GROUP_ID, PRODUCT_ID, quantity)));
    }

    public static AddCartItemCommand addCommandWithMultipleItems() {
        return AddCartItemCommand.of(
                MEMBER_ID,
                USER_ID,
                List.of(cartItemDetail(10L, 20L, 2), cartItemDetail(11L, 21L, 1)));
    }

    public static AddCartItemCommand.CartItemDetail cartItemDetail() {
        return new AddCartItemCommand.CartItemDetail(
                PRODUCT_GROUP_ID, PRODUCT_ID, QUANTITY, SELLER_ID);
    }

    public static AddCartItemCommand.CartItemDetail cartItemDetail(
            long productGroupId, long productId, int quantity) {
        return new AddCartItemCommand.CartItemDetail(
                productGroupId, productId, quantity, SELLER_ID);
    }

    // ===== ModifyCartItemCommand =====

    public static ModifyCartItemCommand modifyCommand() {
        return ModifyCartItemCommand.of(CART_ID, MEMBER_ID, USER_ID, 5);
    }

    public static ModifyCartItemCommand modifyCommand(Long cartId, int newQuantity) {
        return ModifyCartItemCommand.of(cartId, MEMBER_ID, USER_ID, newQuantity);
    }

    // ===== DeleteCartItemsCommand =====

    public static DeleteCartItemsCommand deleteCommand() {
        return DeleteCartItemsCommand.of(List.of(CART_ID), MEMBER_ID, USER_ID);
    }

    public static DeleteCartItemsCommand deleteCommand(List<Long> cartIds) {
        return DeleteCartItemsCommand.of(cartIds, MEMBER_ID, USER_ID);
    }

    public static DeleteCartItemsCommand deleteCommandMultiple() {
        return DeleteCartItemsCommand.of(List.of(1L, 2L, 3L), MEMBER_ID, USER_ID);
    }
}
