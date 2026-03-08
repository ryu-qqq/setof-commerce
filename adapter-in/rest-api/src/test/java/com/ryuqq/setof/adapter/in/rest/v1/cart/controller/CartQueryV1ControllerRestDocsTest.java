package com.ryuqq.setof.adapter.in.rest.v1.cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.cart.CartApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.cart.CartV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.mapper.CartV1ApiMapper;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.application.cart.port.in.query.GetCartCountUseCase;
import com.ryuqq.setof.application.cart.port.in.query.GetCartsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * CartQueryV1Controller REST Docs 테스트.
 *
 * <p>장바구니 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("CartQueryV1Controller REST Docs 테스트")
@WebMvcTest(CartQueryV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class CartQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetCartCountUseCase getCartCountUseCase;

    @MockBean private GetCartsUseCase getCartsUseCase;

    @MockBean private CartV1ApiMapper mapper;

    @Nested
    @DisplayName("장바구니 개수 조회 API")
    class GetCartCountTest {

        @Test
        @DisplayName("장바구니 아이템 개수 조회 성공")
        void getCartCount_Success() throws Exception {
            // given
            CartCountResult result = CartApiFixtures.cartCountResult();
            CartCountV1ApiResponse response = CartApiFixtures.cartCountResponse();

            given(mapper.toCountParams(any())).willReturn(CartApiFixtures.countParams(1L));
            given(getCartCountUseCase.execute(any(CartSearchParams.class))).willReturn(result);
            given(mapper.toCartCountResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(CartV1Endpoints.CART_COUNT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.cartQuantity").value(5))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data.cartQuantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("장바구니에 담긴 아이템 개수"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("장바구니 목록 조회 API")
    class GetCartsTest {

        @Test
        @DisplayName("장바구니 목록 커서 기반 조회 성공")
        void getCarts_Success() throws Exception {
            // given
            CartSliceResult result = CartApiFixtures.cartSliceResult();
            CartSliceV1ApiResponse response = CartApiFixtures.cartSliceResponse();

            given(mapper.toSearchParams(any(), any())).willReturn(CartApiFixtures.searchParams(1L));
            given(getCartsUseCase.execute(any(CartSearchParams.class))).willReturn(result);
            given(mapper.toCartSliceResponse(any(), any())).willReturn(response);

            // when & then
            mockMvc.perform(get(CartV1Endpoints.CARTS).param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(2))
                    .andExpect(jsonPath("$.data.last").value(false))
                    .andExpect(jsonPath("$.data.first").value(true))
                    .andExpect(jsonPath("$.data.lastDomainId").value(1002))
                    .andExpect(jsonPath("$.data.totalElements").value(10))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("lastDomainId")
                                                    .description(
                                                            "커서: 마지막으로 조회한 도메인 ID (다음 페이지 조회 시"
                                                                    + " 사용)")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("조회할 아이템 수 (1~100, 기본값: 20)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("장바구니 항목 목록"),
                                            fieldWithPath("data.content[].brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data.content[].brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명"),
                                            fieldWithPath("data.content[].productGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품그룹명"),
                                            fieldWithPath("data.content[].sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("판매자 ID"),
                                            fieldWithPath("data.content[].sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("판매자명"),
                                            fieldWithPath("data.content[].productId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품(SKU) ID"),
                                            fieldWithPath("data.content[].price")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("가격 정보"),
                                            fieldWithPath("data.content[].price.regularPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정가"),
                                            fieldWithPath("data.content[].price.currentPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 판매가"),
                                            fieldWithPath("data.content[].price.salePrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인가"),
                                            fieldWithPath("data.content[].price.directDiscountRate")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("직접 할인율"),
                                            fieldWithPath(
                                                            "data.content[].price.directDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("직접 할인가"),
                                            fieldWithPath("data.content[].price.discountRate")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 할인율"),
                                            fieldWithPath("data.content[].quantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("장바구니 수량"),
                                            fieldWithPath("data.content[].stockQuantity")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 재고 수량"),
                                            fieldWithPath("data.content[].optionValue")
                                                    .type(JsonFieldType.STRING)
                                                    .description("옵션 값 (예: 블랙 270)"),
                                            fieldWithPath("data.content[].cartId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("장바구니 ID"),
                                            fieldWithPath("data.content[].productGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품그룹 ID"),
                                            fieldWithPath("data.content[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표 이미지 URL"),
                                            fieldWithPath("data.content[].productStatus")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 상태 (ON_SALE, SOLD_OUT 등)"),
                                            fieldWithPath("data.content[].categories")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("카테고리 목록"),
                                            fieldWithPath("data.content[].categories[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath(
                                                            "data.content[].categories[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data.content[].mileage")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("마일리지 정보"),
                                            fieldWithPath("data.content[].mileage.mileageRate")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("마일리지 적립률 (0.01 = 1%)"),
                                            fieldWithPath(
                                                            "data.content[].mileage.expectedMileageAmount")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("예상 적립 마일리지"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 번째 페이지 여부"),
                                            fieldWithPath("data.number")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 번호 (커서 페이징에서는 항상 0)"),
                                            fieldWithPath("data.sort.unsorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬되지 않음 여부"),
                                            fieldWithPath("data.sort.sorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬됨 여부"),
                                            fieldWithPath("data.sort.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬 조건 비어있음 여부"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("요청한 페이지 크기"),
                                            fieldWithPath("data.numberOfElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 항목 수"),
                                            fieldWithPath("data.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("결과 비어있음 여부"),
                                            fieldWithPath("data.lastDomainId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("다음 페이지 요청에 사용할 커서 (마지막 도메인 ID)"),
                                            fieldWithPath("data.cursorValue")
                                                    .type(JsonFieldType.STRING)
                                                    .description("커서 값 (문자열)"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 장바구니 항목 수"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("빈 장바구니 목록 조회 성공")
        void getCarts_Empty_Success() throws Exception {
            // given
            CartSliceResult result = CartApiFixtures.cartSliceResultEmpty();
            CartSliceV1ApiResponse response = CartApiFixtures.cartSliceResponseEmpty();

            given(mapper.toSearchParams(any(), any())).willReturn(CartApiFixtures.searchParams(1L));
            given(getCartsUseCase.execute(any(CartSearchParams.class))).willReturn(result);
            given(mapper.toCartSliceResponse(any(), any())).willReturn(response);

            // when & then
            mockMvc.perform(get(CartV1Endpoints.CARTS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(0))
                    .andExpect(jsonPath("$.data.last").value(true))
                    .andExpect(jsonPath("$.data.empty").value(true))
                    .andExpect(jsonPath("$.data.totalElements").value(0))
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
