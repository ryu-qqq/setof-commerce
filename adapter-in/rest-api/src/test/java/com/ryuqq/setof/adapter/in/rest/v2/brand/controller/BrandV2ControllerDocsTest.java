package com.ryuqq.setof.adapter.in.rest.v2.brand.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v2.brand.dto.query.BrandV2SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.brand.mapper.BrandV2ApiMapper;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandUseCase;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsUseCase;
import com.ryuqq.setof.application.common.response.PageResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * BrandV2Controller REST Docs 테스트
 *
 * <p>브랜드 정보 조회 API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = BrandV2Controller.class)
@Import({BrandV2Controller.class, BrandV2ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("BrandV2Controller REST Docs")
class BrandV2ControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private GetBrandUseCase getBrandUseCase;
    @MockitoBean private GetBrandsUseCase getBrandsUseCase;
    @MockitoBean private BrandV2ApiMapper brandV2ApiMapper;

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
    @DisplayName("GET /api/v2/brands - 브랜드 목록 조회 API 문서")
    void getBrands() throws Exception {
        // Given
        List<BrandSummaryResponse> brands =
                List.of(
                        new BrandSummaryResponse(
                                1L, "NIKE", "나이키", "https://cdn.example.com/nike.png"),
                        new BrandSummaryResponse(
                                2L, "ADIDAS", "아디다스", "https://cdn.example.com/adidas.png"),
                        new BrandSummaryResponse(
                                3L, "PUMA", "푸마", "https://cdn.example.com/puma.png"));

        PageResponse<BrandSummaryResponse> pageResponse =
                PageResponse.of(brands, 0, 20, 3L, 1, true, true);

        doReturn(new BrandSearchQuery(null, null, 0, 20))
                .when(brandV2ApiMapper)
                .toSearchQuery(any(BrandV2SearchApiRequest.class));
        when(getBrandsUseCase.execute(any(BrandSearchQuery.class))).thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.Brands.BASE).param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(3))
                .andExpect(jsonPath("$.data.content[0].brandId").value(1))
                .andExpect(jsonPath("$.data.content[0].code").value("NIKE"))
                .andExpect(jsonPath("$.data.content[0].nameKo").value("나이키"))
                .andDo(
                        document(
                                "brand-list",
                                queryParameters(
                                        parameterWithName("page")
                                                .description("페이지 번호 (0부터 시작)")
                                                .optional(),
                                        parameterWithName("size").description("페이지 크기").optional(),
                                        parameterWithName("keyword")
                                                .description("검색 키워드 (브랜드명)")
                                                .optional()),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각 (ISO 8601 형식)"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data.content")
                                                .type(JsonFieldType.ARRAY)
                                                .description("브랜드 목록"),
                                        fieldWithPath("data.content[].brandId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("브랜드 ID"),
                                        fieldWithPath("data.content[].code")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 코드"),
                                        fieldWithPath("data.content[].nameKo")
                                                .type(JsonFieldType.STRING)
                                                .description("한글 브랜드명"),
                                        fieldWithPath("data.content[].logoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("로고 이미지 URL"),
                                        fieldWithPath("data.page")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지"),
                                        fieldWithPath("data.size")
                                                .type(JsonFieldType.NUMBER)
                                                .description("페이지 크기"),
                                        fieldWithPath("data.totalElements")
                                                .type(JsonFieldType.NUMBER)
                                                .description("전체 요소 수"),
                                        fieldWithPath("data.totalPages")
                                                .type(JsonFieldType.NUMBER)
                                                .description("전체 페이지 수"),
                                        fieldWithPath("data.first")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("첫 페이지 여부"),
                                        fieldWithPath("data.last")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("마지막 페이지 여부"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName("GET /api/v2/brands/{brandId} - 브랜드 단건 조회 API 문서")
    void getBrand() throws Exception {
        // Given
        Long brandId = 1L;
        BrandResponse brandResponse =
                new BrandResponse(
                        brandId,
                        "NIKE",
                        "나이키",
                        "Nike",
                        "https://cdn.example.com/nike.png",
                        "ACTIVE");

        when(getBrandUseCase.execute(brandId)).thenReturn(brandResponse);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.Brands.BASE + "/{brandId}", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.brandId").value(brandId))
                .andExpect(jsonPath("$.data.code").value("NIKE"))
                .andExpect(jsonPath("$.data.nameKo").value("나이키"))
                .andExpect(jsonPath("$.data.nameEn").value("Nike"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andDo(
                        document(
                                "brand-detail",
                                pathParameters(parameterWithName("brandId").description("브랜드 ID")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각 (ISO 8601 형식)"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data.brandId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("브랜드 ID"),
                                        fieldWithPath("data.code")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 코드"),
                                        fieldWithPath("data.nameKo")
                                                .type(JsonFieldType.STRING)
                                                .description("한글 브랜드명"),
                                        fieldWithPath("data.nameEn")
                                                .type(JsonFieldType.STRING)
                                                .description("영문 브랜드명"),
                                        fieldWithPath("data.logoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("로고 이미지 URL"),
                                        fieldWithPath("data.status")
                                                .type(JsonFieldType.STRING)
                                                .description("상태 (ACTIVE/INACTIVE)"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }
}
