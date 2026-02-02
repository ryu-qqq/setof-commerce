package com.ryuqq.setof.adapter.in.rest.admin.v1.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.category.CategoryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper.CategoryAdminV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.port.in.query.GetAllCategoriesAsTreeUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetChildCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetParentCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.SearchCategoryByOffsetUseCase;
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

    @MockBean private GetAllCategoriesAsTreeUseCase getAllCategoriesAsTreeUseCase;

    @MockBean private GetChildCategoriesUseCase getChildCategoriesUseCase;

    @MockBean private GetParentCategoriesUseCase getParentCategoriesUseCase;

    @MockBean private SearchCategoryByOffsetUseCase searchCategoryByOffsetUseCase;

    @MockBean private CategoryAdminV1ApiMapper mapper;

    @Nested
    @DisplayName("전체 카테고리 트리 조회 API")
    class GetAllCategoriesAsTreeTest {

        @Test
        @DisplayName("전체 카테고리 트리 조회 성공")
        void getAllCategoriesAsTree_Success() throws Exception {
            // given
            List<TreeCategoryResult> results = CategoryV1ApiFixtures.multipleTreeCategoryResults();
            List<TreeCategoryV1ApiResponse> response =
                    List.of(
                            CategoryV1ApiFixtures.treeCategoryResponse(),
                            new TreeCategoryV1ApiResponse(10L, "신발", "신발", 1, 0L, List.of()),
                            new TreeCategoryV1ApiResponse(20L, "가방", "가방", 1, 0L, List.of()));

            given(getAllCategoriesAsTreeUseCase.execute()).willReturn(results);
            given(mapper.toTreeResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/category"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").exists())
                    .andExpect(jsonPath("$.data[0].categoryName").exists())
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
                                            fieldWithPath("data[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
                                            fieldWithPath("data[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("자식 카테고리 목록")
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
    }

    @Nested
    @DisplayName("자식 카테고리 조회 API")
    class GetChildCategoriesTest {

        @Test
        @DisplayName("자식 카테고리 조회 성공")
        void getChildCategories_Success() throws Exception {
            // given
            List<TreeCategoryResult> results =
                    List.of(
                            TreeCategoryResult.leaf(2L, "상의", 1L, 2, true, "/1/2"),
                            TreeCategoryResult.leaf(3L, "하의", 1L, 2, true, "/1/3"));
            List<TreeCategoryV1ApiResponse> response =
                    List.of(
                            new TreeCategoryV1ApiResponse(2L, "상의", "상의", 2, 1L, List.of()),
                            new TreeCategoryV1ApiResponse(3L, "하의", "하의", 2, 1L, List.of()));

            given(getChildCategoriesUseCase.execute(anyLong())).willReturn(results);
            given(mapper.toTreeResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/category/{categoryId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").value(2))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("categoryId")
                                                    .description("부모 카테고리 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("자식 카테고리 목록"),
                                            fieldWithPath("data[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath("data[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
                                            fieldWithPath("data[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("자식 카테고리 목록")
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
    }

    @Nested
    @DisplayName("부모 카테고리 조회 API")
    class GetParentCategoriesTest {

        @Test
        @DisplayName("부모 카테고리 조회 성공")
        void getParentCategories_Success() throws Exception {
            // given
            List<TreeCategoryResult> results =
                    List.of(TreeCategoryResult.leaf(1L, "의류", 0L, 1, true, "/1"));
            List<TreeCategoryV1ApiResponse> response =
                    List.of(new TreeCategoryV1ApiResponse(1L, "의류", "의류", 1, 0L, List.of()));

            given(getParentCategoriesUseCase.execute(anyLong())).willReturn(results);
            given(mapper.toTreeResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/category/parent/{categoryId}", 10L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("categoryId")
                                                    .description("자식 카테고리 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("부모 카테고리 목록"),
                                            fieldWithPath("data[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath("data[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
                                            fieldWithPath("data[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("자식 카테고리 목록")
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
    }

    @Nested
    @DisplayName("카테고리 페이징 조회 API")
    class SearchCategoriesTest {

        @Test
        @DisplayName("카테고리 목록 조회 성공")
        void searchCategories_Success() throws Exception {
            // given
            CategoryPageResult pageResult = CategoryV1ApiFixtures.pageResult();
            CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> response =
                    CategoryV1ApiFixtures.pageResponse();

            given(searchCategoryByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get("/api/v1/category/page")
                                    .param("categoryName", "의류")
                                    .param("depth", "1")
                                    .param("displayed", "true")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("categoryName")
                                                    .description("카테고리명 검색 +\n" + "부분 일치 검색")
                                                    .optional(),
                                            parameterWithName("depth")
                                                    .description("카테고리 깊이 필터")
                                                    .optional(),
                                            parameterWithName("displayed")
                                                    .description(
                                                            "노출 여부 필터 +\n" + "true: 노출, false: 미노출")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 +\n" + "0부터 시작 (기본값: 0)")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 +\n" + "기본값: 20")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 데이터"),
                                            fieldWithPath("data.content")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("카테고리 목록"),
                                            fieldWithPath("data.content[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath("data.content[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data.content[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.content[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data.content[].targetGroup")
                                                    .type(JsonFieldType.STRING)
                                                    .description("타겟 그룹"),
                                            fieldWithPath("data.content[].categoryFullPath")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리 전체 경로"),
                                            fieldWithPath("data.pageable")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 정보"),
                                            fieldWithPath("data.pageable.pageNumber")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 번호"),
                                            fieldWithPath("data.pageable.pageSize")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
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
                                                    .description("전체 데이터 수"),
                                            fieldWithPath("data.totalPages")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 페이지 수"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 페이지 여부"),
                                            fieldWithPath("data.numberOfElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 데이터 수"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.number")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                            fieldWithPath("data.sort")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정렬 정보"),
                                            fieldWithPath("data.sort.unsorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비정렬 여부"),
                                            fieldWithPath("data.sort.sorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬 여부"),
                                            fieldWithPath("data.sort.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("빈 정렬 여부"),
                                            fieldWithPath("data.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("빈 페이지 여부"),
                                            fieldWithPath("data.lastDomainId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("마지막 도메인 ID")
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
    }
}
