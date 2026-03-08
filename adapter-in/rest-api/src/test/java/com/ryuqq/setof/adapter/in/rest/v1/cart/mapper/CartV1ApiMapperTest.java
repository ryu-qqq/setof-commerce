package com.ryuqq.setof.adapter.in.rest.v1.cart.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.cart.CartApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.AddCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.DeleteCartItemsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.ModifyCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.SearchCartsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartV1ApiResponse;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CartV1ApiMapper 단위 테스트.
 *
 * <p>장바구니 V1 API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("CartV1ApiMapper 단위 테스트")
class CartV1ApiMapperTest {

    private CartV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CartV1ApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams 메서드 테스트")
    class ToSearchParamsTest {

        @Test
        @DisplayName("userId와 SearchRequest를 CartSearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            Long userId = 42L;
            SearchCartsCursorV1ApiRequest request = CartApiFixtures.searchRequest();

            // when
            CartSearchParams params = mapper.toSearchParams(userId, request);

            // then
            assertThat(params.memberId()).isEqualTo("42");
            assertThat(params.userId()).isEqualTo(userId);
            assertThat(params.lastCartId()).isNull();
            assertThat(params.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("memberId는 userId의 문자열 변환값으로 설정된다")
        void toSearchParams_MemberIdIsStringOfUserId() {
            // given
            Long userId = 100L;
            SearchCartsCursorV1ApiRequest request = CartApiFixtures.searchRequest();

            // when
            CartSearchParams params = mapper.toSearchParams(userId, request);

            // then
            assertThat(params.memberId()).isEqualTo(String.valueOf(userId));
        }

        @Test
        @DisplayName("size가 null이면 기본값 20이 적용된다")
        void toSearchParams_DefaultSizeWhenNull() {
            // given
            Long userId = 42L;
            SearchCartsCursorV1ApiRequest request = CartApiFixtures.searchRequestWithNullSize();

            // when
            CartSearchParams params = mapper.toSearchParams(userId, request);

            // then
            assertThat(params.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("커서가 있는 경우 lastCartId가 올바르게 전달된다")
        void toSearchParams_WithCursor() {
            // given
            Long userId = 42L;
            Long lastCartId = 999L;
            SearchCartsCursorV1ApiRequest request =
                    CartApiFixtures.searchRequestWithCursor(lastCartId);

            // when
            CartSearchParams params = mapper.toSearchParams(userId, request);

            // then
            assertThat(params.lastCartId()).isEqualTo(lastCartId);
            assertThat(params.size()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("toCountParams 메서드 테스트")
    class ToCountParamsTest {

        @Test
        @DisplayName("userId로 카운트 조회용 CartSearchParams를 생성한다")
        void toCountParams_Success() {
            // given
            Long userId = 42L;

            // when
            CartSearchParams params = mapper.toCountParams(userId);

            // then
            assertThat(params.memberId()).isEqualTo("42");
            assertThat(params.userId()).isEqualTo(userId);
            assertThat(params.lastCartId()).isNull();
        }

        @Test
        @DisplayName("카운트 파라미터의 memberId는 userId의 문자열 변환값이다")
        void toCountParams_MemberIdIsStringOfUserId() {
            // given
            Long userId = 7L;

            // when
            CartSearchParams params = mapper.toCountParams(userId);

            // then
            assertThat(params.memberId()).isEqualTo("7");
        }
    }

    @Nested
    @DisplayName("toCartCountResponse 메서드 테스트")
    class ToCartCountResponseTest {

        @Test
        @DisplayName("CartCountResult를 CartCountV1ApiResponse로 변환한다")
        void toCartCountResponse_Success() {
            // given
            CartCountResult result = CartApiFixtures.cartCountResult();

            // when
            CartCountV1ApiResponse response = mapper.toCartCountResponse(result);

            // then
            assertThat(response.cartQuantity()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("toCartSliceResponse 메서드 테스트")
    class ToCartSliceResponseTest {

        @Test
        @DisplayName("CartSliceResult를 레거시 호환 CartSliceV1ApiResponse로 변환한다")
        void toCartSliceResponse_Success() {
            // given
            CartSliceResult result = CartApiFixtures.cartSliceResult();

            // when
            CartSliceV1ApiResponse response = mapper.toCartSliceResponse(result, null);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.last()).isFalse();
            assertThat(response.first()).isTrue();
            assertThat(response.number()).isZero();
            assertThat(response.sort().unsorted()).isTrue();
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.numberOfElements()).isEqualTo(2);
            assertThat(response.empty()).isFalse();
            assertThat(response.totalElements()).isEqualTo(10L);
        }

        @Test
        @DisplayName("lastDomainId는 SliceMeta 커서 값으로 설정된다")
        void toCartSliceResponse_LastDomainIdFromCursor() {
            // given
            CartSliceResult result = CartApiFixtures.cartSliceResult();

            // when
            CartSliceV1ApiResponse response = mapper.toCartSliceResponse(result, null);

            // then
            assertThat(response.lastDomainId()).isEqualTo(1002L);
            assertThat(response.cursorValue()).isEqualTo("1002");
        }

        @Test
        @DisplayName("content가 비어있으면 lastDomainId는 null이고 empty는 true이다")
        void toCartSliceResponse_EmptyResult() {
            // given
            CartSliceResult result = CartApiFixtures.cartSliceResultEmpty();

            // when
            CartSliceV1ApiResponse response = mapper.toCartSliceResponse(result, null);

            // then
            assertThat(response.lastDomainId()).isNull();
            assertThat(response.cursorValue()).isNull();
            assertThat(response.content()).isEmpty();
            assertThat(response.last()).isTrue();
            assertThat(response.empty()).isTrue();
        }

        @Test
        @DisplayName("options가 null인 CartItemResult를 변환할 때 categories는 빈 Set이 된다")
        void toCartSliceResponse_NullOptionsResultsInEmptyCategories() {
            // given
            CartItemResult itemWithNullOptions =
                    CartApiFixtures.cartItemResultWithNullOptions(2001L);
            SliceMeta sliceMeta = SliceMeta.withCursor(2001L, 20, false, 1);
            CartSliceResult result =
                    CartSliceResult.of(List.of(itemWithNullOptions), sliceMeta, 1L);

            // when
            CartSliceV1ApiResponse response = mapper.toCartSliceResponse(result, null);

            // then
            assertThat(response.content()).hasSize(1);
            CartV1ApiResponse cartResponse = response.content().get(0);
            assertThat(cartResponse.categories()).isEmpty();
        }

        @Test
        @DisplayName("CartItemResult의 각 필드가 CartV1ApiResponse에 올바르게 매핑된다")
        void toCartSliceResponse_FieldMapping() {
            // given
            CartSliceResult result = CartApiFixtures.cartSliceResultLastPage();

            // when
            CartSliceV1ApiResponse response = mapper.toCartSliceResponse(result, null);

            // then
            assertThat(response.content()).hasSize(1);
            assertThat(response.last()).isTrue();
            assertThat(response.lastDomainId()).isNull();

            CartV1ApiResponse item = response.content().get(0);
            assertThat(item.cartId()).isEqualTo(1003L);
            assertThat(item.productGroupId()).isEqualTo(101L);
            assertThat(item.productGroupName()).isEqualTo("에어맥스 90");
            assertThat(item.productId()).isEqualTo(1001L);
            assertThat(item.quantity()).isEqualTo(2);
            assertThat(item.stockQuantity()).isEqualTo(50);
            assertThat(item.optionValue()).isEqualTo("블랙 270");
            assertThat(item.imageUrl()).isEqualTo("https://cdn.example.com/image.jpg");
            assertThat(item.productStatus()).isEqualTo("ON_SALE");
            assertThat(item.brandId()).isEqualTo(10L);
            assertThat(item.brandName()).isEqualTo("나이키");
            assertThat(item.sellerId()).isEqualTo(10L);
            assertThat(item.sellerName()).isEqualTo("공식스토어");
            assertThat(item.price().regularPrice()).isEqualTo(150000);
            assertThat(item.price().currentPrice()).isEqualTo(129000);
            assertThat(item.price().salePrice()).isEqualTo(129000);
            assertThat(item.price().directDiscountRate()).isEqualTo(0);
            assertThat(item.price().directDiscountPrice()).isEqualTo(0);
            assertThat(item.price().discountRate()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("toAddCommand 메서드 테스트")
    class ToAddCommandTest {

        @Test
        @DisplayName("userId와 AddRequest 목록을 AddCartItemCommand로 변환한다")
        void toAddCommand_Success() {
            // given
            Long userId = 42L;
            List<AddCartItemV1ApiRequest> requests = CartApiFixtures.addRequestList();

            // when
            AddCartItemCommand command = mapper.toAddCommand(userId, requests);

            // then
            assertThat(command.memberId()).isEqualTo("42");
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.items()).hasSize(2);
        }

        @Test
        @DisplayName("memberId는 userId의 문자열 변환값이다")
        void toAddCommand_MemberIdIsStringOfUserId() {
            // given
            Long userId = 99L;
            List<AddCartItemV1ApiRequest> requests = List.of(CartApiFixtures.addRequest());

            // when
            AddCartItemCommand command = mapper.toAddCommand(userId, requests);

            // then
            assertThat(command.memberId()).isEqualTo("99");
        }

        @Test
        @DisplayName("AddRequest의 각 필드가 CartItemDetail에 올바르게 매핑된다")
        void toAddCommand_ItemDetailFieldMapping() {
            // given
            Long userId = 42L;
            List<AddCartItemV1ApiRequest> requests =
                    List.of(new AddCartItemV1ApiRequest(101L, 1001L, 2, 10L));

            // when
            AddCartItemCommand command = mapper.toAddCommand(userId, requests);

            // then
            AddCartItemCommand.CartItemDetail detail = command.items().get(0);
            assertThat(detail.productGroupId()).isEqualTo(101L);
            assertThat(detail.productId()).isEqualTo(1001L);
            assertThat(detail.quantity()).isEqualTo(2);
            assertThat(detail.sellerId()).isEqualTo(10L);
        }
    }

    @Nested
    @DisplayName("toModifyCommand 메서드 테스트")
    class ToModifyCommandTest {

        @Test
        @DisplayName("cartId, userId, ModifyRequest를 ModifyCartItemCommand로 변환한다")
        void toModifyCommand_Success() {
            // given
            Long cartId = 500L;
            Long userId = 42L;
            ModifyCartItemV1ApiRequest request = CartApiFixtures.modifyRequest();

            // when
            ModifyCartItemCommand command = mapper.toModifyCommand(cartId, userId, request);

            // then
            assertThat(command.cartId()).isEqualTo(cartId);
            assertThat(command.memberId()).isEqualTo("42");
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.newQuantity()).isEqualTo(3);
        }

        @Test
        @DisplayName("memberId는 userId의 문자열 변환값이다")
        void toModifyCommand_MemberIdIsStringOfUserId() {
            // given
            Long cartId = 500L;
            Long userId = 77L;
            ModifyCartItemV1ApiRequest request = new ModifyCartItemV1ApiRequest(5);

            // when
            ModifyCartItemCommand command = mapper.toModifyCommand(cartId, userId, request);

            // then
            assertThat(command.memberId()).isEqualTo("77");
        }
    }

    @Nested
    @DisplayName("toDeleteCommand 메서드 테스트")
    class ToDeleteCommandTest {

        @Test
        @DisplayName("userId와 DeleteRequest를 DeleteCartItemsCommand로 변환한다")
        void toDeleteCommand_Success() {
            // given
            Long userId = 42L;
            DeleteCartItemsV1ApiRequest request = CartApiFixtures.deleteRequest(300L);

            // when
            DeleteCartItemsCommand command = mapper.toDeleteCommand(userId, request);

            // then
            assertThat(command.memberId()).isEqualTo("42");
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.cartIds()).containsExactly(300L);
        }

        @Test
        @DisplayName("ofSingle 패턴으로 cartIds 목록이 단건 ID를 포함한다")
        void toDeleteCommand_CartIdsContainsSingleId() {
            // given
            Long userId = 42L;
            Long cartId = 999L;
            DeleteCartItemsV1ApiRequest request = CartApiFixtures.deleteRequest(cartId);

            // when
            DeleteCartItemsCommand command = mapper.toDeleteCommand(userId, request);

            // then
            assertThat(command.cartIds()).hasSize(1);
            assertThat(command.cartIds()).containsExactly(cartId);
        }
    }
}
