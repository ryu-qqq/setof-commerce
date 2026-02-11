package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.BrandAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.BrandAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper.BrandAdminV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.port.in.query.SearchBrandByOffsetUseCase;
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
 * BrandAdminQueryV1Controller REST Docs 테스트.
 *
 * <p>브랜드 Admin Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandAdminQueryV1Controller REST Docs 테스트")
@WebMvcTest(BrandAdminQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.admin.TestConfiguration.class)
class BrandAdminQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchBrandByOffsetUseCase searchBrandByOffsetUseCase;

    @MockBean private BrandAdminV1ApiMapper mapper;

    @Nested
    @DisplayName("브랜드 목록 검색 API")
    class FetchBrandsTest {

        @Test
        @DisplayName("브랜드 목록 검색 성공 (US)")
        void fetchBrands_US_Success() throws Exception {
            // given
            BrandPageResult pageResult = BrandAdminApiFixtures.brandPageResult();
            CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                    BrandAdminApiFixtures.brandPageResponse();

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchBrandByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(pageResult)).willReturn(response);

            // when & then
            mockMvc.perform(
                            get(BrandAdminV1Endpoints.BRANDS)
                                    .param("brandName", "Nike")
                                    .param("mainDisplayType", "US")
                                    .param("page", "0")
                                    .param("size", "20")
                                    .param("sortDirection", "ASC"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(2))
                    .andExpect(jsonPath("$.data.content[0].brandId").value(1L))
                    .andExpect(jsonPath("$.data.content[0].brandName").value("Nike"))
                    .andExpect(jsonPath("$.data.content[0].mainDisplayType").value("US"))
                    .andExpect(jsonPath("$.data.totalElements").value(2L))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("brandName")
                                                    .description(
                                                            "브랜드명 검색어 (mainDisplayType에 따라 한글/영문"
                                                                    + " 검색)")
                                                    .optional(),
                                            parameterWithName("mainDisplayType")
                                                    .description(
                                                            "메인 표시 타입. 검색 필드 결정 (US: 영문명, KR: 한글명)")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 (0부터 시작)")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 (1~100)")
                                                    .optional(),
                                            parameterWithName("sortDirection")
                                                    .description("정렬 방향 (ASC, DESC)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 응답 데이터"),
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("브랜드 목록"),
                                            fieldWithPath("data.content[].brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data.content[].brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명 (시스템 내부 식별용)"),
                                            fieldWithPath("data.content[].mainDisplayType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "메인 표시 타입 (US: 영문명 우선, KR: 한글명 우선)"),
                                            fieldWithPath("data.content[].displayEnglishName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("영문 표시명"),
                                            fieldWithPath("data.content[].displayKoreanName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("한글 표시명"),
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
                                                    .description("현재 페이지 번호 (0부터 시작)"),
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
                                                    .description("마지막 도메인 ID")
                                                    .optional(),
                                            fieldWithPath("response")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 메타 정보"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("브랜드 목록 검색 성공 (KR)")
        void fetchBrands_KR_Success() throws Exception {
            // given
            BrandPageResult pageResult = BrandAdminApiFixtures.brandPageResult();
            CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                    BrandAdminApiFixtures.brandPageResponse();

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchBrandByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(pageResult)).willReturn(response);

            // when & then
            mockMvc.perform(
                            get(BrandAdminV1Endpoints.BRANDS)
                                    .param("brandName", "나이키")
                                    .param("mainDisplayType", "KR"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray());
        }

        @Test
        @DisplayName("브랜드 빈 목록 조회 성공")
        void fetchBrands_Empty_Success() throws Exception {
            // given
            BrandPageResult emptyPageResult = BrandAdminApiFixtures.emptyBrandPageResult();
            CustomPageableV1ApiResponse<BrandV1ApiResponse> emptyResponse =
                    BrandAdminApiFixtures.emptyBrandPageResponse();

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchBrandByOffsetUseCase.execute(any())).willReturn(emptyPageResult);
            given(mapper.toPageResponse(emptyPageResult)).willReturn(emptyResponse);

            // when & then
            mockMvc.perform(get(BrandAdminV1Endpoints.BRANDS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(0))
                    .andExpect(jsonPath("$.data.totalElements").value(0L))
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("브랜드 검색 파라미터 없이 조회 성공")
        void fetchBrands_NoParams_Success() throws Exception {
            // given
            BrandPageResult pageResult = BrandAdminApiFixtures.brandPageResult();
            CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                    BrandAdminApiFixtures.brandPageResponse();

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchBrandByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(pageResult)).willReturn(response);

            // when & then
            mockMvc.perform(get(BrandAdminV1Endpoints.BRANDS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("브랜드 검색 페이징 처리 성공")
        void fetchBrands_WithPaging_Success() throws Exception {
            // given
            BrandPageResult pageResult = BrandAdminApiFixtures.brandPageResult(1, 10, 25L);
            CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                    BrandAdminApiFixtures.brandPageResponse(1, 10, 25L);

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchBrandByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(pageResult)).willReturn(response);

            // when & then
            mockMvc.perform(
                            get(BrandAdminV1Endpoints.BRANDS)
                                    .param("page", "1")
                                    .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.number").value(1))
                    .andExpect(jsonPath("$.data.size").value(10))
                    .andExpect(jsonPath("$.data.totalElements").value(25L))
                    .andExpect(jsonPath("$.data.totalPages").value(3));
        }
    }
}
