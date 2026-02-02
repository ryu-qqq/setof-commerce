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

import com.ryuqq.setof.adapter.in.rest.admin.brand.BrandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandV1ApiResponse;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * BrandQueryV1Controller REST Docs 테스트.
 *
 * <p>브랜드 조회 V1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandQueryV1Controller REST Docs 테스트")
@WebMvcTest(BrandQueryV1Controller.class)
@WithMockUser
class BrandQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchBrandByOffsetUseCase searchBrandByOffsetUseCase;

    @MockBean private BrandAdminV1ApiMapper mapper;

    @Nested
    @DisplayName("브랜드 목록 조회 API")
    class SearchBrandsTest {

        @Test
        @DisplayName("브랜드 목록 조회 성공")
        void searchBrands_Success() throws Exception {
            // given
            BrandPageResult pageResult = BrandV1ApiFixtures.pageResult();
            CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> response =
                    BrandV1ApiFixtures.pageResponse();

            given(searchBrandByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get("/api/v1/brands")
                                    .param("brandName", "NIKE")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].brandId").exists())
                    .andExpect(jsonPath("$.data.content[0].brandName").exists())
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("brandName")
                                                    .description("브랜드명 검색 +\n" + "부분 일치 검색")
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
                                                    .description("브랜드 목록"),
                                            fieldWithPath("data.content[].brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data.content[].brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명"),
                                            fieldWithPath("data.content[].mainDisplayType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메인 표시 타입 (ENGLISH, KOREAN)"),
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

        @Test
        @DisplayName("브랜드 목록 조회 - 결과 없음")
        void searchBrands_Empty() throws Exception {
            // given
            BrandPageResult pageResult = BrandV1ApiFixtures.emptyPageResult();
            CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> response =
                    CustomPageableV1ApiResponse.of(java.util.List.of(), 0, 20, 0L);

            given(searchBrandByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/brands").param("brandName", "존재하지않는브랜드"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isEmpty())
                    .andExpect(jsonPath("$.data.totalElements").value(0));
        }
    }
}
