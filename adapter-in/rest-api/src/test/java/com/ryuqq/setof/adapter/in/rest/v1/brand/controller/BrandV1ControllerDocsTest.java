package com.ryuqq.setof.adapter.in.rest.v1.brand.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.brand.mapper.BrandV1ApiMapper;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandUseCase;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsUseCase;
import com.ryuqq.setof.application.common.response.PageResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
 * BrandV1Controller REST Docs 테스트
 *
 * <p>Legacy V1 브랜드 API 문서 생성을 위한 테스트
 *
 * <p>V1 API 특징:
 *
 * <ul>
 *   <li>인증 불필요 (Public)
 *   <li>페이징 미지원 (최대 1000건 반환)
 *   <li>V1ApiResponse 래퍼 사용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API 사용을 권장합니다
 */
@SuppressWarnings("deprecation")
@Disabled(
        "V1 REST Docs tests fail due to ApiResponse wrapper not being applied in WebMvcTest context"
                + " - requires ControllerAdvice fix")
@WebMvcTest(controllers = BrandV1Controller.class)
@Import({BrandV1Controller.class, BrandV1ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("BrandV1Controller REST Docs (Legacy)")
class BrandV1ControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private GetBrandUseCase getBrandUseCase;
    @MockitoBean private GetBrandsUseCase getBrandsUseCase;
    @MockitoBean private BrandV1ApiMapper brandV1ApiMapper;

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
    @DisplayName("GET /api/v1/brand - [Legacy] 브랜드 목록 조회 API 문서")
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
                PageResponse.of(brands, 0, 1000, 3L, 1, true, true);

        when(getBrandsUseCase.execute(any(BrandSearchQuery.class))).thenReturn(pageResponse);
        when(brandV1ApiMapper.toV1Response(any(BrandSummaryResponse.class)))
                .thenAnswer(
                        invocation -> {
                            BrandSummaryResponse summary = invocation.getArgument(0);
                            return new BrandV1ApiResponse(
                                    summary.id(),
                                    summary.code(),
                                    summary.nameKo(),
                                    summary.logoUrl());
                        });

        // When & Then
        mockMvc.perform(get(ApiPaths.Brand.LIST).param("searchWord", "나이키"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(3))
                .andExpect(jsonPath("$.data.content[0].brandId").value(1))
                .andExpect(jsonPath("$.data.content[0].brandName").value("NIKE"))
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/brand-list",
                                queryParameters(
                                        parameterWithName("searchWord")
                                                .description("브랜드명 검색어 (선택)")
                                                .optional()),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("페이징 응답 객체"),
                                        fieldWithPath("data.content")
                                                .type(JsonFieldType.ARRAY)
                                                .description("브랜드 목록 (최대 1000건)"),
                                        fieldWithPath("data.content[].brandId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("브랜드 ID"),
                                        fieldWithPath("data.content[].brandName")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 코드명"),
                                        fieldWithPath("data.content[].korBrandName")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 한글명"),
                                        fieldWithPath("data.content[].brandIconImageUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 로고 이미지 URL")
                                                .optional(),
                                        fieldWithPath("data.page")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지 번호"),
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
    @DisplayName("GET /api/v1/brand/{brandId} - [Legacy] 브랜드 단건 조회 API 문서")
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

        BrandV1ApiResponse v1Response =
                new BrandV1ApiResponse(brandId, "NIKE", "나이키", "https://cdn.example.com/nike.png");

        when(getBrandUseCase.execute(anyLong())).thenReturn(brandResponse);
        when(brandV1ApiMapper.toV1Response(any(BrandResponse.class))).thenReturn(v1Response);

        // When & Then
        mockMvc.perform(get(ApiPaths.Brand.DETAIL, brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.brandId").value(brandId))
                .andExpect(jsonPath("$.data.brandName").value("NIKE"))
                .andExpect(jsonPath("$.data.korBrandName").value("나이키"))
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/brand-detail",
                                pathParameters(parameterWithName("brandId").description("브랜드 ID")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("브랜드 상세 정보"),
                                        fieldWithPath("data.brandId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("브랜드 ID"),
                                        fieldWithPath("data.brandName")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 코드명"),
                                        fieldWithPath("data.korBrandName")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 한글명"),
                                        fieldWithPath("data.brandIconImageUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("브랜드 로고 이미지 URL")
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
}
