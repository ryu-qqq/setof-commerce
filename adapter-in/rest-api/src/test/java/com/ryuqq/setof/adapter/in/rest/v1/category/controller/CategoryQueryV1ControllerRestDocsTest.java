package com.ryuqq.setof.adapter.in.rest.v1.category.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.category.CategoryApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.category.CategoryV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.category.mapper.CategoryV1ApiMapper;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesForDisplayUseCase;
import java.util.List;
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
 * CategoryQueryV1Controller REST Docs 테스트.
 *
 * <p>카테고리 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CategoryQueryV1Controller REST Docs 테스트")
@WebMvcTest(CategoryQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class CategoryQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetCategoriesForDisplayUseCase getCategoriesForDisplayUseCase;

    @MockBean private CategoryV1ApiMapper mapper;

    @Nested
    @DisplayName("카테고리 트리 조회 API")
    class GetCategoriesTest {

        @Test
        @DisplayName("카테고리 트리 조회 성공")
        void getCategories_Success() throws Exception {
            // given
            List<CategoryDisplayResult> results = CategoryApiFixtures.displayResultTreeList();
            List<TreeCategoryV1ApiResponse> response =
                    CategoryApiFixtures.categoryResponseTreeList();

            given(getCategoriesForDisplayUseCase.execute()).willReturn(results);
            given(mapper.toListResponse(results)).willReturn(response);

            // when & then
            mockMvc.perform(get(CategoryV1Endpoints.CATEGORY))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.data[0].categoryId").value(1))
                    .andExpect(jsonPath("$.data[0].categoryName").value("여성"))
                    .andExpect(jsonPath("$.data[0].children.length()").value(2))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data[]")
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
                                                    .description("카테고리 depth (1=루트)"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID (루트면 0)"),
                                            fieldWithPath("data[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("하위 카테고리 목록 (재귀 구조)"),
                                            fieldWithPath("data[].children[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("자식 카테고리 ID")
                                                    .optional(),
                                            fieldWithPath("data[].children[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("자식 카테고리명")
                                                    .optional(),
                                            fieldWithPath("data[].children[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("자식 카테고리 depth")
                                                    .optional(),
                                            fieldWithPath("data[].children[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("자식의 부모 카테고리 ID")
                                                    .optional(),
                                            fieldWithPath("data[].children[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("자식의 하위 카테고리 목록 (재귀)")
                                                    .optional(),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("빈 카테고리 트리 조회 성공")
        void getCategories_Empty_Success() throws Exception {
            // given
            given(getCategoriesForDisplayUseCase.execute()).willReturn(List.of());
            given(mapper.toListResponse(List.of())).willReturn(List.of());

            // when & then
            mockMvc.perform(get(CategoryV1Endpoints.CATEGORY))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0))
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("다단계 카테고리 트리 조회 성공")
        void getCategories_MultiLevel_Success() throws Exception {
            // given
            CategoryDisplayResult grandChild =
                    CategoryApiFixtures.displayResult(111L, "티셔츠", 11L, 3);
            CategoryDisplayResult child =
                    CategoryApiFixtures.displayResultWithChildren(
                            11L, "상의", 1L, 2, List.of(grandChild));
            CategoryDisplayResult parent =
                    CategoryApiFixtures.displayResultWithChildren(1L, "여성", 0L, 1, List.of(child));
            List<CategoryDisplayResult> results = List.of(parent);

            TreeCategoryV1ApiResponse grandChildResponse =
                    CategoryApiFixtures.categoryResponse(111L, "티셔츠", 3, 11L);
            TreeCategoryV1ApiResponse childResponse =
                    CategoryApiFixtures.categoryResponseWithChildren(
                            11L, "상의", 2, 1L, List.of(grandChildResponse));
            TreeCategoryV1ApiResponse parentResponse =
                    CategoryApiFixtures.categoryResponseWithChildren(
                            1L, "여성", 1, 0L, List.of(childResponse));
            List<TreeCategoryV1ApiResponse> response = List.of(parentResponse);

            given(getCategoriesForDisplayUseCase.execute()).willReturn(results);
            given(mapper.toListResponse(results)).willReturn(response);

            // when & then
            mockMvc.perform(get(CategoryV1Endpoints.CATEGORY))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").value(1))
                    .andExpect(jsonPath("$.data[0].children[0].categoryId").value(11))
                    .andExpect(jsonPath("$.data[0].children[0].children[0].categoryId").value(111))
                    .andExpect(
                            jsonPath("$.data[0].children[0].children[0].categoryName").value("티셔츠"))
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
