package com.ryuqq.setof.adapter.in.rest.v1.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.category.CategoryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryDisplayV1ApiResponse;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * CategoryQueryV1Controller REST Docs 테스트.
 *
 * <p>카테고리 조회 V1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CategoryQueryV1Controller REST Docs 테스트")
@WebMvcTest(CategoryQueryV1Controller.class)
@WithMockUser
class CategoryQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetCategoriesForDisplayUseCase getCategoriesForDisplayUseCase;

    @MockBean private CategoryV1ApiMapper mapper;

    @Nested
    @DisplayName("전체 카테고리 트리 조회 API")
    class GetAllCategoriesAsTreeTest {

        @Test
        @DisplayName("전체 카테고리 트리 조회 성공")
        void getAllCategoriesAsTree_Success() throws Exception {
            // given
            List<CategoryDisplayResult> results =
                    CategoryV1ApiFixtures.treeCategoryDisplayResults();
            List<CategoryDisplayV1ApiResponse> response =
                    List.of(
                            CategoryV1ApiFixtures.categoryDisplayResponseWithChildren(),
                            new CategoryDisplayV1ApiResponse(10L, "신발", 1, 0L, List.of()),
                            new CategoryDisplayV1ApiResponse(20L, "가방", 1, 0L, List.of()));

            given(getCategoriesForDisplayUseCase.execute()).willReturn(results);
            given(mapper.toListResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/category"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").exists())
                    .andExpect(jsonPath("$.data[0].categoryName").exists())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
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
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
                                            fieldWithPath("data[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("자식 카테고리 목록"),
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
                                                    .description("자식 카테고리 깊이")
                                                    .optional(),
                                            fieldWithPath("data[].children[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("자식의 부모 카테고리 ID")
                                                    .optional(),
                                            fieldWithPath("data[].children[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("손자 카테고리 목록")
                                                    .optional(),
                                            fieldWithPath("response")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 메타 정보"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("카테고리 트리 조회 - 결과 없음")
        void getAllCategoriesAsTree_Empty() throws Exception {
            // given
            List<CategoryDisplayResult> results = List.of();
            List<CategoryDisplayV1ApiResponse> response = List.of();

            given(getCategoriesForDisplayUseCase.execute()).willReturn(results);
            given(mapper.toListResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/category"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("리프 노드 카테고리 조회")
        void getAllCategoriesAsTree_LeafNodes() throws Exception {
            // given
            List<CategoryDisplayResult> results =
                    CategoryV1ApiFixtures.multipleCategoryDisplayResults();
            List<CategoryDisplayV1ApiResponse> response =
                    CategoryV1ApiFixtures.multipleCategoryDisplayResponses();

            given(getCategoriesForDisplayUseCase.execute()).willReturn(results);
            given(mapper.toListResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/category"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(3))
                    .andExpect(jsonPath("$.data[0].children").isEmpty())
                    .andExpect(jsonPath("$.data[1].children").isEmpty())
                    .andExpect(jsonPath("$.data[2].children").isEmpty());
        }
    }
}
