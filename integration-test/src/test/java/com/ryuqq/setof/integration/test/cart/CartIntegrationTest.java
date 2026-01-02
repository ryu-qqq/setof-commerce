package com.ryuqq.setof.integration.test.cart;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.AddCartItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.RemoveCartItemsV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateAllSelectedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateCartItemQuantityV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateCartItemSelectedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.response.CartItemV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.response.CartV2ApiResponse;
import com.ryuqq.setof.integration.test.cart.fixture.CartIntegrationTestFixture;
import com.ryuqq.setof.integration.test.common.IntegrationTestBase;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Cart Integration Test
 *
 * <p>장바구니 V2 API의 통합 테스트를 수행합니다.
 *
 * <h3>테스트 시나리오</h3>
 *
 * <ul>
 *   <li>장바구니 조회 (빈 장바구니, 아이템 있는 장바구니)
 *   <li>장바구니 아이템 추가
 *   <li>장바구니 아이템 수량 변경
 *   <li>장바구니 아이템 선택 상태 변경
 *   <li>장바구니 아이템 삭제
 *   <li>장바구니 비우기
 *   <li>인증 실패 케이스
 * </ul>
 *
 * <p>테스트 데이터는 cart-test-data.sql에서 상품/판매자 정보를 로드하고, 회원과 장바구니 아이템은 API를 통해 동적으로 생성합니다.
 *
 * @since 1.0.0
 */
