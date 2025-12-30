package com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.mapper.ProductNoticeAdminV1ApiMapper;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeId;
import java.util.Collections;
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
 * ProductNoticeAdminV1Controller REST Docs 테스트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@WebMvcTest(controllers = ProductNoticeAdminV1Controller.class)
@Import({
    ProductNoticeAdminV1Controller.class,
    ProductNoticeAdminV1ControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("ProductNoticeAdminV1Controller REST Docs (Legacy)")
class ProductNoticeAdminV1ControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v1";

    @MockitoBean private UpdateProductNoticeUseCase updateProductNoticeUseCase;
    @MockitoBean private ProductNoticeAdminV1ApiMapper mapper;
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
    @DisplayName("PUT /api/v1/product/group/{productGroupId}/notice - [Legacy] 상품 고지 수정")
    void updateProductNotice() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                {
                    "material": "면 100%",
                    "color": "블랙, 화이트, 네이비",
                    "size": "S, M, L, XL",
                    "manufacturer": "테스트 제조사",
                    "countryOfOrigin": "대한민국",
                    "caution": "세탁 시 주의사항",
                    "warrantyInfo": "품질보증 1년",
                    "customerServiceInfo": "고객센터: 1234-5678"
                }
                """;

        given(mapper.toUpdateCommand(anyLong(), any()))
                .willReturn(
                        new UpdateProductNoticeCommand(
                                ProductNoticeId.of(1L), Collections.emptyList()));
        doNothing().when(updateProductNoticeUseCase).execute(any(UpdateProductNoticeCommand.class));

        // When & Then
        mockMvc.perform(
                        put(BASE_URL + "/product/group/{productGroupId}/notice", productGroupId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-product-notice-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("material")
                                                .type(JsonFieldType.STRING)
                                                .description("소재")
                                                .optional(),
                                        fieldWithPath("color")
                                                .type(JsonFieldType.STRING)
                                                .description("색상")
                                                .optional(),
                                        fieldWithPath("size")
                                                .type(JsonFieldType.STRING)
                                                .description("사이즈")
                                                .optional(),
                                        fieldWithPath("manufacturer")
                                                .type(JsonFieldType.STRING)
                                                .description("제조사")
                                                .optional(),
                                        fieldWithPath("countryOfOrigin")
                                                .type(JsonFieldType.STRING)
                                                .description("원산지")
                                                .optional(),
                                        fieldWithPath("caution")
                                                .type(JsonFieldType.STRING)
                                                .description("주의사항")
                                                .optional(),
                                        fieldWithPath("warrantyInfo")
                                                .type(JsonFieldType.STRING)
                                                .description("품질보증정보")
                                                .optional(),
                                        fieldWithPath("customerServiceInfo")
                                                .type(JsonFieldType.STRING)
                                                .description("고객서비스 정보")
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
}
