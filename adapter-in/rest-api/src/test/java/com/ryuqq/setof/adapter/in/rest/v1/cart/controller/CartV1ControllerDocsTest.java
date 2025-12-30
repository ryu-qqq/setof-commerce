package com.ryuqq.setof.adapter.in.rest.v1.cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.command.CreateCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse.CartSearchCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse.CartSearchPriceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse.CartSearchProductStatusV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.DeleteCartV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.mapper.CartV1ApiMapper;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.RemoveCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemQuantityCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.dto.response.EnrichedCartResponse;
import com.ryuqq.setof.application.cart.port.in.command.AddCartItemUseCase;
import com.ryuqq.setof.application.cart.port.in.command.RemoveCartItemUseCase;
import com.ryuqq.setof.application.cart.port.in.command.UpdateCartItemQuantityUseCase;
import com.ryuqq.setof.application.cart.port.in.query.GetCartUseCase;
import com.ryuqq.setof.application.cart.port.in.query.GetEnrichedCartUseCase;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * CartV1Controller REST Docs 테스트
 *
 * <p>Legacy V1 장바구니 API 문서 생성을 위한 테스트
 *
 * <p>V1 Cart API 특징:
 *
 * <ul>
 *   <li>인증 필요 (JWT Token)
 *   <li>V1ApiResponse 래퍼 사용
 *   <li>SliceApiResponse로 목록 반환
 *   <li>V2 UseCase 재사용 (Strangler Fig Pattern)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API 사용을 권장합니다
 */
