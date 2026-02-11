package com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.sellerapplication.SellerApplicationApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.response.SellerApplicationApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.mapper.SellerApplicationQueryApiMapper;
import com.ryuqq.setof.application.sellerapplication.dto.response.SellerApplicationPageResult;
import com.ryuqq.setof.application.sellerapplication.dto.response.SellerApplicationResult;
import com.ryuqq.setof.application.sellerapplication.port.in.query.GetSellerApplicationUseCase;
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

    @MockBean private GetSellerApplicationUseCase getUseCase;

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
                                    SellerApplicationApiFixtures.sellerApplicationApiResponse(1L),
                                    SellerApplicationApiFixtures.sellerApplicationApiResponse(2L)),
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
                                                            "신청 상태 필터 (복수 선택 가능) +\n"
                                                                + "PENDING: 대기중, APPROVED: 승인됨,"
                                                                + " REJECTED: 거절됨 +\n"
                                                                + "예: status=PENDING&status=APPROVED"
                                                                + " +\n"
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
                                                    .description("사업장 주소"),
                                            fieldWithPath(
                                                            "data.content[].businessInfo.businessAddress.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 우편번호"),
                                            fieldWithPath(
                                                            "data.content[].businessInfo.businessAddress.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 주소"),
                                            fieldWithPath(
                                                            "data.content[].businessInfo.businessAddress.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 상세주소")
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
                                                    .description("휴대폰번호"),
                                            fieldWithPath("data.content[].addressInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보"),
                                            fieldWithPath("data.content[].addressInfo.addressType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 유형"),
                                            fieldWithPath("data.content[].addressInfo.addressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 별칭"),
                                            fieldWithPath("data.content[].addressInfo.address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 상세"),
                                            fieldWithPath(
                                                            "data.content[].addressInfo.address.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath(
                                                            "data.content[].addressInfo.address.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소"),
                                            fieldWithPath(
                                                            "data.content[].addressInfo.address.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소")
                                                    .optional(),
                                            fieldWithPath("data.content[].addressInfo.contactInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("담당자 정보"),
                                            fieldWithPath(
                                                            "data.content[].addressInfo.contactInfo.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자명"),
                                            fieldWithPath(
                                                            "data.content[].addressInfo.contactInfo.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자 연락처"),
                                            fieldWithPath("data.content[].agreement")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("동의 정보"),
                                            fieldWithPath("data.content[].agreement.agreedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("동의 일시"),
                                            fieldWithPath("data.content[].agreement.termsAgreed")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("이용약관 동의 여부"),
                                            fieldWithPath("data.content[].agreement.privacyAgreed")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("개인정보 처리방침 동의 여부"),
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

    @Nested
    @DisplayName("셀러 입점 신청 상세 조회 API")
    class GetTest {

        @Test
        @DisplayName("셀러 입점 신청 상세 조회 성공")
        void get_Success() throws Exception {
            // given
            Long applicationId = 1L;
            SellerApplicationResult result =
                    SellerApplicationApiFixtures.applicationResult(applicationId);
            SellerApplicationApiResponse response =
                    SellerApplicationApiFixtures.sellerApplicationApiResponse(applicationId);

            given(getUseCase.execute(eq(applicationId))).willReturn(result);
            given(mapper.toResponse(any(SellerApplicationResult.class))).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v2/admin/seller-applications/{applicationId}", applicationId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(applicationId))
                    .andExpect(jsonPath("$.data.sellerInfo").exists())
                    .andExpect(jsonPath("$.data.businessInfo").exists())
                    .andExpect(jsonPath("$.data.status").value("PENDING"))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("applicationId")
                                                    .description("신청 ID")),
                                    responseFields(
                                            fieldWithPath("data.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("신청 ID"),
                                            fieldWithPath("data.sellerInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 정보"),
                                            fieldWithPath("data.sellerInfo.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명"),
                                            fieldWithPath("data.sellerInfo.displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.sellerInfo.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL")
                                                    .optional(),
                                            fieldWithPath("data.sellerInfo.description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명")
                                                    .optional(),
                                            fieldWithPath("data.businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보"),
                                            fieldWithPath("data.businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호"),
                                            fieldWithPath("data.businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명"),
                                            fieldWithPath("data.businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명"),
                                            fieldWithPath("data.businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호")
                                                    .optional(),
                                            fieldWithPath("data.businessInfo.businessAddress")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업장 주소"),
                                            fieldWithPath(
                                                            "data.businessInfo.businessAddress.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 우편번호"),
                                            fieldWithPath("data.businessInfo.businessAddress.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 주소"),
                                            fieldWithPath("data.businessInfo.businessAddress.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 상세주소")
                                                    .optional(),
                                            fieldWithPath("data.csContact")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 연락처"),
                                            fieldWithPath("data.csContact.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호"),
                                            fieldWithPath("data.csContact.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이메일"),
                                            fieldWithPath("data.csContact.mobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("휴대폰번호"),
                                            fieldWithPath("data.addressInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보"),
                                            fieldWithPath("data.addressInfo.addressType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 유형"),
                                            fieldWithPath("data.addressInfo.addressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 별칭"),
                                            fieldWithPath("data.addressInfo.address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 상세"),
                                            fieldWithPath("data.addressInfo.address.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath("data.addressInfo.address.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소"),
                                            fieldWithPath("data.addressInfo.address.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소")
                                                    .optional(),
                                            fieldWithPath("data.addressInfo.contactInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("담당자 정보"),
                                            fieldWithPath("data.addressInfo.contactInfo.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자명"),
                                            fieldWithPath("data.addressInfo.contactInfo.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자 연락처"),
                                            fieldWithPath("data.agreement")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("동의 정보"),
                                            fieldWithPath("data.agreement.agreedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("동의 일시"),
                                            fieldWithPath("data.agreement.termsAgreed")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("이용약관 동의 여부"),
                                            fieldWithPath("data.agreement.privacyAgreed")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("개인정보 처리방침 동의 여부"),
                                            fieldWithPath("data.status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("신청 상태"),
                                            fieldWithPath("data.appliedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("신청일시"),
                                            fieldWithPath("data.processedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("처리일시")
                                                    .optional(),
                                            fieldWithPath("data.processedBy")
                                                    .type(JsonFieldType.STRING)
                                                    .description("처리자")
                                                    .optional(),
                                            fieldWithPath("data.rejectionReason")
                                                    .type(JsonFieldType.STRING)
                                                    .description("거절 사유")
                                                    .optional(),
                                            fieldWithPath("data.approvedSellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("승인된 셀러 ID")
                                                    .optional(),
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
