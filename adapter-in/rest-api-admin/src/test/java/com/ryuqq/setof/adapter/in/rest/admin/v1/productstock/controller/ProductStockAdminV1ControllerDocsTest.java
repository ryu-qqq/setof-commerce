package com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.controller;

import static org.mockito.ArgumentMatchers.any;
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
import com.ryuqq.setof.adapter.in.rest.admin.v1.productstock.mapper.ProductStockAdminV1ApiMapper;
import com.ryuqq.setof.application.product.dto.command.UpdateProductOptionCommand;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductOptionUseCase;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
 * ProductStockAdminV1Controller REST Docs 테스트
 *
 * <p>V1 Legacy Product Stock Admin API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@WebMvcTest(controllers = ProductStockAdminV1Controller.class)
@Import({
    ProductStockAdminV1Controller.class,
    ProductStockAdminV1ControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("ProductStockAdminV1Controller REST Docs (Legacy)")
class ProductStockAdminV1ControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v1";

    @MockitoBean private SetStockUseCase setStockUseCase;
    @MockitoBean private UpdateProductOptionUseCase updateProductOptionUseCase;
    @MockitoBean private ProductStockAdminV1ApiMapper mapper;
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
    @DisplayName("PUT /api/v1/product/group/{productGroupId}/option - [Legacy] 상품 옵션 수정")
    void updateProductOption() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                [
                    {
                        "productId": 100,
                        "quantity": 50,
                        "additionalPrice": 1000,
                        "options": [
                            {"optionName": "SIZE", "optionValue": "L"},
                            {"optionName": "COLOR", "optionValue": "Red"}
                        ]
                    }
                ]
                """;

        BDDMockito.given(mapper.toUpdateProductOptionCommand(any(), any()))
                .willReturn(
                        new UpdateProductOptionCommand(
                                100L,
                                1L,
                                "SIZE",
                                "L",
                                "COLOR",
                                "Red",
                                BigDecimal.valueOf(1000),
                                50));
        doNothing().when(updateProductOptionUseCase).execute(any(UpdateProductOptionCommand.class));

        // When & Then
        mockMvc.perform(
                        put(BASE_URL + "/product/group/{productGroupId}/option", productGroupId)
                                .header("X-Seller-Id", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-option-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("[].productId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품 ID"),
                                        fieldWithPath("[].quantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("재고 수량"),
                                        fieldWithPath("[].additionalPrice")
                                                .type(JsonFieldType.NUMBER)
                                                .description("추가 가격")
                                                .optional(),
                                        fieldWithPath("[].options")
                                                .type(JsonFieldType.ARRAY)
                                                .description("옵션 상세 목록"),
                                        fieldWithPath("[].options[].optionGroupId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("옵션 그룹 ID")
                                                .optional(),
                                        fieldWithPath("[].options[].optionDetailId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("옵션 상세 ID")
                                                .optional(),
                                        fieldWithPath("[].options[].optionName")
                                                .type(JsonFieldType.STRING)
                                                .description("옵션명 (SIZE, COLOR 등)"),
                                        fieldWithPath("[].options[].optionValue")
                                                .type(JsonFieldType.STRING)
                                                .description("옵션값 (M, Black 등)")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("수정된 상품 목록"),
                                        fieldWithPath("data[].productId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품 ID"),
                                        fieldWithPath("data[].stockQuantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("재고 수량"),
                                        fieldWithPath("data[].productStatus")
                                                .type(JsonFieldType.STRING)
                                                .description("상품 상태"),
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
    @DisplayName("PATCH /api/v1/product/{productId}/stock - [Legacy] 개별 상품 재고 수정")
    void updateProductStock() throws Exception {
        // Given
        Long productId = 100L;
        String requestBody =
                """
                {
                    "productId": 100,
                    "productStockQuantity": 200
                }
                """;

        BDDMockito.given(mapper.toSetStockCommand(any()))
                .willReturn(new SetStockCommand(productId, 200));
        doNothing().when(setStockUseCase).execute(any(SetStockCommand.class));

        // When & Then
        mockMvc.perform(
                        patch(BASE_URL + "/product/{productId}/stock", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-stock-update",
                                pathParameters(parameterWithName("productId").description("상품 ID")),
                                requestFields(
                                        fieldWithPath("productId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품 ID"),
                                        fieldWithPath("productStockQuantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("재고 수량")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("수정된 상품 목록"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }

    @Test
    @DisplayName("PATCH /api/v1/product/group/{productGroupId}/stock - [Legacy] 상품 그룹 재고 수정")
    void updateProductStocks() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                [
                    {"productId": 100, "productStockQuantity": 150},
                    {"productId": 101, "productStockQuantity": 200}
                ]
                """;

        BDDMockito.given(mapper.toSetStockCommand(any()))
                .willReturn(new SetStockCommand(100L, 150));
        doNothing().when(setStockUseCase).execute(any(SetStockCommand.class));

        // When & Then
        mockMvc.perform(
                        patch(BASE_URL + "/product/group/{productGroupId}/stock", productGroupId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-group-stock-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("[].productId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("상품 ID"),
                                        fieldWithPath("[].productStockQuantity")
                                                .type(JsonFieldType.NUMBER)
                                                .description("재고 수량")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("수정된 상품 목록"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }
}
