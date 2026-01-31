package com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.sellerapplication.SellerApplicationApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.response.SellerApplicationApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.mapper.SellerApplicationQueryApiMapper;
import com.ryuqq.setof.application.sellerapplication.dto.response.SellerApplicationPageResult;
import com.ryuqq.setof.application.sellerapplication.port.in.query.SearchSellerApplicationByOffsetUseCase;
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
 * SellerApplicationQueryController REST Docs 테스트.
 *
 * <p>셀러 입점 신청 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerApplicationQueryController REST Docs 테스트")
@WebMvcTest(SellerApplicationQueryController.class)
@WithMockUser
class SellerApplicationQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchSellerApplicationByOffsetUseCase searchUseCase;

    @MockBean private SellerApplicationQueryApiMapper mapper;

    @Nested
    @DisplayName("셀러 입점 신청 목록 조회 API")
    class SearchTest {

        @Test
        @DisplayName("셀러 입점 신청 목록 조회 성공")
        void search_Success() throws Exception {
            // given
            SellerApplicationPageResult pageResult =
                    SellerApplicationApiFixtures.applicationPageResult();

            PageApiResponse<SellerApplicationApiResponse> pageResponse =
                    PageApiResponse.of(
                            List.of(
                                    new SellerApplicationApiResponse(
                                            1L,
                                            new SellerApplicationApiResponse.SellerInfo(
                                                    "테스트셀러1", "테스트 브랜드1", null, null),
                                            new SellerApplicationApiResponse.BusinessInfo(
                                                    "123-45-67890", "테스트컴퍼니1", "홍길동", null, null),
                                            new SellerApplicationApiResponse.CsContactInfo(
                                                    "02-1234-5678", "cs@example.com", null),
                                            null,
                                            null,
                                            "PENDING",
                                            "2025-01-23T10:30:00+09:00",
                                            null,
                                            null,
                                            null,
                                            null),
                                    new SellerApplicationApiResponse(
                                            2L,
                                            new SellerApplicationApiResponse.SellerInfo(
                                                    "테스트셀러2", "테스트 브랜드2", null, null),
                                            new SellerApplicationApiResponse.BusinessInfo(
                                                    "987-65-43210", "테스트컴퍼니2", "김철수", null, null),
                                            new SellerApplicationApiResponse.CsContactInfo(
                                                    "02-9876-5432", "cs2@example.com", null),
                                            null,
                                            null,
                                            "PENDING",
                                            "2025-01-23T10:30:00+09:00",
                                            null,
                                            null,
                                            null,
                                            null)),
                            0,
                            20,
                            2L);

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(any())).willReturn(pageResponse);

            // when & then
            mockMvc.perform(
                            get("/api/v2/admin/seller-applications")
                                    .param("status", "PENDING")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.totalElements").value(2))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("status")
                                                    .description(
                                                            "신청 상태 필터 +\n"
                                                                    + "PENDING: 대기중, "
                                                                    + "APPROVED: 승인됨, "
                                                                    + "REJECTED: 거절됨 +\n"
                                                                    + "미입력 시 전체 조회")
                                                    .optional(),
                                            parameterWithName("searchField")
                                                    .description(
                                                            "검색 필드 +\n"
                                                                    + "sellerName: 셀러명, "
                                                                    + "companyName: 회사명")
                                                    .optional(),
                                            parameterWithName("searchValue")
                                                    .description("검색어 +\n" + "searchField 지정 시 필수")
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
                                                    .description("입점 신청 목록"),
                                            fieldWithPath("data.content[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("신청 ID"),
                                            fieldWithPath("data.content[].sellerInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 정보"),
                                            fieldWithPath("data.content[].sellerInfo.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명"),
                                            fieldWithPath("data.content[].sellerInfo.displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.content[].sellerInfo.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL")
                                                    .optional(),
                                            fieldWithPath("data.content[].sellerInfo.description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명")
                                                    .optional(),
                                            fieldWithPath("data.content[].businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보"),
                                            fieldWithPath(
                                                            "data.content[].businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호"),
                                            fieldWithPath("data.content[].businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명"),
                                            fieldWithPath(
                                                            "data.content[].businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명"),
                                            fieldWithPath(
                                                            "data.content[].businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호")
                                                    .optional(),
                                            fieldWithPath(
                                                            "data.content[].businessInfo.businessAddress")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업장 주소")
                                                    .optional(),
                                            fieldWithPath("data.content[].csContact")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 연락처"),
                                            fieldWithPath("data.content[].csContact.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호"),
                                            fieldWithPath("data.content[].csContact.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이메일"),
                                            fieldWithPath("data.content[].csContact.mobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("휴대폰번호")
                                                    .optional(),
                                            fieldWithPath("data.content[].addressInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보")
                                                    .optional(),
                                            fieldWithPath("data.content[].agreement")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("동의 정보")
                                                    .optional(),
                                            fieldWithPath("data.content[].status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("신청 상태"),
                                            fieldWithPath("data.content[].appliedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("신청일시"),
                                            fieldWithPath("data.content[].processedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("처리일시")
                                                    .optional(),
                                            fieldWithPath("data.content[].processedBy")
                                                    .type(JsonFieldType.STRING)
                                                    .description("처리자")
                                                    .optional(),
                                            fieldWithPath("data.content[].rejectionReason")
                                                    .type(JsonFieldType.STRING)
                                                    .description("거절 사유")
                                                    .optional(),
                                            fieldWithPath("data.content[].approvedSellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("승인된 셀러 ID")
                                                    .optional(),
                                            fieldWithPath("data.page")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("총 개수"),
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
                                                    .description("요청 ID")
                                                    .optional())));
        }
    }
}
