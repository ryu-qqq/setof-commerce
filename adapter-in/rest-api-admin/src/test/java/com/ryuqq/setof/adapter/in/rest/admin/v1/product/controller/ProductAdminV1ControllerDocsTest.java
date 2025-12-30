package com.ryuqq.setof.adapter.in.rest.admin.v1.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.docs.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.error.ErrorMapperRegistry;
import com.ryuqq.setof.adapter.in.rest.admin.v1.product.mapper.ProductAdminV1ApiMapper;
import com.ryuqq.setof.application.product.dto.command.DeleteProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.MarkProductOutOfStockCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductDisplayCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.FullProductResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import com.ryuqq.setof.application.product.port.in.command.DeleteProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.command.MarkProductOutOfStockUseCase;
import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductDisplayUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupStatusUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductPriceUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
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
 * ProductAdminV1Controller REST Docs 테스트
 *
 * <p>V1 Legacy Product Admin API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@WebMvcTest(controllers = ProductAdminV1Controller.class)
@Import({ProductAdminV1Controller.class, ProductAdminV1ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("ProductAdminV1Controller REST Docs (Legacy)")
class ProductAdminV1ControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v1";

    @MockitoBean private GetFullProductUseCase getFullProductUseCase;
    @MockitoBean private GetProductGroupsUseCase getProductGroupsUseCase;
    @MockitoBean private RegisterFullProductUseCase registerFullProductUseCase;
    @MockitoBean private DeleteProductGroupUseCase deleteProductGroupUseCase;
    @MockitoBean private UpdateProductGroupUseCase updateProductGroupUseCase;
    @MockitoBean private UpdateProductPriceUseCase updateProductPriceUseCase;
    @MockitoBean private UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase;
    @MockitoBean private UpdateProductDisplayUseCase updateProductDisplayUseCase;
    @MockitoBean private MarkProductOutOfStockUseCase markProductOutOfStockUseCase;
    @MockitoBean private ProductAdminV1ApiMapper mapper;
    @MockitoBean private ErrorMapperRegistry errorMapperRegistry;

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
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @Test
    @DisplayName("GET /api/v1/product/group/{productGroupId} - [Legacy] 상품 그룹 조회")
    void fetchProductGroup() throws Exception {
        // Given
        Long productGroupId = 1L;
        FullProductResponse fullProductResponse = createMockFullProductResponse(productGroupId);

        given(getFullProductUseCase.getFullProduct(productGroupId)).willReturn(fullProductResponse);
        given(mapper.toFetchResponse(any())).willReturn(null);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/product/group/{productGroupId}", productGroupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-group-fetch",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NULL)
                                                .description("상품 그룹 상세 정보")
                                                .optional(),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName("GET /api/v1/products/group - [Legacy] 상품 그룹 목록 조회")
    void fetchProductGroups() throws Exception {
        // Given
        List<ProductGroupSummaryResponse> summaries =
                List.of(
                        new ProductGroupSummaryResponse(
                                1L,
                                1L,
                                "테스트 상품 1",
                                "SIZE_COLOR",
                                BigDecimal.valueOf(45000),
                                "ON_SALE",
                                5),
                        new ProductGroupSummaryResponse(
                                2L,
                                1L,
                                "테스트 상품 2",
                                "SIZE_COLOR",
                                BigDecimal.valueOf(35000),
                                "ON_SALE",
                                3));

        given(mapper.toQuery(any(), anyInt(), anyInt()))
                .willReturn(ProductGroupSearchQuery.defaultQuery());
        given(getProductGroupsUseCase.execute(any(ProductGroupSearchQuery.class)))
                .willReturn(summaries);
        given(getProductGroupsUseCase.count(any(ProductGroupSearchQuery.class))).willReturn(2L);
        given(mapper.toDetailResponses(any())).willReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get(BASE_URL + "/products/group").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-groups-list",
                                queryParameters(
                                        parameterWithName("page")
                                                .description("페이지 번호 (0부터 시작)")
                                                .optional(),
                                        parameterWithName("size").description("페이지 크기").optional()),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("페이지 응답"),
                                        fieldWithPath("data.content")
                                                .type(JsonFieldType.ARRAY)
                                                .description("상품 그룹 목록"),
                                        fieldWithPath("data.pageable")
                                                .type(JsonFieldType.OBJECT)
                                                .description("페이지 정보"),
                                        fieldWithPath("data.pageable.pageNumber")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지 번호"),
                                        fieldWithPath("data.pageable.pageSize")
                                                .type(JsonFieldType.NUMBER)
                                                .description("페이지 크기"),
                                        fieldWithPath("data.pageable.sort")
                                                .type(JsonFieldType.OBJECT)
                                                .description("정렬 정보"),
                                        fieldWithPath("data.pageable.sort.sorted")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("정렬 여부"),
                                        fieldWithPath("data.pageable.sort.unsorted")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("비정렬 여부"),
                                        fieldWithPath("data.pageable.sort.empty")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("정렬 정보 비어있는지 여부"),
                                        fieldWithPath("data.pageable.offset")
                                                .type(JsonFieldType.NUMBER)
                                                .description("오프셋"),
                                        fieldWithPath("data.pageable.paged")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("페이징 여부"),
                                        fieldWithPath("data.pageable.unpaged")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("비페이징 여부"),
                                        fieldWithPath("data.totalElements")
                                                .type(JsonFieldType.NUMBER)
                                                .description("전체 요소 수"),
                                        fieldWithPath("data.lastDomainId")
                                                .type(JsonFieldType.NULL)
                                                .description("마지막 도메인 ID")
                                                .optional(),
                                        fieldWithPath("data.totalPages")
                                                .type(JsonFieldType.NUMBER)
                                                .description("전체 페이지 수"),
                                        fieldWithPath("data.number")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지 번호"),
                                        fieldWithPath("data.size")
                                                .type(JsonFieldType.NUMBER)
                                                .description("페이지 크기"),
                                        fieldWithPath("data.numberOfElements")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지 요소 수"),
                                        fieldWithPath("data.first")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("첫 페이지 여부"),
                                        fieldWithPath("data.last")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("마지막 페이지 여부"),
                                        fieldWithPath("data.sort")
                                                .type(JsonFieldType.OBJECT)
                                                .description("정렬 정보"),
                                        fieldWithPath("data.sort.sorted")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("정렬 여부"),
                                        fieldWithPath("data.sort.unsorted")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("비정렬 여부"),
                                        fieldWithPath("data.sort.empty")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("정렬 정보 비어있는지 여부"),
                                        fieldWithPath("data.hasContent")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("컨텐츠 존재 여부"),
                                        fieldWithPath("data.hasNext")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 존재 여부"),
                                        fieldWithPath("data.hasPrevious")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("이전 페이지 존재 여부"),
                                        fieldWithPath("data.empty")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("비어있는지 여부"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName("POST /api/v1/product/group - [Legacy] 상품 등록")
    void registerProduct() throws Exception {
        // Given
        String requestBody =
                """
                {
                    "sellerId": 1,
                    "categoryId": 100,
                    "brandId": 1,
                    "productGroupName": "테스트 상품",
                    "optionType": "SIZE_COLOR",
                    "managementType": "SELF",
                    "price": {
                        "regularPrice": 50000,
                        "currentPrice": 45000
                    },
                    "productImageList": [
                        {
                            "productImageType": "MAIN",
                            "imageUrl": "https://example.com/image.jpg",
                            "displayOrder": 1
                        }
                    ],
                    "detailDescription": "<p>상품 상세 설명</p>",
                    "productOptions": [
                        {
                            "quantity": 100,
                            "additionalPrice": 0,
                            "options": [
                                {"optionName": "SIZE", "optionValue": "M"},
                                {"optionName": "COLOR", "optionValue": "Black"}
                            ]
                        }
                    ]
                }
                """;

        given(mapper.toRegisterCommand(any())).willReturn(createMockRegisterCommand());
        given(registerFullProductUseCase.registerFullProduct(any(RegisterFullProductCommand.class)))
                .willReturn(1L);

        // When & Then
        mockMvc.perform(
                        post(BASE_URL + "/product/group")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-register",
                                requestFields(
                                        fieldWithPath("sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 ID"),
                                        fieldWithPath("brandId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("브랜드 ID"),
                                        fieldWithPath("productGroupName")
                                                .type(JsonFieldType.STRING)
                                                .description("상품 그룹명"),
                                        fieldWithPath("optionType")
                                                .type(JsonFieldType.STRING)
                                                .description("옵션 타입 (SIZE_COLOR, SINGLE 등)"),
                                        fieldWithPath("managementType")
                                                .type(JsonFieldType.STRING)
                                                .description("관리 타입 (SELF, CONSIGNMENT 등)"),
                                        fieldWithPath("price")
                                                .type(JsonFieldType.OBJECT)
                                                .description("가격 정보"),
                                        fieldWithPath("price.regularPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("정가"),
                                        fieldWithPath("price.currentPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("판매가"),
                                        fieldWithPath("productImageList")
                                                .type(JsonFieldType.ARRAY)
                                                .description("상품 이미지 목록"),
                                        fieldWithPath("productImageList[].productImageType")
                                                .type(JsonFieldType.STRING)
                                                .description("이미지 타입"),
                                        fieldWithPath("productImageList[].imageUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("이미지 URL"),
                                        fieldWithPath("productImageList[].displayOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("표시 순서"),
                                        fieldWithPath("detailDescription")
                                                .type(JsonFieldType.STRING)
                                                .description("상세 설명"),
                                        fieldWithPath("productOptions")
                                                .type(JsonFieldType.ARRAY)
                                                .description("상품 옵션 목록"),
                                        fieldWithPath("productOptions[].quantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("재고 수량"),
                                        fieldWithPath("productOptions[].additionalPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("추가 가격"),
                                        fieldWithPath("productOptions[].options")
                                                .type(JsonFieldType.ARRAY)
                                                .description("옵션 상세"),
                                        fieldWithPath("productOptions[].options[].optionName")
                                                .type(JsonFieldType.STRING)
                                                .description("옵션명"),
                                        fieldWithPath("productOptions[].options[].optionValue")
                                                .type(JsonFieldType.STRING)
                                                .description("옵션값")),
                                responseFields(
                                        fieldWithPath("data.productGroupId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("생성된 상품 그룹 ID"),
                                        fieldWithPath("data.sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("data.products")
                                                .type(JsonFieldType.ARRAY)
                                                .description("생성된 상품 목록"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName("PATCH /api/v1/product/group/{productGroupId}/price - [Legacy] 가격 수정")
    void updatePrice() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                {
                    "regularPrice": 60000,
                    "currentPrice": 50000,
                    "discountRate": 17
                }
                """;

        given(mapper.toUpdatePriceCommand(anyLong(), any(), any()))
                .willReturn(
                        new UpdateProductPriceCommand(
                                productGroupId,
                                1L,
                                BigDecimal.valueOf(60000),
                                BigDecimal.valueOf(50000)));
        doNothing().when(updateProductPriceUseCase).execute(any(UpdateProductPriceCommand.class));

        // When & Then
        mockMvc.perform(
                        patch(BASE_URL + "/product/group/{productGroupId}/price", productGroupId)
                                .header("X-Seller-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-price-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("regularPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("정가"),
                                        fieldWithPath("currentPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("판매가"),
                                        fieldWithPath("discountRate")
                                                .type(JsonFieldType.NUMBER)
                                                .description("할인율")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NUMBER)
                                                .description("수정된 상품 그룹 ID"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName(
            "PATCH /api/v1/product/group/{productGroupId}/display-yn - [Legacy] 상품 그룹 전시 여부 수정")
    void updateDisplayYnGroup() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                {"displayYn": "Y"}
                """;

        doNothing()
                .when(updateProductGroupStatusUseCase)
                .execute(any(UpdateProductGroupStatusCommand.class));

        // When & Then
        mockMvc.perform(
                        patch(
                                        BASE_URL + "/product/group/{productGroupId}/display-yn",
                                        productGroupId)
                                .header("X-Seller-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-group-display-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("displayYn")
                                                .type(JsonFieldType.STRING)
                                                .description("전시 여부 (Y/N)")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NUMBER)
                                                .description("수정된 상품 그룹 ID"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName("PATCH /api/v1/product/{productId}/display-yn - [Legacy] 개별 상품 전시 여부 수정")
    void updateDisplayYnIndividual() throws Exception {
        // Given
        Long productId = 100L;
        String requestBody =
                """
                {"displayYn": "N"}
                """;

        doNothing()
                .when(updateProductDisplayUseCase)
                .execute(any(UpdateProductDisplayCommand.class));

        // When & Then
        mockMvc.perform(
                        patch(BASE_URL + "/product/{productId}/display-yn", productId)
                                .header("X-Seller-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-display-update",
                                pathParameters(parameterWithName("productId").description("상품 ID")),
                                requestFields(
                                        fieldWithPath("displayYn")
                                                .type(JsonFieldType.STRING)
                                                .description("전시 여부 (Y/N)")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NUMBER)
                                                .description("수정된 상품 ID"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName("PATCH /api/v1/product/group/{productGroupId}/out-stock - [Legacy] 상품 그룹 품절 처리")
    void outOfStock() throws Exception {
        // Given
        Long productGroupId = 1L;
        FullProductResponse fullProductResponse = createMockFullProductResponse(productGroupId);

        given(getFullProductUseCase.getFullProduct(productGroupId)).willReturn(fullProductResponse);
        doNothing()
                .when(markProductOutOfStockUseCase)
                .execute(any(MarkProductOutOfStockCommand.class));

        // When & Then
        mockMvc.perform(
                        patch(
                                        BASE_URL + "/product/group/{productGroupId}/out-stock",
                                        productGroupId)
                                .header("X-Seller-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-out-of-stock",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("품절 처리된 상품 목록"),
                                        fieldWithPath("data[].productId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품 ID"),
                                        fieldWithPath("data[].stockQuantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("재고 수량 (0)"),
                                        fieldWithPath("data[].productStatus")
                                                .type(JsonFieldType.STRING)
                                                .description("상품 상태 (SOLD_OUT)"),
                                        fieldWithPath("data[].option")
                                                .type(JsonFieldType.STRING)
                                                .description("옵션 값")
                                                .optional(),
                                        fieldWithPath("data[].options")
                                                .type(JsonFieldType.ARRAY)
                                                .description("옵션 상세 목록")
                                                .optional(),
                                        fieldWithPath("data[].additionalPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("추가 가격")
                                                .optional(),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName("DELETE /api/v1/product/groups - [Legacy] 상품 그룹 삭제")
    void deleteProductGroup() throws Exception {
        // Given
        String requestBody =
                """
                {"productGroupIds": [1, 2, 3]}
                """;

        given(mapper.toDeleteCommand(anyLong(), any()))
                .willReturn(new DeleteProductGroupCommand(1L, 1L));
        doNothing().when(deleteProductGroupUseCase).execute(any(DeleteProductGroupCommand.class));

        // When & Then
        mockMvc.perform(
                        delete(BASE_URL + "/product/groups")
                                .header("X-Seller-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-groups-delete",
                                requestFields(
                                        fieldWithPath("productGroupIds")
                                                .type(JsonFieldType.ARRAY)
                                                .description("삭제할 상품 그룹 ID 목록")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("삭제된 상품 그룹 ID 목록"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    private FullProductResponse createMockFullProductResponse(Long productGroupId) {
        List<ProductResponse> products =
                List.of(
                        new ProductResponse(
                                100L,
                                productGroupId,
                                "SIZE_COLOR",
                                "SIZE",
                                "M",
                                "COLOR",
                                "Black",
                                BigDecimal.ZERO,
                                false,
                                true));
        ProductGroupResponse productGroup =
                new ProductGroupResponse(
                        productGroupId,
                        1L,
                        100L,
                        null,
                        "테스트 상품",
                        "SIZE_COLOR",
                        BigDecimal.valueOf(50000),
                        BigDecimal.valueOf(45000),
                        "ON_SALE",
                        null,
                        null,
                        products);
        return new FullProductResponse(
                productGroup,
                products,
                Collections.emptyList(),
                null,
                null,
                Collections.emptyList());
    }

    private RegisterFullProductCommand createMockRegisterCommand() {
        return new RegisterFullProductCommand(
                1L, // sellerId
                100L, // categoryId
                null, // brandId
                "테스트 상품", // name
                "SIZE_COLOR", // optionType
                BigDecimal.valueOf(50000), // regularPrice
                BigDecimal.valueOf(45000), // currentPrice
                null, // shippingPolicyId
                null, // refundPolicyId
                Collections.emptyList(), // products
                Collections.emptyList(), // images
                null, // description
                null); // notice
    }
}
