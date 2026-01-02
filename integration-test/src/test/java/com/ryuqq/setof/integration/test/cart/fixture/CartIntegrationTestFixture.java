package com.ryuqq.setof.integration.test.cart.fixture;

import java.math.BigDecimal;

/**
 * Cart Integration Test Fixture
 *
 * <p>장바구니 통합 테스트에서 사용하는 상수 및 예상값을 정의합니다.
 *
 * <p>테스트 데이터는 cart-test-data.sql과 일치해야 합니다.
 *
 * @since 1.0.0
 */
public final class CartIntegrationTestFixture {

    private CartIntegrationTestFixture() {
        // Utility class
    }

    // ============================================================
    // Test Member (cart-test-data.sql과 일치)
    // ============================================================

    /** 장바구니 테스트용 회원 ID */
    public static final String CART_TEST_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9c01";

    public static final String CART_TEST_PHONE = "01011112222";
    public static final String CART_TEST_EMAIL = "carttest@example.com";
    public static final String CART_TEST_PASSWORD = "Password1!";

    /** 장바구니에 아이템이 있는 회원 ID */
    public static final String MEMBER_WITH_CART_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9c02";

    public static final String MEMBER_WITH_CART_PHONE = "01033334444";
    public static final String MEMBER_WITH_CART_EMAIL = "withcart@example.com";
    public static final String MEMBER_WITH_CART_PASSWORD = "Password1!";

    // ============================================================
    // Test Cart Data (cart-test-data.sql과 일치)
    // ============================================================

    /** 기존 장바구니 ID */
    public static final Long EXISTING_CART_ID = 1L;

    /** 기존 장바구니 아이템 ID */
    public static final Long EXISTING_CART_ITEM_ID_1 = 1L;

    public static final Long EXISTING_CART_ITEM_ID_2 = 2L;

    // ============================================================
    // Test Product Data (cart-test-data.sql과 일치)
    // ============================================================

    /** 테스트 상품 그룹 ID */
    public static final Long TEST_PRODUCT_GROUP_ID = 10L;

    /** 테스트 상품 ID */
    public static final Long TEST_PRODUCT_ID_1 = 10L;

    public static final Long TEST_PRODUCT_ID_2 = 11L;

    public static final Long TEST_PRODUCT_ID_3 = 12L;

    /** 테스트 상품 재고 ID */
    public static final Long TEST_PRODUCT_STOCK_ID_1 = 101L;

    public static final Long TEST_PRODUCT_STOCK_ID_2 = 102L;

    public static final Long TEST_PRODUCT_STOCK_ID_3 = 103L;

    /** 테스트 판매자 ID */
    public static final Long TEST_SELLER_ID = 10L;

    /** 테스트 단가 */
    public static final BigDecimal TEST_UNIT_PRICE = BigDecimal.valueOf(29900);

    public static final BigDecimal TEST_UNIT_PRICE_2 = BigDecimal.valueOf(39900);

    // ============================================================
    // Cart Item Defaults
    // ============================================================

    /** 기본 수량 */
    public static final int DEFAULT_QUANTITY = 1;

    /** 최소 수량 */
    public static final int MIN_QUANTITY = 1;

    /** 최대 수량 */
    public static final int MAX_QUANTITY = 99;

    /** 최대 장바구니 아이템 수 */
    public static final int MAX_CART_ITEMS = 100;

    // ============================================================
    // Expected Values
    // ============================================================

    /** 기존 장바구니의 아이템 수 (cart-test-data.sql 기준) */
    public static final int EXISTING_CART_ITEM_COUNT = 2;

    /** 기존 장바구니 아이템 1의 수량 */
    public static final int EXISTING_ITEM_1_QUANTITY = 2;

    /** 기존 장바구니 아이템 2의 수량 */
    public static final int EXISTING_ITEM_2_QUANTITY = 1;

    // ============================================================
    // API Paths
    // ============================================================

    public static final String CART_BASE_PATH = "/members/me/cart";
    public static final String CART_ITEMS_PATH = "/members/me/cart/items";
    public static final String CART_COUNT_PATH = "/members/me/cart/count";
    public static final String CART_CLEAR_PATH = "/members/me/cart/clear";

    /**
     * 수량 변경 경로 생성
     *
     * @param cartItemId 장바구니 아이템 ID
     * @return 수량 변경 API 경로
     */
    public static String quantityPath(Long cartItemId) {
        return CART_ITEMS_PATH + "/" + cartItemId + "/quantity";
    }
}