@DisplayName("Cart Integration Test")
@Sql(
        scripts = "classpath:sql/cart/cart-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CartIntegrationTest extends IntegrationTestBase {

    private static final String CART_API_PATH = "/members/me/cart";

    // ============================================================
    // 장바구니 조회 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 조회")
    class GetCartTest {

        @Test
        @DisplayName("새 회원의 장바구니 조회 시 빈 장바구니를 반환한다")
        void shouldReturnEmptyCartForNewMember() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("cart");
            String url = apiV2Url(CART_API_PATH);

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CartV2ApiResponse cart = response.getBody().data();
            assertThat(cart).isNotNull();
            assertThat(cart.items()).isEmpty();
        }

        @Test
        @DisplayName("아이템이 있는 장바구니 조회 시 모든 아이템을 반환한다")
        void shouldReturnCartWithItems() {
            // given - 회원 등록 후 장바구니에 아이템 추가
            String accessToken = registerUniqueAndGetAccessToken("cartWithItems");
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_2);

            String url = apiV2Url(CART_API_PATH);

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CartV2ApiResponse cart = response.getBody().data();
            assertThat(cart).isNotNull();
            assertThat(cart.items()).hasSize(2);
        }

        @Test
        @Disabled(
                "통합 테스트 환경에서 모든 엔드포인트가 public으로 설정되어 있음 (application-test.yml: public-endpoints:"
                        + " /**)")
        @DisplayName("인증 없이 장바구니 조회 시 401 에러를 반환한다")
        void shouldReturn401WhenNoAuthentication() {
            // given
            String url = apiV2Url(CART_API_PATH);

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, jsonEntity(null), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    // ============================================================
    // 장바구니 아이템 개수 조회 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 아이템 개수 조회")
    class GetItemCountTest {

        @Test
        @DisplayName("빈 장바구니의 아이템 개수는 0이다")
        void shouldReturnZeroForEmptyCart() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("count");
            String url = apiV2Url(CART_API_PATH + "/count");

            // when
            ResponseEntity<ApiResponse<Integer>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            Integer count = response.getBody().data();
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("아이템이 있는 장바구니의 개수를 정확히 반환한다")
        void shouldReturnCorrectItemCount() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("countWithItems");
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_2);

            String url = apiV2Url(CART_API_PATH + "/count");

            // when
            ResponseEntity<ApiResponse<Integer>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Integer count = response.getBody().data();
            assertThat(count).isEqualTo(2);
        }
    }

    // ============================================================
    // 장바구니 아이템 추가 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 아이템 추가")
    class AddItemTest {

        @Test
        @DisplayName("상품을 장바구니에 추가할 수 있다")
        void shouldAddItemToCart() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("add");

            AddCartItemV2ApiRequest request =
                    new AddCartItemV2ApiRequest(
                            CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_GROUP_ID,
                            CartIntegrationTestFixture.TEST_SELLER_ID,
                            CartIntegrationTestFixture.DEFAULT_QUANTITY,
                            CartIntegrationTestFixture.TEST_UNIT_PRICE);

            String url = apiV2Url(CART_API_PATH + "/items");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CartV2ApiResponse cart = response.getBody().data();
            assertThat(cart).isNotNull();
            assertThat(cart.items()).isNotEmpty();
        }

        @Test
        @DisplayName("이미 존재하는 상품 추가 시 수량이 증가한다")
        void shouldIncreaseQuantityWhenAddingExistingItem() {
            // given - 아이템을 먼저 추가
            String accessToken = registerUniqueAndGetAccessToken("addExisting");
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);

            // 동일한 상품 다시 추가
            AddCartItemV2ApiRequest request =
                    new AddCartItemV2ApiRequest(
                            CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_GROUP_ID,
                            CartIntegrationTestFixture.TEST_SELLER_ID,
                            CartIntegrationTestFixture.DEFAULT_QUANTITY,
                            CartIntegrationTestFixture.TEST_UNIT_PRICE);

            String url = apiV2Url(CART_API_PATH + "/items");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            CartV2ApiResponse cart = response.getBody().data();
            // 기존 수량(1) + 추가 수량(1) = 2
            CartItemV2ApiResponse item =
                    cart.items().stream()
                            .filter(
                                    i ->
                                            i.productStockId()
                                                    .equals(
                                                            CartIntegrationTestFixture
                                                                    .TEST_PRODUCT_STOCK_ID_1))
                            .findFirst()
                            .orElseThrow();
            assertThat(item.quantity()).isEqualTo(2);
        }

        @Test
        @DisplayName("수량이 0 이하일 때 400 에러를 반환한다")
        void shouldReturn400WhenQuantityIsZeroOrNegative() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("addZero");

            AddCartItemV2ApiRequest request =
                    new AddCartItemV2ApiRequest(
                            CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_GROUP_ID,
                            CartIntegrationTestFixture.TEST_SELLER_ID,
                            0, // 잘못된 수량
                            CartIntegrationTestFixture.TEST_UNIT_PRICE);

            String url = apiV2Url(CART_API_PATH + "/items");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @Disabled(
                "통합 테스트 환경에서 모든 엔드포인트가 public으로 설정되어 있음 (application-test.yml: public-endpoints:"
                        + " /**)")
        @DisplayName("인증 없이 아이템 추가 시 401 에러를 반환한다")
        void shouldReturn401WhenNoAuthentication() {
            // given
            AddCartItemV2ApiRequest request =
                    new AddCartItemV2ApiRequest(
                            CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_ID_1,
                            CartIntegrationTestFixture.TEST_PRODUCT_GROUP_ID,
                            CartIntegrationTestFixture.TEST_SELLER_ID,
                            CartIntegrationTestFixture.DEFAULT_QUANTITY,
                            CartIntegrationTestFixture.TEST_UNIT_PRICE);

            String url = apiV2Url(CART_API_PATH + "/items");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, jsonEntity(request), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    // ============================================================
    // 장바구니 아이템 수량 변경 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 아이템 수량 변경")
    class UpdateQuantityTest {

        @Test
        @DisplayName("아이템 수량을 변경할 수 있다")
        void shouldUpdateItemQuantity() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("updateQty");
            Long cartItemId =
                    addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);

            int newQuantity = 5;
            UpdateCartItemQuantityV2ApiRequest request =
                    new UpdateCartItemQuantityV2ApiRequest(newQuantity);

            String url = apiV2Url(CART_API_PATH + "/items/" + cartItemId + "/quantity");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CartV2ApiResponse cart = response.getBody().data();
            CartItemV2ApiResponse item =
                    cart.items().stream()
                            .filter(i -> i.cartItemId().equals(cartItemId))
                            .findFirst()
                            .orElseThrow();
            assertThat(item.quantity()).isEqualTo(newQuantity);
        }

        @Test
        @DisplayName("수량이 최대값(99)을 초과하면 400 에러를 반환한다")
        void shouldReturn400WhenQuantityExceedsMax() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("updateMax");
            Long cartItemId =
                    addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);

            UpdateCartItemQuantityV2ApiRequest request =
                    new UpdateCartItemQuantityV2ApiRequest(
                            CartIntegrationTestFixture.MAX_QUANTITY + 1);

            String url = apiV2Url(CART_API_PATH + "/items/" + cartItemId + "/quantity");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("존재하지 않는 아이템 수량 변경 시 404 에러를 반환한다")
        void shouldReturn404WhenItemNotFound() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("updateNotFound");

            UpdateCartItemQuantityV2ApiRequest request = new UpdateCartItemQuantityV2ApiRequest(5);
            Long nonExistentItemId = 99999L;

            String url = apiV2Url(CART_API_PATH + "/items/" + nonExistentItemId + "/quantity");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // 장바구니 아이템 선택 상태 변경 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 아이템 선택 상태 변경")
    class UpdateSelectedTest {

        @Test
        @DisplayName("특정 아이템들의 선택 상태를 변경할 수 있다")
        void shouldUpdateItemsSelected() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("updateSelected");
            Long cartItemId1 =
                    addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);
            Long cartItemId2 =
                    addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_2);

            List<Long> cartItemIds = List.of(cartItemId1, cartItemId2);
            UpdateCartItemSelectedV2ApiRequest request =
                    new UpdateCartItemSelectedV2ApiRequest(cartItemIds, false);

            String url = apiV2Url(CART_API_PATH + "/items/selected");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("모든 아이템의 선택 상태를 일괄 변경할 수 있다")
        void shouldUpdateAllItemsSelected() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("updateAllSelected");
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_2);

            UpdateAllSelectedV2ApiRequest request = new UpdateAllSelectedV2ApiRequest(false);

            String url = apiV2Url(CART_API_PATH + "/items/all-selected");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("빈 아이템 ID 리스트로 선택 상태 변경 시 400 에러를 반환한다")
        void shouldReturn400WhenEmptyCartItemIds() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("emptySelect");

            UpdateCartItemSelectedV2ApiRequest request =
                    new UpdateCartItemSelectedV2ApiRequest(List.of(), true);

            String url = apiV2Url(CART_API_PATH + "/items/selected");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ============================================================
    // 장바구니 아이템 삭제 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 아이템 삭제")
    class RemoveItemsTest {

        @Test
        @DisplayName("특정 아이템들을 삭제할 수 있다")
        void shouldRemoveItems() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("remove");
            Long cartItemId1 =
                    addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);
            Long cartItemId2 =
                    addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_2);

            List<Long> cartItemIds = List.of(cartItemId1);
            RemoveCartItemsV2ApiRequest request = new RemoveCartItemsV2ApiRequest(cartItemIds);

            String url = apiV2Url(CART_API_PATH + "/items/remove");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            // 검증: 장바구니 조회 시 아이템이 1개만 남아있어야 함
            ResponseEntity<ApiResponse<CartV2ApiResponse>> getResponse =
                    restTemplate.exchange(
                            apiV2Url(CART_API_PATH),
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(getResponse.getBody().data().items()).hasSize(1);
            assertThat(getResponse.getBody().data().items().get(0).cartItemId())
                    .isEqualTo(cartItemId2);
        }

        @Test
        @DisplayName("빈 아이템 ID 리스트로 삭제 시 400 에러를 반환한다")
        void shouldReturn400WhenEmptyCartItemIds() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("emptyRemove");

            RemoveCartItemsV2ApiRequest request = new RemoveCartItemsV2ApiRequest(List.of());

            String url = apiV2Url(CART_API_PATH + "/items/remove");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ============================================================
    // 장바구니 비우기 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 비우기")
    class ClearCartTest {

        @Test
        @DisplayName("장바구니의 모든 아이템을 삭제할 수 있다")
        void shouldClearAllItems() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("clear");
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1);
            addItemToCart(accessToken, CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_2);

            String url = apiV2Url(CART_API_PATH + "/clear");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            // 검증: 장바구니 조회 시 아이템이 없어야 함
            ResponseEntity<ApiResponse<CartV2ApiResponse>> getResponse =
                    restTemplate.exchange(
                            apiV2Url(CART_API_PATH),
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(getResponse.getBody().data().items()).isEmpty();
        }

        @Test
        @DisplayName("빈 장바구니 비우기도 성공한다")
        void shouldSucceedWhenClearingEmptyCart() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("clearEmpty");
            String url = apiV2Url(CART_API_PATH + "/clear");

            // when
            ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    // ============================================================
    // 통합 시나리오 테스트
    // ============================================================

    @Nested
    @DisplayName("장바구니 통합 시나리오")
    class CartFlowScenarioTest {

        @Test
        @DisplayName("장바구니 추가 → 수량 변경 → 선택 해제 → 삭제 플로우가 정상 동작한다")
        void shouldCompleteFullCartFlow() {
            // given
            String accessToken = registerUniqueAndGetAccessToken("fullFlow");

            // Step 1: 아이템 추가
            AddCartItemV2ApiRequest addRequest =
                    new AddCartItemV2ApiRequest(
                            CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_3,
                            CartIntegrationTestFixture.TEST_PRODUCT_ID_3,
                            CartIntegrationTestFixture.TEST_PRODUCT_GROUP_ID,
                            CartIntegrationTestFixture.TEST_SELLER_ID,
                            2,
                            CartIntegrationTestFixture.TEST_UNIT_PRICE);

            ResponseEntity<ApiResponse<CartV2ApiResponse>> addResponse =
                    restTemplate.exchange(
                            apiV2Url(CART_API_PATH + "/items"),
                            HttpMethod.POST,
                            authenticatedEntity(addRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            CartV2ApiResponse addedCart = addResponse.getBody().data();
            Long cartItemId =
                    addedCart.items().stream()
                            .filter(
                                    i ->
                                            i.productStockId()
                                                    .equals(
                                                            CartIntegrationTestFixture
                                                                    .TEST_PRODUCT_STOCK_ID_3))
                            .findFirst()
                            .orElseThrow()
                            .cartItemId();

            // Step 2: 수량 변경
            UpdateCartItemQuantityV2ApiRequest quantityRequest =
                    new UpdateCartItemQuantityV2ApiRequest(5);
            ResponseEntity<ApiResponse<CartV2ApiResponse>> quantityResponse =
                    restTemplate.exchange(
                            apiV2Url(CART_API_PATH + "/items/" + cartItemId + "/quantity"),
                            HttpMethod.PATCH,
                            authenticatedEntity(quantityRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(quantityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            CartV2ApiResponse quantityCart = quantityResponse.getBody().data();
            CartItemV2ApiResponse updatedItem =
                    quantityCart.items().stream()
                            .filter(i -> i.cartItemId().equals(cartItemId))
                            .findFirst()
                            .orElseThrow();
            assertThat(updatedItem.quantity()).isEqualTo(5);

            // Step 3: 선택 해제
            UpdateCartItemSelectedV2ApiRequest selectedRequest =
                    new UpdateCartItemSelectedV2ApiRequest(List.of(cartItemId), false);
            ResponseEntity<ApiResponse<CartV2ApiResponse>> selectedResponse =
                    restTemplate.exchange(
                            apiV2Url(CART_API_PATH + "/items/selected"),
                            HttpMethod.PATCH,
                            authenticatedEntity(selectedRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(selectedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            // Step 4: 아이템 삭제
            RemoveCartItemsV2ApiRequest removeRequest =
                    new RemoveCartItemsV2ApiRequest(List.of(cartItemId));
            ResponseEntity<ApiResponse<CartV2ApiResponse>> removeResponse =
                    restTemplate.exchange(
                            apiV2Url(CART_API_PATH + "/items/remove"),
                            HttpMethod.PATCH,
                            authenticatedEntity(removeRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(removeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            // 검증: 장바구니 비어있음
            ResponseEntity<ApiResponse<CartV2ApiResponse>> finalResponse =
                    restTemplate.exchange(
                            apiV2Url(CART_API_PATH),
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(finalResponse.getBody().data().items()).isEmpty();
        }
    }

    // ============================================================
    // 헬퍼 메서드
    // ============================================================

    /**
     * 장바구니에 아이템 추가 후 cartItemId 반환
     *
     * @param accessToken 인증 토큰
     * @param productStockId 상품 재고 ID
     * @return 생성된 장바구니 아이템 ID
     */
    private Long addItemToCart(String accessToken, Long productStockId) {
        Long productId = getProductIdForStock(productStockId);

        AddCartItemV2ApiRequest request =
                new AddCartItemV2ApiRequest(
                        productStockId,
                        productId,
                        CartIntegrationTestFixture.TEST_PRODUCT_GROUP_ID,
                        CartIntegrationTestFixture.TEST_SELLER_ID,
                        CartIntegrationTestFixture.DEFAULT_QUANTITY,
                        CartIntegrationTestFixture.TEST_UNIT_PRICE);

        ResponseEntity<ApiResponse<CartV2ApiResponse>> response =
                restTemplate.exchange(
                        apiV2Url(CART_API_PATH + "/items"),
                        HttpMethod.POST,
                        authenticatedEntity(request, accessToken),
                        new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode())
                .withFailMessage(
                        "장바구니 아이템 추가 실패. StockId: %s, Status: %s",
                        productStockId, response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        // null 체크 추가
        assertThat(response.getBody())
                .withFailMessage("응답 body가 null입니다. StockId: %s", productStockId)
                .isNotNull();
        assertThat(response.getBody().data())
                .withFailMessage("응답 data가 null입니다. StockId: %s", productStockId)
                .isNotNull();
        assertThat(response.getBody().data().items())
                .withFailMessage("응답 items가 null입니다. StockId: %s", productStockId)
                .isNotNull();

        // 추가된 아이템의 cartItemId 반환 (productStockId로 필터링)
        CartItemV2ApiResponse foundItem =
                response.getBody().data().items().stream()
                        .filter(item -> item.productStockId().equals(productStockId))
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalStateException(
                                                String.format(
                                                        "추가된 아이템을 찾을 수 없음. StockId: %s, Items: %s",
                                                        productStockId,
                                                        response.getBody().data().items())));

        assertThat(foundItem.cartItemId())
                .withFailMessage(
                        "cartItemId가 null입니다. StockId: %s, Item: %s", productStockId, foundItem)
                .isNotNull();

        return foundItem.cartItemId();
    }

    /**
     * 상품 재고 ID로부터 상품 ID 조회
     *
     * @param productStockId 상품 재고 ID
     * @return 상품 ID
     */
    private Long getProductIdForStock(Long productStockId) {
        if (productStockId.equals(CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_1)) {
            return CartIntegrationTestFixture.TEST_PRODUCT_ID_1;
        } else if (productStockId.equals(CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_2)) {
            return CartIntegrationTestFixture.TEST_PRODUCT_ID_2;
        } else if (productStockId.equals(CartIntegrationTestFixture.TEST_PRODUCT_STOCK_ID_3)) {
            return CartIntegrationTestFixture.TEST_PRODUCT_ID_3;
        }
        throw new IllegalArgumentException("Unknown productStockId: " + productStockId);
    }
}
