package com.ryuqq.setof.adapter.in.rest.v2.category.controller;

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
import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryPathUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryTreeUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryUseCase;
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
 * CategoryV2Controller REST Docs 테스트
 *
 * <p>카테고리 정보 조회 API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = CategoryV2Controller.class)
@Import({CategoryV2Controller.class, CategoryV2ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("CategoryV2Controller REST Docs")
class CategoryV2ControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private GetCategoryUseCase getCategoryUseCase;
    @MockitoBean private GetCategoriesUseCase getCategoriesUseCase;
    @MockitoBean private GetCategoryTreeUseCase getCategoryTreeUseCase;
    @MockitoBean private GetCategoryPathUseCase getCategoryPathUseCase;

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
    @DisplayName("GET /api/v2/categories/{categoryId} - 카테고리 단건 조회 API 문서")
    void getCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryResponse categoryResponse =
                new CategoryResponse(
                        categoryId, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE");

        when(getCategoryUseCase.getCategory(categoryId)).thenReturn(categoryResponse);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.Categories.BASE + "/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(categoryId))
                .andExpect(jsonPath("$.data.code").value("FASHION"))
                .andExpect(jsonPath("$.data.nameKo").value("패션"))
                .andExpect(jsonPath("$.data.depth").value(0))
                .andDo(
                        document(
                                "category-detail",
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")),
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
                                        fieldWithPath("data.categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 ID"),
                                        fieldWithPath("data.code")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리 코드"),
                                        fieldWithPath("data.nameKo")
                                                .type(JsonFieldType.STRING)
                                                .description("한글 카테고리명"),
                                        fieldWithPath("data.parentId")
                                                .type(JsonFieldType.NULL)
                                                .description("부모 카테고리 ID (최상위는 null)")
                                                .optional(),
                                        fieldWithPath("data.depth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("깊이 (0=최상위)"),
                                        fieldWithPath("data.path")
                                                .type(JsonFieldType.STRING)
                                                .description("경로 (Path Enumeration)"),
                                        fieldWithPath("data.sortOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("data.isLeaf")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("리프 노드 여부"),
                                        fieldWithPath("data.status")
                                                .type(JsonFieldType.STRING)
                                                .description("상태 (ACTIVE/INACTIVE)"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName("GET /api/v2/categories - 하위 카테고리 목록 조회 API 문서")
    void getCategories() throws Exception {
        // Given
        List<CategoryResponse> categories =
                List.of(
                        new CategoryResponse(
                                1L, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE"),
                        new CategoryResponse(
                                2L, "ELECTRONICS", "전자제품", null, 0, "/2/", 2, false, "ACTIVE"),
                        new CategoryResponse(
                                3L, "HOME", "가구/인테리어", null, 0, "/3/", 3, false, "ACTIVE"));

        when(getCategoriesUseCase.getCategories(null)).thenReturn(categories);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.Categories.BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].categoryId").value(1))
                .andExpect(jsonPath("$.data[0].code").value("FASHION"))
                .andDo(
                        document(
                                "category-list",
                                queryParameters(
                                        parameterWithName("parentId")
                                                .description("부모 카테고리 ID (없으면 최상위 조회)")
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
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("카테고리 목록"),
                                        fieldWithPath("data[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 ID"),
                                        fieldWithPath("data[].code")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리 코드"),
                                        fieldWithPath("data[].nameKo")
                                                .type(JsonFieldType.STRING)
                                                .description("한글 카테고리명"),
                                        fieldWithPath("data[].parentId")
                                                .type(JsonFieldType.NULL)
                                                .description("부모 카테고리 ID")
                                                .optional(),
                                        fieldWithPath("data[].depth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("깊이"),
                                        fieldWithPath("data[].path")
                                                .type(JsonFieldType.STRING)
                                                .description("경로"),
                                        fieldWithPath("data[].sortOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("data[].isLeaf")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("리프 노드 여부"),
                                        fieldWithPath("data[].status")
                                                .type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName("GET /api/v2/categories/tree - 카테고리 트리 조회 API 문서")
    void getCategoryTree() throws Exception {
        // Given
        List<CategoryTreeResponse> treeResponse =
                List.of(
                        new CategoryTreeResponse(
                                1L,
                                "FASHION",
                                "패션",
                                0,
                                1,
                                false,
                                List.of(
                                        new CategoryTreeResponse(
                                                5L, "CLOTHING", "의류", 1, 1, false, List.of()))));

        when(getCategoryTreeUseCase.getCategoryTree()).thenReturn(treeResponse);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.Categories.BASE + "/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].categoryId").value(1))
                .andExpect(jsonPath("$.data[0].children").isArray())
                .andDo(
                        document(
                                "category-tree",
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
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("카테고리 트리"),
                                        fieldWithPath("data[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 ID"),
                                        fieldWithPath("data[].code")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리 코드"),
                                        fieldWithPath("data[].nameKo")
                                                .type(JsonFieldType.STRING)
                                                .description("한글 카테고리명"),
                                        fieldWithPath("data[].depth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("깊이"),
                                        fieldWithPath("data[].sortOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("data[].isLeaf")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("리프 노드 여부"),
                                        fieldWithPath("data[].children")
                                                .type(JsonFieldType.ARRAY)
                                                .description("하위 카테고리"),
                                        fieldWithPath("data[].children[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("하위 카테고리 ID"),
                                        fieldWithPath("data[].children[].code")
                                                .type(JsonFieldType.STRING)
                                                .description("하위 카테고리 코드"),
                                        fieldWithPath("data[].children[].nameKo")
                                                .type(JsonFieldType.STRING)
                                                .description("하위 카테고리명"),
                                        fieldWithPath("data[].children[].depth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("깊이"),
                                        fieldWithPath("data[].children[].sortOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("정렬 순서"),
                                        fieldWithPath("data[].children[].isLeaf")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("리프 노드 여부"),
                                        fieldWithPath("data[].children[].children")
                                                .type(JsonFieldType.ARRAY)
                                                .description("하위의 하위 카테고리"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName("GET /api/v2/categories/{categoryId}/path - 카테고리 경로 조회 API 문서")
    void getCategoryPath() throws Exception {
        // Given
        Long categoryId = 23L;
        CategoryPathResponse pathResponse =
                new CategoryPathResponse(
                        categoryId,
                        List.of(
                                new CategoryPathResponse.BreadcrumbItem(1L, "FASHION", "패션", 0),
                                new CategoryPathResponse.BreadcrumbItem(5L, "CLOTHING", "의류", 1),
                                new CategoryPathResponse.BreadcrumbItem(23L, "TOPS", "상의", 2)));

        when(getCategoryPathUseCase.getCategoryPath(categoryId)).thenReturn(pathResponse);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.Categories.BASE + "/{categoryId}/path", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(categoryId))
                .andExpect(jsonPath("$.data.breadcrumbs").isArray())
                .andExpect(jsonPath("$.data.breadcrumbs.length()").value(3))
                .andDo(
                        document(
                                "category-path",
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 ID")),
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
                                        fieldWithPath("data.categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 카테고리 ID"),
                                        fieldWithPath("data.breadcrumbs")
                                                .type(JsonFieldType.ARRAY)
                                                .description("상위 경로 목록 (최상위 -> 현재)"),
                                        fieldWithPath("data.breadcrumbs[].categoryId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("카테고리 ID"),
                                        fieldWithPath("data.breadcrumbs[].code")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리 코드"),
                                        fieldWithPath("data.breadcrumbs[].nameKo")
                                                .type(JsonFieldType.STRING)
                                                .description("카테고리명"),
                                        fieldWithPath("data.breadcrumbs[].depth")
                                                .type(JsonFieldType.NUMBER)
                                                .description("깊이"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }
}
