package com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.sellerapplication.SellerApplicationApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.ApplySellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.RejectSellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.mapper.SellerApplicationCommandApiMapper;
import com.ryuqq.setof.application.sellerapplication.port.in.command.ApplySellerApplicationUseCase;
import com.ryuqq.setof.application.sellerapplication.port.in.command.ApproveSellerApplicationUseCase;
import com.ryuqq.setof.application.sellerapplication.port.in.command.RejectSellerApplicationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * SellerApplicationCommandController REST Docs 테스트.
 *
 * <p>셀러 입점 신청 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerApplicationCommandController REST Docs 테스트")
@WebMvcTest(SellerApplicationCommandController.class)
@WithMockUser
class SellerApplicationCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private ApplySellerApplicationUseCase applyUseCase;

    @MockBean private ApproveSellerApplicationUseCase approveUseCase;

    @MockBean private RejectSellerApplicationUseCase rejectUseCase;

    @MockBean private SellerApplicationCommandApiMapper mapper;

    private static final Long APPLICATION_ID = 1L;
    private static final Long SELLER_ID = 100L;

    @Nested
    @DisplayName("셀러 입점 신청 API")
    class ApplyTest {

        @Test
        @DisplayName("셀러 입점 신청 성공")
        void apply_Success() throws Exception {
            // given
            ApplySellerApplicationApiRequest request = SellerApplicationApiFixtures.applyRequest();
            given(applyUseCase.execute(any())).willReturn(APPLICATION_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/seller-applications")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.applicationId").value(APPLICATION_ID))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("sellerInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 기본 정보 [필수]"),
                                            fieldWithPath("sellerInfo.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명 [필수, 최대 100자]"),
                                            fieldWithPath("sellerInfo.displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명 [최대 100자]")
                                                    .optional(),
                                            fieldWithPath("sellerInfo.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL [최대 500자]")
                                                    .optional(),
                                            fieldWithPath("sellerInfo.description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명 [최대 1000자]")
                                                    .optional(),
                                            fieldWithPath("businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보 [필수]"),
                                            fieldWithPath("businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호 [필수, 최대 20자]"),
                                            fieldWithPath("businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명 [필수, 최대 100자]"),
                                            fieldWithPath("businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명 [필수, 최대 50자]"),
                                            fieldWithPath("businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호 [최대 50자]")
                                                    .optional(),
                                            fieldWithPath("businessInfo.businessAddress")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업장 주소 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호 [필수, 최대 10자]"),
                                            fieldWithPath("businessInfo.businessAddress.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 [필수, 최대 200자]"),
                                            fieldWithPath("businessInfo.businessAddress.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소 [최대 200자]")
                                                    .optional(),
                                            fieldWithPath("csContact")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 연락처 [필수]"),
                                            fieldWithPath("csContact.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호 [필수, 최대 20자]"),
                                            fieldWithPath("csContact.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이메일 [필수, 이메일 형식, 최대 100자]"),
                                            fieldWithPath("csContact.mobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("휴대폰번호 [최대 20자]")
                                                    .optional(),
                                            fieldWithPath("returnAddress")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("반품지 주소 [필수]"),
                                            fieldWithPath("returnAddress.addressType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "주소 유형 [필수] +\n"
                                                                    + "RETURN: 반품지, SHIPPING: 출고지"),
                                            fieldWithPath("returnAddress.addressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 별칭 [필수, 최대 50자]"),
                                            fieldWithPath("returnAddress.address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 상세 [필수]"),
                                            fieldWithPath("returnAddress.address.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호 [필수, 최대 10자]"),
                                            fieldWithPath("returnAddress.address.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 [필수, 최대 200자]"),
                                            fieldWithPath("returnAddress.address.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소 [최대 200자]")
                                                    .optional(),
                                            fieldWithPath("returnAddress.contactInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("담당자 정보")
                                                    .optional(),
                                            fieldWithPath("returnAddress.contactInfo.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자명 [최대 50자]")
                                                    .optional(),
                                            fieldWithPath("returnAddress.contactInfo.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자 연락처 [최대 20자]")
                                                    .optional(),
                                            fieldWithPath("settlementInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정산 정보 [필수]"),
                                            fieldWithPath("settlementInfo.bankCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행 코드 [최대 10자]")
                                                    .optional(),
                                            fieldWithPath("settlementInfo.bankName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행명 [필수, 최대 50자]"),
                                            fieldWithPath("settlementInfo.accountNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계좌번호 [필수, 최대 30자, 숫자만]"),
                                            fieldWithPath("settlementInfo.accountHolderName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("예금주 [필수, 최대 50자]"),
                                            fieldWithPath("settlementInfo.settlementCycle")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정산 주기 [필수] +\n"
                                                                    + "WEEKLY: 주간, BIWEEKLY: 격주,"
                                                                    + " MONTHLY: 월간"),
                                            fieldWithPath("settlementInfo.settlementDay")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정산일 [필수, 1~31]")),
                                    responseFields(
                                            fieldWithPath("data.applicationId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 신청 ID"),
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
    @DisplayName("셀러 입점 신청 승인 API")
    class ApproveTest {

        @Test
        @DisplayName("셀러 입점 신청 승인 성공")
        void approve_Success() throws Exception {
            // given
            given(approveUseCase.execute(any())).willReturn(SELLER_ID);

            // when & then
            mockMvc.perform(
                            post(
                                    "/api/v2/admin/seller-applications/{applicationId}/approve",
                                    APPLICATION_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerId").value(SELLER_ID))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("applicationId")
                                                    .description("신청 ID")),
                                    responseFields(
                                            fieldWithPath("data.sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 셀러 ID"),
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
    @DisplayName("셀러 입점 신청 거절 API")
    class RejectTest {

        @Test
        @DisplayName("셀러 입점 신청 거절 성공")
        void reject_Success() throws Exception {
            // given
            RejectSellerApplicationApiRequest request =
                    SellerApplicationApiFixtures.rejectRequest();
            willDoNothing().given(rejectUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post(
                                            "/api/v2/admin/seller-applications/{applicationId}/reject",
                                            APPLICATION_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("applicationId")
                                                    .description("신청 ID")),
                                    requestFields(
                                            fieldWithPath("rejectionReason")
                                                    .type(JsonFieldType.STRING)
                                                    .description("거절 사유 [필수]"))));
        }
    }
}