@SuppressWarnings("deprecation")
@Deprecated
@WebMvcTest(controllers = CartV1Controller.class)
@Import({CartV1Controller.class, CartV1ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("CartV1Controller REST Docs (Legacy)")
class CartV1ControllerDocsTest extends RestDocsTestSupport {

    private static final String TEST_MEMBER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @MockitoBean private GetCartUseCase getCartUseCase;
    @MockitoBean private GetEnrichedCartUseCase getEnrichedCartUseCase;
    @MockitoBean private AddCartItemUseCase addCartItemUseCase;
    @MockitoBean private UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    @MockitoBean private RemoveCartItemUseCase removeCartItemUseCase;
    @MockitoBean private CartV1ApiMapper cartV1ApiMapper;

    @Autowired private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUpWithSecurity(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(springSecurity())
                        .apply(
                                documentationConfiguration(restDocumentation)
                                        .operationPreprocessors()
                                        .withRequestDefaults(prettyPrint())
                                        .withResponseDefaults(prettyPrint()))
                        .build();
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .build();
        }
    }

    private MemberPrincipal createTestPrincipal() {
        return MemberPrincipal.of(TEST_MEMBER_ID, "010-1234-5678");
    }

    @Test
    @DisplayName("GET /api/v1/cart-count - [Legacy] 장바구니 개수 조회 API 문서")
    void getCartCount() throws Exception {
        // Given
        UUID memberId = UUID.fromString(TEST_MEMBER_ID);
        int cartCount = 5;

        when(getCartUseCase.getItemCount(memberId)).thenReturn(cartCount);
        when(cartV1ApiMapper.toCountResponse(cartCount))
                .thenReturn(new CartCountV1ApiResponse(cartCount));

        // When & Then
        mockMvc.perform(
                        get(ApiPaths.Cart.COUNT)
                                .with(user(createTestPrincipal()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cartQuantity").value(5))
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/cart-count",
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("장바구니 개수 정보"),
                                        fieldWithPath("data.cartQuantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("장바구니 아이템 개수"),
                                        fieldWithPath("response")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 상태 정보"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드 (200: 성공)"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지")
                                                .optional())));
    }

    @Test
    @DisplayName("GET /api/v1/carts - [Legacy] 장바구니 목록 조회 API 문서")
    void getCarts() throws Exception {
        // Given
        UUID memberId = UUID.fromString(TEST_MEMBER_ID);

        List<CartSearchV1ApiResponse> cartItems =
                List.of(
                        createSampleCartItem(1L, 12345L, 67890L, "베이직 티셔츠", "화이트/M", 2),
                        createSampleCartItem(2L, 12346L, 67891L, "슬림핏 청바지", "블루/32", 1));

        EnrichedCartResponse enrichedResponse =
                new EnrichedCartResponse(
                        1L,
                        memberId,
                        List.of(),
                        java.math.BigDecimal.valueOf(48000),
                        java.math.BigDecimal.valueOf(48000),
                        cartItems.size(),
                        cartItems.size(),
                        3,
                        java.time.Instant.now(),
                        java.time.Instant.now());

        when(getEnrichedCartUseCase.getEnrichedCart(memberId)).thenReturn(enrichedResponse);
        when(cartV1ApiMapper.toV1ResponseList(any(EnrichedCartResponse.class)))
                .thenReturn(cartItems);

        // When & Then
        mockMvc.perform(
                        get(ApiPaths.Cart.LIST)
                                .with(user(createTestPrincipal()))
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].cartId").value(1))
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/cart-list",
                                queryParameters(
                                        parameterWithName("pageNumber")
                                                .description("페이지 번호 (0부터 시작, 현재 무시됨)")
                                                .optional(),
                                        parameterWithName("pageSize")
                                                .description("페이지 크기 (현재 무시됨)")
                                                .optional()),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("장바구니 목록 페이징 정보"),
                                        fieldWithPath("data.content")
                                                .type(JsonFieldType.ARRAY)
                                                .description("장바구니 아이템 목록"),
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
                                                .description("상품 ID"),
                                        fieldWithPath("data.content[].price")
                                                .type(JsonFieldType.OBJECT)
                                                .description("가격 정보"),
                                        fieldWithPath("data.content[].price.regularPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("정가"),
                                        fieldWithPath("data.content[].price.currentPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재가"),
                                        fieldWithPath("data.content[].price.salePrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("판매가"),
                                        fieldWithPath("data.content[].price.directDiscountPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("직접 할인 금액"),
                                        fieldWithPath("data.content[].price.discountRate")
                                                .type(JsonFieldType.NUMBER)
                                                .description("할인율 (%)"),
                                        fieldWithPath("data.content[].price.directDiscountRate")
                                                .type(JsonFieldType.NUMBER)
                                                .description("직접 할인율 (%)"),
                                        fieldWithPath("data.content[].quantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("요청 수량"),
                                        fieldWithPath("data.content[].stockQuantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("재고 수량"),
                                        fieldWithPath("data.content[].optionValue")
                                                .type(JsonFieldType.STRING)
                                                .description("옵션값 (색상/사이즈)"),
                                        fieldWithPath("data.content[].cartId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("장바구니 아이템 ID"),
                                        fieldWithPath("data.content[].imageUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("썸네일 이미지 URL"),
                                        fieldWithPath("data.content[].productGroupId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품그룹 ID"),
                                        fieldWithPath("data.content[].productStatus")
                                                .type(JsonFieldType.OBJECT)
                                                .description("상품 상태 정보"),
                                        fieldWithPath("data.content[].productStatus.soldOutYn")
                                                .type(JsonFieldType.STRING)
                                                .description("품절 여부 (Y/N)"),
                                        fieldWithPath("data.content[].productStatus.displayYn")
                                                .type(JsonFieldType.STRING)
                                                .description("전시 여부 (Y/N)"),
                                        fieldWithPath("data.content[].mileageRate")
                                                .type(JsonFieldType.NUMBER)
                                                .description("마일리지 적립률"),
                                        fieldWithPath("data.content[].expectedMileageAmount")
                                                .type(JsonFieldType.NUMBER)
                                                .description("예상 적립 마일리지"),
                                        fieldWithPath("data.content[].categories")
                                                .type(JsonFieldType.ARRAY)
                                                .description("카테고리 정보 목록"),
                                        fieldWithPath("data.content[].categories[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 ID"),
                                        fieldWithPath("data.content[].categories[].categoryName")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리명"),
                                        fieldWithPath("data.content[].categories[].displayName")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리 전시명"),
                                        fieldWithPath(
                                                        "data.content[].categories[].parentCategoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("부모 카테고리 ID")
                                                .optional(),
                                        fieldWithPath("data.content[].categories[].categoryDepth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 뎁스"),
                                        fieldWithPath("data.size")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지 아이템 수"),
                                        fieldWithPath("data.hasNext")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 존재 여부"),
                                        fieldWithPath("data.cursor")
                                                .type(JsonFieldType.STRING)
                                                .description("다음 페이지 커서 (deprecated)")
                                                .optional(),
                                        fieldWithPath("data.nextCursor")
                                                .type(JsonFieldType.STRING)
                                                .description("다음 페이지 커서")
                                                .optional(),
                                        fieldWithPath("response")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 상태 정보"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드 (200: 성공)"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지")
                                                .optional())));
    }

    @Test
    @DisplayName("POST /api/v1/cart - [Legacy] 장바구니 추가 API 문서")
    void addToCart() throws Exception {
        // Given
        List<CreateCartV1ApiRequest> requests =
                List.of(new CreateCartV1ApiRequest(12345L, 67890L, 2, 100L));

        when(cartV1ApiMapper.toAddItemCommand(any(UUID.class), any(CreateCartV1ApiRequest.class)))
                .thenReturn(
                        new AddCartItemCommand(
                                UUID.fromString(TEST_MEMBER_ID),
                                67890L,
                                67890L,
                                12345L,
                                100L,
                                2,
                                java.math.BigDecimal.ZERO));
        when(addCartItemUseCase.addItem(any(AddCartItemCommand.class)))
                .thenReturn(createMockCartResponse());

        // When & Then
        mockMvc.perform(
                        post(ApiPaths.Cart.CREATE)
                                .with(user(createTestPrincipal()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/cart-create",
                                requestFields(
                                        fieldWithPath("[]")
                                                .type(JsonFieldType.ARRAY)
                                                .description("장바구니 추가 요청 목록"),
                                        fieldWithPath("[].productGroupId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품그룹 ID"),
                                        fieldWithPath("[].productId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품 ID (SKU)"),
                                        fieldWithPath("[].quantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("수량 (1-999)"),
                                        fieldWithPath("[].sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("판매자 ID")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 데이터 (빈 객체)"),
                                        fieldWithPath("response")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 상태 정보"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드 (200: 성공)"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지")
                                                .optional())));
    }

    @Test
    @DisplayName("PUT /api/v1/cart/{cartId} - [Legacy] 장바구니 수정 API 문서")
    void modifyCart() throws Exception {
        // Given
        Long cartId = 1L;
        CreateCartV1ApiRequest request = new CreateCartV1ApiRequest(12345L, 67890L, 5, 100L);

        when(cartV1ApiMapper.toUpdateQuantityCommand(
                        any(UUID.class), any(Long.class), any(CreateCartV1ApiRequest.class)))
                .thenReturn(
                        new UpdateCartItemQuantityCommand(
                                UUID.fromString(TEST_MEMBER_ID), cartId, 5));
        when(updateCartItemQuantityUseCase.updateQuantity(any(UpdateCartItemQuantityCommand.class)))
                .thenReturn(createMockCartResponse());

        // When & Then
        mockMvc.perform(
                        put(ApiPaths.Cart.UPDATE, cartId)
                                .with(user(createTestPrincipal()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/cart-update",
                                pathParameters(
                                        parameterWithName("cartId").description("장바구니 아이템 ID")),
                                requestFields(
                                        fieldWithPath("productGroupId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품그룹 ID"),
                                        fieldWithPath("productId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품 ID (SKU)"),
                                        fieldWithPath("quantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("수정할 수량 (1-999)"),
                                        fieldWithPath("sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("판매자 ID")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 데이터 (빈 객체)"),
                                        fieldWithPath("response")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 상태 정보"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드 (200: 성공)"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지")
                                                .optional())));
    }

    @Test
    @DisplayName("DELETE /api/v1/carts/{cartId} - [Legacy] 장바구니 삭제 API 문서")
    void deleteCarts() throws Exception {
        // Given
        Long cartId = 1L;

        when(cartV1ApiMapper.toRemoveItemCommand(any(UUID.class), any()))
                .thenReturn(
                        new RemoveCartItemCommand(
                                UUID.fromString(TEST_MEMBER_ID), List.of(cartId)));
        when(removeCartItemUseCase.removeItems(any(RemoveCartItemCommand.class)))
                .thenReturn(createMockCartResponse());
        when(cartV1ApiMapper.toDeleteResponse(1)).thenReturn(new DeleteCartV1ApiResponse(cartId));

        // When & Then
        mockMvc.perform(
                        delete(ApiPaths.Cart.DELETE, cartId)
                                .with(user(createTestPrincipal()))
                                .param("cartId", String.valueOf(cartId))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cartId").value(1))
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/cart-delete",
                                pathParameters(
                                        parameterWithName("cartId").description("장바구니 아이템 ID")),
                                queryParameters(
                                        parameterWithName("cartId")
                                                .description("삭제할 장바구니 ID")
                                                .optional()),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("삭제 결과 정보"),
                                        fieldWithPath("data.cartId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("삭제된 장바구니 ID"),
                                        fieldWithPath("response")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 상태 정보"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드 (200: 성공)"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지")
                                                .optional())));
    }

    private CartSearchV1ApiResponse createSampleCartItem(
            Long cartId,
            Long productGroupId,
            Long productId,
            String productGroupName,
            String optionValue,
            int quantity) {

        CartSearchPriceV1ApiResponse price =
                new CartSearchPriceV1ApiResponse(29000L, 19000L, 19000L, 10000L, 34, 34);

        CartSearchProductStatusV1ApiResponse status =
                new CartSearchProductStatusV1ApiResponse("N", "Y");

        List<CartSearchCategoryV1ApiResponse> categories =
                List.of(
                        new CartSearchCategoryV1ApiResponse(1L, "의류", "의류", null, 1),
                        new CartSearchCategoryV1ApiResponse(10L, "상의", "상의", 1L, 2));

        return new CartSearchV1ApiResponse(
                1L,
                "NIKE",
                productGroupName,
                100L,
                "나이키코리아",
                productId,
                price,
                quantity,
                100,
                optionValue,
                cartId,
                "https://cdn.example.com/image.jpg",
                productGroupId,
                status,
                0.01,
                190.0,
                categories);
    }

    private CartResponse createMockCartResponse() {
        return new CartResponse(
                1L,
                UUID.fromString(TEST_MEMBER_ID),
                List.of(),
                java.math.BigDecimal.valueOf(48000),
                java.math.BigDecimal.valueOf(48000),
                1,
                1,
                2,
                java.time.Instant.now(),
                java.time.Instant.now());
    }
}
