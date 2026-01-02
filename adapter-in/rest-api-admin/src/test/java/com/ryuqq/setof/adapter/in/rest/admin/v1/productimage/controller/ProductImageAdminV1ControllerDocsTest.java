package com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.controller;

import static org.mockito.ArgumentMatchers.any;
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
import com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.mapper.ProductImageAdminV1ApiMapper;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.response.ProductDescriptionResponse;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;
import com.ryuqq.setof.application.productimage.dto.response.ProductImageResponse;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productdescription.vo.ProductDescriptionId;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
 * ProductImageAdminV1Controller REST Docs 테스트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@WebMvcTest(controllers = ProductImageAdminV1Controller.class)
@Import({
    ProductImageAdminV1Controller.class,
    ProductImageAdminV1ControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("ProductImageAdminV1Controller REST Docs (Legacy)")
class ProductImageAdminV1ControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v1";

    @MockitoBean private ProductImageAdminV1ApiMapper mapper;
    @MockitoBean private UpdateProductImageUseCase updateProductImageUseCase;
    @MockitoBean private GetProductImageUseCase getProductImageUseCase;
    @MockitoBean private GetProductDescriptionUseCase getProductDescriptionUseCase;
    @MockitoBean private UpdateProductDescriptionUseCase updateProductDescriptionUseCase;
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
    @DisplayName("PUT /api/v1/product/group/{productGroupId}/images - [Legacy] 상품 이미지 수정")
    void updateProductImages() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                [
                    {
                        "productImageType": "MAIN",
                        "imageUrl": "https://example.com/image1.jpg"
                    },
                    {
                        "productImageType": "SUB",
                        "imageUrl": "https://example.com/image2.jpg"
                    }
                ]
                """;

        List<ProductImageResponse> existingImages =
                List.of(
                        new ProductImageResponse(
                                1L,
                                productGroupId,
                                "MAIN",
                                "https://old.jpg",
                                null,
                                1,
                                Instant.now()));

        given(getProductImageUseCase.getByProductGroupId(ProductGroupId.of(productGroupId)))
                .willReturn(existingImages);
        given(mapper.toUpdateImageCommands(any(), any())).willReturn(Collections.emptyList());
        doNothing().when(updateProductImageUseCase).update(any(UpdateProductImageCommand.class));

        // When & Then
        mockMvc.perform(
                        put(BASE_URL + "/product/group/{productGroupId}/images", productGroupId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-images-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("[].productImageType")
                                                .type(JsonFieldType.STRING)
                                                .description("이미지 타입 (MAIN, SUB 등)"),
                                        fieldWithPath("[].imageUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("이미지 URL"),
                                        fieldWithPath("[].originUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("원본 URL")
                                                .optional()),
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
            "PUT /api/v1/product/group/{productGroupId}/detailDescription - [Legacy] 상품 상세 설명 수정")
    void updateProductDescription() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                {
                    "detailDescription": "<p>상품 상세 설명입니다.</p>"
                }
                """;

        ProductDescriptionResponse existingDescription =
                new ProductDescriptionResponse(
                        1L,
                        productGroupId,
                        "기존 상세 설명",
                        Collections.emptyList(),
                        true,
                        false,
                        Instant.now(),
                        Instant.now());

        given(getProductDescriptionUseCase.findByProductGroupId(ProductGroupId.of(productGroupId)))
                .willReturn(Optional.of(existingDescription));
        given(mapper.toUpdateDescriptionCommand(anyLong(), any()))
                .willReturn(
                        new UpdateProductDescriptionCommand(
                                ProductDescriptionId.of(1L),
                                "<p>상품 상세 설명입니다.</p>",
                                Collections.emptyList()));
        doNothing()
                .when(updateProductDescriptionUseCase)
                .execute(any(UpdateProductDescriptionCommand.class));

        // When & Then
        mockMvc.perform(
                        put(
                                        BASE_URL
                                                + "/product/group/{productGroupId}/detailDescription",
                                        productGroupId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-description-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("detailDescription")
                                                .type(JsonFieldType.STRING)
                                                .description("상세 설명 (HTML 가능)")),
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
}
