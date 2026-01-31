package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.commoncode.CommonCodeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.response.CommonCodeApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.mapper.CommonCodeQueryApiMapper;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodePageResult;
import com.ryuqq.setof.application.commoncode.port.in.query.SearchCommonCodeUseCase;
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
 * CommonCodeQueryController REST Docs 테스트.
 *
 * <p>공통코드 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommonCodeQueryController REST Docs 테스트")
@WebMvcTest(CommonCodeQueryController.class)
@WithMockUser
class CommonCodeQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchCommonCodeUseCase searchCommonCodeUseCase;

    @MockBean private CommonCodeQueryApiMapper mapper;

    @Nested
    @DisplayName("공통코드 목록 조회 API")
    class SearchCommonCodesTest {

        @Test
        @DisplayName("공통코드 목록 조회 성공")
        void searchCommonCodes_Success() throws Exception {
            // given
            CommonCodePageResult pageResult = CommonCodeApiFixtures.pageResult();
            PageApiResponse<CommonCodeApiResponse> response = createPageResponse();

            given(searchCommonCodeUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get("/api/v2/admin/common-codes")
                                    .param("commonCodeTypeId", "1")
                                    .param("active", "true")
                                    .param("code", "CARD")
                                    .param("sortKey", "CREATED_AT")
                                    .param("sortDirection", "DESC")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("commonCodeTypeId")
                                                    .description("공통 코드 타입 ID [필수]"),
                                            parameterWithName("active")
                                                    .description(
                                                            "활성화 여부 필터 +\n"
                                                                    + "true: 활성만, false: 비활성만 +\n"
                                                                    + "미입력 시 전체 조회")
                                                    .optional(),
                                            parameterWithName("code")
                                                    .description("코드 검색 +\n" + "부분 일치 검색")
                                                    .optional(),
                                            parameterWithName("sortKey")
                                                    .description(
                                                            "정렬 키 +\n"
                                                                    + "CREATED_AT: 생성일 (기본값), "
                                                                    + "DISPLAY_ORDER: 표시순서, "
                                                                    + "CODE: 코드")
                                                    .optional(),
                                            parameterWithName("sortDirection")
                                                    .description(
                                                            "정렬 방향 +\n"
                                                                    + "DESC: 내림차순 (기본값), "
                                                                    + "ASC: 오름차순")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 [0 이상] +\n" + "기본값: 0")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 [1~100] +\n" + "기본값: 20")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("공통코드 목록"),
                                            fieldWithPath("data.content[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("공통코드 ID"),
                                            fieldWithPath("data.content[].commonCodeTypeId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("공통 코드 타입 ID"),
                                            fieldWithPath("data.content[].code")
                                                    .type(JsonFieldType.STRING)
                                                    .description("코드"),
                                            fieldWithPath("data.content[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.content[].displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("표시 순서"),
                                            fieldWithPath("data.content[].active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 상태"),
                                            fieldWithPath("data.content[].createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시 (ISO 8601)"),
                                            fieldWithPath("data.content[].updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (ISO 8601)"),
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
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }

        @Test
        @DisplayName("공통코드 빈 목록 조회 성공")
        void searchCommonCodes_Empty_Success() throws Exception {
            // given
            CommonCodePageResult emptyResult = CommonCodeApiFixtures.emptyPageResult();
            PageApiResponse<CommonCodeApiResponse> emptyResponse =
                    PageApiResponse.of(List.of(), 0, 20, 0);

            given(searchCommonCodeUseCase.execute(any())).willReturn(emptyResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(emptyResponse);

            // when & then
            mockMvc.perform(
                            get("/api/v2/admin/common-codes")
                                    .param("commonCodeTypeId", "1")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isEmpty());
        }
    }

    private PageApiResponse<CommonCodeApiResponse> createPageResponse() {
        CommonCodeApiResponse codeResponse =
                new CommonCodeApiResponse(
                        100L,
                        1L,
                        "CARD",
                        "신용카드",
                        1,
                        true,
                        "2025-01-26T10:30:00+09:00",
                        "2025-01-26T10:30:00+09:00");

        return PageApiResponse.of(List.of(codeResponse), 0, 20, 1);
    }
}
