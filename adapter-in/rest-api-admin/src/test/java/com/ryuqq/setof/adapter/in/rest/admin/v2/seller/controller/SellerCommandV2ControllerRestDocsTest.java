package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerFullApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerCommandApiMapper;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerFullUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * SellerCommandV2Controller REST Docs 테스트.
 *
 * <p>셀러 Command V2 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerCommandV2Controller REST Docs 테스트")
@WebMvcTest(SellerCommandV2Controller.class)
@WithMockUser
class SellerCommandV2ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterSellerUseCase registerSellerUseCase;

    @MockBean private UpdateSellerUseCase updateSellerUseCase;

    @MockBean private UpdateSellerFullUseCase updateSellerFullUseCase;

    @MockBean private SellerCommandApiMapper mapper;

    private static final Long SELLER_ID = 1L;

    @Nested
    @DisplayName("셀러 등록 API")
    class RegisterSellerTest {

        @Test
        @DisplayName("셀러 등록 성공")
        void registerSeller_Success() throws Exception {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();
            given(registerSellerUseCase.execute(any())).willReturn(SELLER_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/sellers")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.sellerId").value(SELLER_ID))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("seller")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 기본 정보 [필수]"),
                                            fieldWithPath("seller.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명 [필수]"),
                                            fieldWithPath("seller.displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명 [필수]"),
                                            fieldWithPath("seller.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL [필수]"),
                                            fieldWithPath("seller.description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명 [필수]"),
                                            fieldWithPath("businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보 [필수]"),
                                            fieldWithPath("businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호 [필수]"),
                                            fieldWithPath("businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명 [필수]"),
                                            fieldWithPath("businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명 [필수]"),
                                            fieldWithPath("businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업장 주소 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소 [필수]"),
                                            fieldWithPath("businessInfo.csContact")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 연락처 [필수]"),
                                            fieldWithPath("businessInfo.csContact.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전화번호 [필수]"),
                                            fieldWithPath("businessInfo.csContact.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이메일 [필수]"),
                                            fieldWithPath("businessInfo.csContact.mobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("휴대폰번호 [필수]"),
                                            fieldWithPath("address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보 [필수]"),
                                            fieldWithPath("address.addressType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "주소 타입 [필수] +\n"
                                                                    + "RETURN: 반품지, SHIPPING: 출고지"),
                                            fieldWithPath("address.addressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 이름 [필수]"),
                                            fieldWithPath("address.address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 [필수]"),
                                            fieldWithPath("address.address.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호 [필수]"),
                                            fieldWithPath("address.address.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 [필수]"),
                                            fieldWithPath("address.address.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소 [필수]"),
                                            fieldWithPath("address.contactInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("담당자 연락처 [필수]"),
                                            fieldWithPath("address.contactInfo.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자명 [필수]"),
                                            fieldWithPath("address.contactInfo.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("연락처 [필수]"),
                                            fieldWithPath("address.defaultAddress")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 주소 여부 [필수]")),
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
    @DisplayName("셀러 기본정보 수정 API")
    class UpdateSellerTest {

        @Test
        @DisplayName("셀러 기본정보 수정 성공")
        void updateSeller_Success() throws Exception {
            // given
            UpdateSellerApiRequest request = SellerApiFixtures.updateRequest();
            willDoNothing().given(updateSellerUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v2/admin/sellers/{sellerId}", SELLER_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    requestFields(
                                            fieldWithPath("sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명 [필수]"),
                                            fieldWithPath("displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명 [필수]"),
                                            fieldWithPath("logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL [필수]"),
                                            fieldWithPath("description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명 [필수]"),
                                            fieldWithPath("address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보 [필수]"),
                                            fieldWithPath("address.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호 [필수]"),
                                            fieldWithPath("address.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 [필수]"),
                                            fieldWithPath("address.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소 [필수]"),
                                            fieldWithPath("csInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 정보 [필수]"),
                                            fieldWithPath("csInfo.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 전화번호 [필수]"),
                                            fieldWithPath("csInfo.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 이메일 [필수]"),
                                            fieldWithPath("csInfo.mobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 휴대폰번호 [필수]"),
                                            fieldWithPath("businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보 [필수]"),
                                            fieldWithPath("businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호 [필수]"),
                                            fieldWithPath("businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명 [필수]"),
                                            fieldWithPath("businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명 [필수]"),
                                            fieldWithPath("businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업장 주소 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 우편번호 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 주소 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 상세주소 [필수]"))));
        }
    }

    @Nested
    @DisplayName("셀러 전체정보 수정 API")
    class UpdateSellerFullTest {

        @Test
        @DisplayName("셀러 전체정보 수정 성공")
        void updateSellerFull_Success() throws Exception {
            // given
            UpdateSellerFullApiRequest request = SellerApiFixtures.updateFullRequest();
            willDoNothing().given(updateSellerFullUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put("/api/v2/admin/sellers/{sellerId}", SELLER_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    requestFields(
                                            fieldWithPath("seller")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 기본 정보 [필수]"),
                                            fieldWithPath("seller.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명 [필수]"),
                                            fieldWithPath("seller.displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명 [필수]"),
                                            fieldWithPath("seller.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL [필수]"),
                                            fieldWithPath("seller.description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명 [필수]"),
                                            fieldWithPath("businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보 [필수]"),
                                            fieldWithPath("businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호 [필수]"),
                                            fieldWithPath("businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명 [필수]"),
                                            fieldWithPath("businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명 [필수]"),
                                            fieldWithPath("businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업장 주소 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 우편번호 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 주소 [필수]"),
                                            fieldWithPath("businessInfo.businessAddress.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 상세주소 [필수]"),
                                            fieldWithPath("csInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 정보 [필수]"),
                                            fieldWithPath("csInfo.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 전화번호 [필수]"),
                                            fieldWithPath("csInfo.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 이메일 [필수]"),
                                            fieldWithPath("csInfo.mobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 휴대폰번호")
                                                    .optional(),
                                            fieldWithPath("csInfo.operatingStartTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 시작 시간 (예: 09:00)")
                                                    .optional(),
                                            fieldWithPath("csInfo.operatingEndTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 종료 시간 (예: 18:00)")
                                                    .optional(),
                                            fieldWithPath("csInfo.operatingDays")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "운영 요일 (쉼표 구분, 예: MON,TUE,WED,THU,FRI)")
                                                    .optional(),
                                            fieldWithPath("csInfo.kakaoChannelUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카카오 채널 URL")
                                                    .optional(),
                                            fieldWithPath("address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보 [필수]"),
                                            fieldWithPath("address.addressId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("주소 ID [필수]"),
                                            fieldWithPath("address.addressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 이름 [필수]"),
                                            fieldWithPath("address.address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 [필수]"),
                                            fieldWithPath("address.address.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호 [필수]"),
                                            fieldWithPath("address.address.line1")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 [필수]"),
                                            fieldWithPath("address.address.line2")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소 [필수]"),
                                            fieldWithPath("address.contactInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("담당자 연락처 [필수]"),
                                            fieldWithPath("address.contactInfo.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자명 [필수]"),
                                            fieldWithPath("address.contactInfo.phone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("연락처 [필수]"),
                                            fieldWithPath("contractInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("계약 정보 [필수]"),
                                            fieldWithPath("contractInfo.commissionRate")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("수수료율 (0 ~ 100) [필수]"),
                                            fieldWithPath("contractInfo.contractStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계약 시작일 (YYYY-MM-DD) [필수]"),
                                            fieldWithPath("contractInfo.contractEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계약 종료일 (YYYY-MM-DD) [필수]"),
                                            fieldWithPath("contractInfo.specialTerms")
                                                    .type(JsonFieldType.STRING)
                                                    .description("특약사항")
                                                    .optional(),
                                            fieldWithPath("settlementInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정산 정보 [필수]"),
                                            fieldWithPath("settlementInfo.bankAccount")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정산 계좌 [필수]"),
                                            fieldWithPath("settlementInfo.bankAccount.bankCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행 코드")
                                                    .optional(),
                                            fieldWithPath("settlementInfo.bankAccount.bankName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행명 [필수]"),
                                            fieldWithPath(
                                                            "settlementInfo.bankAccount.accountNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계좌번호 [필수]"),
                                            fieldWithPath(
                                                            "settlementInfo.bankAccount.accountHolderName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("예금주 [필수]"),
                                            fieldWithPath("settlementInfo.settlementCycle")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "정산 주기 (WEEKLY, BIWEEKLY, MONTHLY)"
                                                                    + " [필수]"),
                                            fieldWithPath("settlementInfo.settlementDay")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정산일 (1 ~ 31) [필수]"))));
        }
    }
}
