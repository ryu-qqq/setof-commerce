package com.ryuqq.setof.adapter.in.rest.v1.category.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.category.mapper.CategoryV1ApiMapper;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryTreeUseCase;
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
 * CategoryV1Controller REST Docs 테스트
 *
 * <p>Legacy V1 카테고리 API 문서 생성을 위한 테스트
 *
 * <p>V1 API 특징:
 *
 * <ul>
 *   <li>인증 불필요 (Public)
 *   <li>트리 구조 반환
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
@WebMvcTest(controllers = CategoryV1Controller.class)
@Import({CategoryV1Controller.class, CategoryV1ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("CategoryV1Controller REST Docs (Legacy)")
class CategoryV1ControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private GetCategoryTreeUseCase getCategoryTreeUseCase;
    @MockitoBean private CategoryV1ApiMapper categoryV1ApiMapper;

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
    @DisplayName("GET /api/v1/category - [Legacy] 카테고리 트리 조회 API 문서")
    void getCategories() throws Exception {
        // Given
        // CategoryTreeResponse: id, code, nameKo, depth, sortOrder, isLeaf, children
        List<CategoryTreeResponse> treeResponses =
                List.of(
                        CategoryTreeResponse.of(
                                1L,
                                "TOP",
                                "상의",
                                1,
                                1,
                                false,
                                List.of(
                                        CategoryTreeResponse.of(
                                                10L,
                                                "TSHIRT",
                                                "티셔츠",
                                                2,
                                                1,
                                                false,
                                                List.of(
                                                        CategoryTreeResponse.of(
                                                                100L,
                                                                "SHORT_SLEEVE",
                                                                "반팔티",
                                                                3,
                                                                1,
                                                                true,
                                                                List.of()))),
                                        CategoryTreeResponse.of(
                                                11L, "SHIRT", "셔츠", 2, 2, true, List.of()))),
                        CategoryTreeResponse.of(2L, "BOTTOM", "하의", 1, 2, true, List.of()));

        List<CategoryV1ApiResponse> v1Responses =
                List.of(
                        new CategoryV1ApiResponse(
                                1L,
                                "상의",
                                1,
                                null,
                                List.of(
                                        new CategoryV1ApiResponse(
                                                10L,
                                                "티셔츠",
                                                2,
                                                1L,
                                                List.of(
                                                        new CategoryV1ApiResponse(
                                                                100L, "반팔티", 3, 10L, List.of()))),
                                        new CategoryV1ApiResponse(11L, "셔츠", 2, 1L, List.of()))),
                        new CategoryV1ApiResponse(2L, "하의", 1, null, List.of()));

        when(getCategoryTreeUseCase.getCategoryTree()).thenReturn(treeResponses);
        when(categoryV1ApiMapper.toV1ResponseList(treeResponses)).thenReturn(v1Responses);

        // When & Then
        mockMvc.perform(get(ApiPaths.Category.LIST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].categoryId").value(1))
                .andExpect(jsonPath("$.data[0].categoryName").value("상의"))
                .andExpect(jsonPath("$.data[0].categoryDepth").value(1))
                .andExpect(jsonPath("$.data[0].children").isArray())
                .andExpect(jsonPath("$.data[0].children.length()").value(2))
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1/category-tree",
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("카테고리 트리 목록"),
                                        fieldWithPath("data[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 ID"),
                                        fieldWithPath("data[].categoryName")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리명"),
                                        fieldWithPath("data[].categoryDepth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("깊이 (1: 대분류, 2: 중분류, 3: 소분류)"),
                                        fieldWithPath("data[].parentCategoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("부모 카테고리 ID")
                                                .optional(),
                                        fieldWithPath("data[].children")
                                                .type(JsonFieldType.ARRAY)
                                                .description("하위 카테고리 목록"),
                                        fieldWithPath("data[].children[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("하위 카테고리 ID"),
                                        fieldWithPath("data[].children[].categoryName")
                                                .type(JsonFieldType.STRING)
                                                .description("하위 카테고리명"),
                                        fieldWithPath("data[].children[].categoryDepth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("하위 카테고리 깊이"),
                                        fieldWithPath("data[].children[].parentCategoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("부모 카테고리 ID"),
                                        fieldWithPath("data[].children[].children")
                                                .type(JsonFieldType.ARRAY)
                                                .description("하위 카테고리의 하위 목록"),
                                        fieldWithPath("data[].children[].children[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("3단계 카테고리 ID")
                                                .optional(),
                                        fieldWithPath("data[].children[].children[].categoryName")
                                                .type(JsonFieldType.STRING)
                                                .description("3단계 카테고리명")
                                                .optional(),
                                        fieldWithPath("data[].children[].children[].categoryDepth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("3단계 카테고리 깊이")
                                                .optional(),
                                        fieldWithPath(
                                                        "data[].children[].children[].parentCategoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("3단계 부모 ID")
                                                .optional(),
                                        fieldWithPath("data[].children[].children[].children")
                                                .type(JsonFieldType.ARRAY)
                                                .description("4단계 하위 목록 (비어있음)")
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
