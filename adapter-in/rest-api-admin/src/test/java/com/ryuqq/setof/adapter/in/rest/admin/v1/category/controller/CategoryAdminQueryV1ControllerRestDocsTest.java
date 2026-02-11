package com.ryuqq.setof.adapter.in.rest.admin.v1.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.TestConfiguration;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.CategoryAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper.CategoryAdminV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.port.in.query.GetAllCategoriesAsTreeUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesByIdsUseCase;
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
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * CategoryAdminQueryV1Controller REST Docs 테스트.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CategoryAdminQueryV1Controller REST Docs 테스트")
@WebMvcTest(CategoryAdminQueryV1Controller.class)
@WithMockUser
@Import(TestConfiguration.class)
class CategoryAdminQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetAllCategoriesAsTreeUseCase getAllCategoriesAsTreeUseCase;
    @MockBean private GetChildCategoriesUseCase getChildCategoriesUseCase;
    @MockBean private GetParentCategoriesUseCase getParentCategoriesUseCase;
    @MockBean private GetCategoriesByIdsUseCase getCategoriesByIdsUseCase;
    @MockBean private SearchCategoryByOffsetUseCase searchCategoryByOffsetUseCase;
    @MockBean private CategoryAdminV1ApiMapper mapper;

    private static final Long CATEGORY_ID = 100L;

    @Nested
    @DisplayName("전체 카테고리 트리 조회 API")
    class FetchAllCategoriesAsTreeTest {

        @Test
        @DisplayName("전체 카테고리 트리 조회 성공")
        void fetchAllCategoriesAsTree_Success() throws Exception {
            // given
            List<TreeCategoryResult> results =
                    List.of(CategoryAdminApiFixtures.treeCategoryResult(CATEGORY_ID));
            List<TreeCategoryV1ApiResponse> responses =
                    List.of(CategoryAdminApiFixtures.treeCategoryApiResponse(CATEGORY_ID));

            given(getAllCategoriesAsTreeUseCase.execute()).willReturn(results);
            given(mapper.toTreeListResponse(results)).willReturn(responses);

            // when & then
            mockMvc.perform(get("/api/v1/category"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").value(CATEGORY_ID))
                    .andExpect(jsonPath("$.data[0].categoryName").value("의류"))
                    .andExpect(jsonPath("$.data[0].children").isArray())
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
                                                    .description("노출명"),
                                            fieldWithPath("data[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이 (1=루트)"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
                                            fieldWithPath("data[].children")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("하위 카테고리 목록"),
                                            fieldWithPath("data[].children[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("하위 카테고리 ID"),
                                            fieldWithPath("data[].children[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("하위 카테고리명"),
                                            fieldWithPath("data[].children[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("하위 노출명"),
                                            fieldWithPath("data[].children[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("하위 카테고리 깊이"),
                                            fieldWithPath("data[].children[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("하위 카테고리의 부모 ID"),
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
    @DisplayName("하위 카테고리 조회 API")
    class FetchAllChildCategoriesTest {

        @Test
        @DisplayName("하위 카테고리 조회 성공")
        void fetchAllChildCategories_Success() throws Exception {
            // given
            List<TreeCategoryResult> results =
                    List.of(CategoryAdminApiFixtures.leafTreeCategoryResult(CATEGORY_ID + 1));
            List<TreeCategoryV1ApiResponse> responses =
                    List.of(CategoryAdminApiFixtures.leafTreeCategoryApiResponse(CATEGORY_ID + 1));

            given(getChildCategoriesUseCase.execute(CATEGORY_ID)).willReturn(results);
            given(mapper.toTreeLeafResponse(any(TreeCategoryResult.class)))
                    .willReturn(responses.get(0));

            // when & then
            mockMvc.perform(get("/api/v1/category/{categoryId}", CATEGORY_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").value(CATEGORY_ID + 1))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("categoryId").description("카테고리 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("하위 카테고리 목록 (flat)"),
                                            fieldWithPath("data[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath("data[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("노출명"),
                                            fieldWithPath("data[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
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
    @DisplayName("상위 카테고리 조회 API")
    class FetchAllParentCategoriesTest {

        @Test
        @DisplayName("상위 카테고리 조회 성공")
        void fetchAllParentCategories_Success() throws Exception {
            // given
            List<TreeCategoryResult> results =
                    List.of(CategoryAdminApiFixtures.leafTreeCategoryResult(CATEGORY_ID - 1));
            List<TreeCategoryV1ApiResponse> responses =
                    List.of(CategoryAdminApiFixtures.leafTreeCategoryApiResponse(CATEGORY_ID - 1));

            given(getParentCategoriesUseCase.execute(CATEGORY_ID)).willReturn(results);
            given(mapper.toTreeLeafResponse(any(TreeCategoryResult.class)))
                    .willReturn(responses.get(0));

            // when & then
            mockMvc.perform(get("/api/v1/category/parent/{categoryId}", CATEGORY_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").value(CATEGORY_ID - 1))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("categoryId").description("카테고리 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상위 카테고리 목록 (flat)"),
                                            fieldWithPath("data[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath("data[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("노출명"),
                                            fieldWithPath("data[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
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
    @DisplayName("다건 카테고리 조회 API")
    class FetchAllParentCategoriesBulkTest {

        @Test
        @DisplayName("다건 카테고리 조회 성공")
        void fetchAllParentCategoriesBulk_Success() throws Exception {
            // given
            List<Long> categoryIds = List.of(100L, 101L, 102L);
            List<TreeCategoryResult> results =
                    List.of(
                            CategoryAdminApiFixtures.leafTreeCategoryResult(100L),
                            CategoryAdminApiFixtures.leafTreeCategoryResult(101L));
            List<TreeCategoryV1ApiResponse> responses =
                    List.of(
                            CategoryAdminApiFixtures.leafTreeCategoryApiResponse(100L),
                            CategoryAdminApiFixtures.leafTreeCategoryApiResponse(101L));

            given(getCategoriesByIdsUseCase.execute(categoryIds)).willReturn(results);
            given(mapper.toTreeLeafResponse(any(TreeCategoryResult.class)))
                    .willReturn(responses.get(0), responses.get(1));

            // when & then
            mockMvc.perform(
                            get("/api/v1/category/parents")
                                    .param("categoryIds", "100")
                                    .param("categoryIds", "101")
                                    .param("categoryIds", "102"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].categoryId").value(100L))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("categoryIds")
                                                    .description("조회할 카테고리 ID 목록")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("카테고리 목록 (flat)"),
                                            fieldWithPath("data[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 ID"),
                                            fieldWithPath("data[].categoryName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리명"),
                                            fieldWithPath("data[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("노출명"),
                                            fieldWithPath("data[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data[].parentCategoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("부모 카테고리 ID"),
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
    class FetchCategoriesTest {

        @Test
        @DisplayName("카테고리 페이징 조회 성공")
        void fetchCategories_Success() throws Exception {
            // given
            CategoryPageResult pageResult = CategoryAdminApiFixtures.categoryPageResult();
            CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> pageResponse =
                    CategoryAdminApiFixtures.productCategoryPageResponse();

            given(searchCategoryByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(pageResult)).willReturn(pageResponse);

            // when & then
            mockMvc.perform(
                            get("/api/v1/category/page")
                                    .param("categoryName", "의류")
                                    .param("categoryDepth", "2")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.totalElements").value(50))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("categoryName")
                                                    .description("카테고리명 검색 (displayName LIKE)")
                                                    .optional(),
                                            parameterWithName("categoryDepth")
                                                    .description("카테고리 깊이 필터 (1=루트, 2=중분류)")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 (0부터 시작)")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 (1~100)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 응답"),
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
                                                    .description("노출명"),
                                            fieldWithPath("data.content[].categoryDepth")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("카테고리 깊이"),
                                            fieldWithPath("data.content[].categoryFullPath")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카테고리 전체 경로"),
                                            fieldWithPath("data.content[].targetGroup")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대상 그룹 (ALL, MALE, FEMALE, KIDS)"),
                                            fieldWithPath("data.pageable")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 정보"),
                                            fieldWithPath("data.pageable.pageNumber")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
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
                                                    .description("전체 데이터 개수"),
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
                                                    .description("현재 페이지 데이터 개수"),
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
                                                    .type(JsonFieldType.NULL)
                                                    .description("마지막 도메인 ID (null)")
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
