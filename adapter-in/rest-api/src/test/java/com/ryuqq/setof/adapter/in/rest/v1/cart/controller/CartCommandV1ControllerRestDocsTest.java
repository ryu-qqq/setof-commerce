package com.ryuqq.setof.adapter.in.rest.v1.cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.cart.CartApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.cart.CartV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.AddCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.ModifyCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.mapper.CartV1ApiMapper;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.cart.port.in.command.AddCartItemUseCase;
import com.ryuqq.setof.application.cart.port.in.command.DeleteCartItemsUseCase;
import com.ryuqq.setof.application.cart.port.in.command.ModifyCartItemUseCase;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * CartCommandV1Controller REST Docs 테스트.
 *
 * <p>장바구니 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("CartCommandV1Controller REST Docs 테스트")
@WebMvcTest(CartCommandV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class CartCommandV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private AddCartItemUseCase addCartItemUseCase;

    @MockBean private ModifyCartItemUseCase modifyCartItemUseCase;

    @MockBean private DeleteCartItemsUseCase deleteCartItemsUseCase;

    @MockBean private CartV1ApiMapper mapper;

    @Nested
    @DisplayName("장바구니 항목 추가 API")
    class AddCartItemsTest {

        @Test
        @DisplayName("장바구니 항목 추가 성공")
        void addCartItems_Success() throws Exception {
            // given
            List<AddCartItemV1ApiRequest> requests = CartApiFixtures.addRequestList();
            AddCartItemCommand command = CartApiFixtures.addCommandMultiple(1L);
            List<CartItem> cartItems = List.of();
            List<com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartV1ApiResponse>
                    responseList = List.of();

            given(mapper.toAddCommand(any(), any())).willReturn(command);
            given(addCartItemUseCase.execute(command)).willReturn(cartItems);
            given(mapper.toCartResponseList(cartItems)).willReturn(responseList);

            // when & then
            mockMvc.perform(
                            post(CartV1Endpoints.CART)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(requests)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("[].productGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품 그룹 ID"),
                                            fieldWithPath("[].productId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품(SKU) ID"),
                                            fieldWithPath("[].quantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("수량 (1~999)"),
                                            fieldWithPath("[].sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("판매자 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("추가된 장바구니 항목 목록"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("장바구니 항목 수량 수정 API")
    class ModifyCartItemTest {

        @Test
        @DisplayName("장바구니 항목 수량 수정 성공")
        void modifyCartItem_Success() throws Exception {
            // given
            long cartId = 1001L;
            ModifyCartItemV1ApiRequest request = CartApiFixtures.modifyRequest();
            ModifyCartItemCommand command = CartApiFixtures.modifyCommand(cartId, 1L);

            given(mapper.toModifyCommand(any(), any(), any())).willReturn(command);
            willDoNothing().given(modifyCartItemUseCase).execute(command);

            // when & then
            mockMvc.perform(
                            put(CartV1Endpoints.CART_BY_ID, cartId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(CartV1Endpoints.PATH_CART_ID)
                                                    .description("수정할 장바구니 항목 ID")),
                                    requestFields(
                                            fieldWithPath("quantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("변경할 수량 (1~999)")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("응답 데이터 없음"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("장바구니 항목 삭제 API")
    class DeleteCartItemsTest {

        @Test
        @DisplayName("장바구니 항목 삭제 성공")
        void deleteCartItems_Success() throws Exception {
            // given
            long cartId = 1001L;
            DeleteCartItemsCommand command = CartApiFixtures.deleteCommand(cartId, 1L);
            int deletedCount = 1;

            given(mapper.toDeleteCommand(any(), any())).willReturn(command);
            given(deleteCartItemsUseCase.execute(command)).willReturn(deletedCount);

            // when & then
            mockMvc.perform(delete(CartV1Endpoints.CARTS + "?cartId=" + cartId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(deletedCount))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("cartId")
                                                    .description("삭제할 장바구니 항목 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("삭제된 항목 수"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
