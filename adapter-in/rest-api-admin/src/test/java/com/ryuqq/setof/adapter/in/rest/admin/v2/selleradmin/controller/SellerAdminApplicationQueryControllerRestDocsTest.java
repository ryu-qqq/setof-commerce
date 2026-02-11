package com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.controller;

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

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.selleradmin.SellerAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.mapper.SellerAdminApplicationQueryApiMapper;
import com.ryuqq.setof.application.selleradmin.dto.query.GetSellerAdminApplicationQuery;
import com.ryuqq.setof.application.selleradmin.dto.query.SellerAdminApplicationSearchParams;
import com.ryuqq.setof.application.selleradmin.dto.response.SellerAdminApplicationPageResult;
import com.ryuqq.setof.application.selleradmin.dto.response.SellerAdminApplicationResult;
import com.ryuqq.setof.application.selleradmin.port.in.query.GetSellerAdminApplicationUseCase;
import com.ryuqq.setof.application.selleradmin.port.in.query.SearchSellerAdminApplicationsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * SellerAdminApplicationQueryController REST Docs 테스트.
 *
 * <p>셀러 관리자 가입 신청 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("SellerAdminApplicationQueryController REST Docs 테스트")
@WebMvcTest(SellerAdminApplicationQueryController.class)
@WithMockUser
class SellerAdminApplicationQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchSellerAdminApplicationsUseCase searchUseCase;

    @MockBean private GetSellerAdminApplicationUseCase getUseCase;

    @MockBean private SellerAdminApplicationQueryApiMapper mapper;

    @Nested
    @DisplayName("셀러 관리자 가입 신청 목록 조회 API")
    class SearchSellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 가입 신청 목록 조회 성공")
        void searchSellerAdmin_Success() throws Exception {
            // given
            SellerAdminApplicationSearchParams searchParams =
                    SellerAdminApplicationSearchParams.of(null, null, null, null, null, null);
            SellerAdminApplicationPageResult pageResult =
                    SellerAdminApiFixtures.applicationPageResult();

            given(mapper.toSearchParams(any())).willReturn(searchParams);
            given(searchUseCase.execute(any())).willReturn(pageResult);

            // when & then
            mockMvc.perform(
                            get("/api/v2/admin/seller-admin-applications")
                                    .param("status", "PENDING_APPROVAL")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.totalElements").value(2))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("sellerIds")
                                                    .description(
                                                            "셀러 ID 목록 +\n"
                                                                    + "슈퍼관리자: 생략 시 전체 조회 +\n"
                                                                    + "셀러관리자: 서버에서 자동 적용")
                                                    .optional(),
                                            parameterWithName("status")
                                                    .description(
                                                            "상태 필터 (복수 선택 가능) +\n"
                                                                    + "PENDING_APPROVAL: 승인대기,"
                                                                    + " ACTIVE: 활성,"
                                                                    + " REJECTED: 거절 +\n"
                                                                    + "미입력 시 전체 조회")
                                                    .optional(),
                                            parameterWithName("searchField")
                                                    .description(
                                                            "검색 필드 +\n"
                                                                    + "loginId: 로그인 ID,"
                                                                    + " name: 이름")
                                                    .optional(),
                                            parameterWithName("searchWord")
                                                    .description("검색어")
                                                    .optional(),
                                            parameterWithName("sortKey")
                                                    .description("정렬 키 +\n" + "기본값: createdAt")
                                                    .optional(),
                                            parameterWithName("sortDirection")
                                                    .description(
                                                            "정렬 방향 (ASC, DESC) +\n" + "기본값: DESC")
                                                    .optional(),
                                            parameterWithName("startDate")
                                                    .description("검색 시작일 (YYYY-MM-DD)")
                                                    .optional(),
                                            parameterWithName("endDate")
                                                    .description("검색 종료일 (YYYY-MM-DD)")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 [0 이상] +\n" + "기본값: 0")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 [1~100] +\n" + "기본값: 20")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.content")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("신청 목록"),
                                            fieldWithPath("data.content[].sellerAdminId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러 관리자 ID (UUIDv7)"),
                                            fieldWithPath("data.content[].sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.content[].loginId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로그인 ID"),
                                            fieldWithPath("data.content[].name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("관리자 이름"),
                                            fieldWithPath("data.content[].phoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("휴대폰 번호"),
                                            fieldWithPath("data.content[].status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상태"),
                                            fieldWithPath("data.content[].authUserId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("인증 서버 사용자 ID (승인 후)")
                                                    .optional(),
                                            fieldWithPath("data.content[].createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시"),
                                            fieldWithPath("data.content[].updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("data.page")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 개수"),
                                            fieldWithPath("data.totalPages")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 페이지 수"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 ID")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("셀러 관리자 가입 신청 상세 조회 API")
    class GetSellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 가입 신청 상세 조회 성공")
        void getSellerAdmin_Success() throws Exception {
            // given
            String sellerAdminId = SellerAdminApiFixtures.sellerAdminId();
            SellerAdminApplicationResult result =
                    SellerAdminApiFixtures.applicationResult(sellerAdminId);
            GetSellerAdminApplicationQuery query = GetSellerAdminApplicationQuery.of(sellerAdminId);

            given(mapper.toGetQuery(any())).willReturn(query);
            given(getUseCase.execute(any())).willReturn(result);

            // when & then
            mockMvc.perform(
                            get(
                                    "/api/v2/admin/seller-admin-applications/{sellerAdminId}",
                                    sellerAdminId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerAdminId").value(sellerAdminId))
                    .andExpect(jsonPath("$.data.status").value("PENDING_APPROVAL"))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerAdminId")
                                                    .description("셀러 관리자 ID (UUIDv7)")),
                                    responseFields(
                                            fieldWithPath("data.sellerAdminId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러 관리자 ID"),
                                            fieldWithPath("data.sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.loginId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로그인 ID"),
                                            fieldWithPath("data.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("관리자 이름"),
                                            fieldWithPath("data.phoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("휴대폰 번호"),
                                            fieldWithPath("data.status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상태"),
                                            fieldWithPath("data.authUserId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("인증 서버 사용자 ID (승인 후)")
                                                    .optional(),
                                            fieldWithPath("data.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시"),
                                            fieldWithPath("data.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 ID")
                                                    .optional())));
        }
    }
}
