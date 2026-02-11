package com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.selleradmin.SellerAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.ApplySellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.BulkApproveSellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.command.BulkRejectSellerAdminApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.response.BulkApproveSellerAdminApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.mapper.SellerAdminApplicationCommandApiMapper;
import com.ryuqq.setof.application.common.dto.result.BatchProcessingResult;
import com.ryuqq.setof.application.selleradmin.port.in.command.ApplySellerAdminUseCase;
import com.ryuqq.setof.application.selleradmin.port.in.command.ApproveSellerAdminUseCase;
import com.ryuqq.setof.application.selleradmin.port.in.command.BulkApproveSellerAdminUseCase;
import com.ryuqq.setof.application.selleradmin.port.in.command.BulkRejectSellerAdminUseCase;
import com.ryuqq.setof.application.selleradmin.port.in.command.RejectSellerAdminUseCase;
import com.ryuqq.setof.application.selleradmin.port.in.command.ResetSellerAdminPasswordUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * SellerAdminApplicationCommandController REST Docs 테스트.
 *
 * <p>셀러 관리자 가입 신청 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("SellerAdminApplicationCommandController REST Docs 테스트")
@WebMvcTest(SellerAdminApplicationCommandController.class)
@WithMockUser
class SellerAdminApplicationCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private ApplySellerAdminUseCase applyUseCase;

    @MockBean private ApproveSellerAdminUseCase approveUseCase;

    @MockBean private RejectSellerAdminUseCase rejectUseCase;

    @MockBean private BulkApproveSellerAdminUseCase bulkApproveUseCase;

    @MockBean private BulkRejectSellerAdminUseCase bulkRejectUseCase;

    @MockBean private ResetSellerAdminPasswordUseCase resetPasswordUseCase;

    @MockBean private SellerAdminApplicationCommandApiMapper mapper;

    private static final String SELLER_ADMIN_ID = "01956f4a-2b3c-7d8e-9f0a-1b2c3d4e5f67";

    @Nested
    @DisplayName("셀러 관리자 가입 신청 API")
    class ApplySellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 가입 신청 성공")
        void applySellerAdmin_Success() throws Exception {
            // given
            ApplySellerAdminApiRequest request = SellerAdminApiFixtures.applyRequest();
            given(applyUseCase.execute(any())).willReturn(SELLER_ADMIN_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/seller-admin-applications")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.sellerAdminId").value(SELLER_ADMIN_ID))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("가입할 셀러 ID [필수]"),
                                            fieldWithPath("loginId")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "로그인 ID [필수, 이메일 형식," + " 최대 100자]"),
                                            fieldWithPath("name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("관리자 이름 [필수, 최대 50자]"),
                                            fieldWithPath("phoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "휴대폰 번호 [필수, 최대 20자," + " 숫자/하이픈만]"),
                                            fieldWithPath("password")
                                                    .type(JsonFieldType.STRING)
                                                    .description("비밀번호 [필수, 8~100자]")),
                                    responseFields(
                                            fieldWithPath("data.sellerAdminId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성된 셀러 관리자 ID (UUIDv7)"),
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
    @DisplayName("셀러 관리자 가입 신청 승인 API")
    class ApproveSellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 가입 신청 승인 성공")
        void approveSellerAdmin_Success() throws Exception {
            // given
            given(approveUseCase.execute(any())).willReturn(SELLER_ADMIN_ID);

            // when & then
            mockMvc.perform(
                            post(
                                    "/api/v2/admin/seller-admin-applications/{sellerAdminId}/approve",
                                    SELLER_ADMIN_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerAdminId").value(SELLER_ADMIN_ID))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerAdminId")
                                                    .description("셀러 관리자 ID (UUIDv7)")),
                                    responseFields(
                                            fieldWithPath("data.sellerAdminId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러 관리자 ID"),
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
    @DisplayName("셀러 관리자 가입 신청 거절 API")
    class RejectSellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 가입 신청 거절 성공")
        void rejectSellerAdmin_Success() throws Exception {
            // given
            willDoNothing().given(rejectUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post(
                                    "/api/v2/admin/seller-admin-applications/{sellerAdminId}/reject",
                                    SELLER_ADMIN_ID))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerAdminId")
                                                    .description("셀러 관리자 ID (UUIDv7)"))));
        }
    }

    @Nested
    @DisplayName("셀러 관리자 가입 신청 일괄 승인 API")
    class BulkApproveSellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 가입 신청 일괄 승인 성공")
        void bulkApproveSellerAdmin_Success() throws Exception {
            // given
            BulkApproveSellerAdminApiRequest request = SellerAdminApiFixtures.bulkApproveRequest();
            BatchProcessingResult<String> result = SellerAdminApiFixtures.batchProcessingResult();
            BulkApproveSellerAdminApiResponse response =
                    SellerAdminApiFixtures.bulkApproveResponse();

            given(bulkApproveUseCase.execute(any())).willReturn(result);
            given(mapper.toResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/seller-admin-applications/bulk-approve")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.totalCount").value(2))
                    .andExpect(jsonPath("$.data.successCount").value(2))
                    .andExpect(jsonPath("$.data.failureCount").value(0))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("sellerAdminIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description(
                                                            "승인할 셀러 관리자 ID 목록" + " [필수, 최대 100건]")),
                                    responseFields(
                                            fieldWithPath("data.totalCount")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("총 처리 건수"),
                                            fieldWithPath("data.successCount")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("성공 건수"),
                                            fieldWithPath("data.failureCount")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("실패 건수"),
                                            fieldWithPath("data.results")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("개별 항목 결과 목록"),
                                            fieldWithPath("data.results[].sellerAdminId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러 관리자 ID"),
                                            fieldWithPath("data.results[].success")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("성공 여부"),
                                            fieldWithPath("data.results[].errorCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("에러 코드 (실패 시)")
                                                    .optional(),
                                            fieldWithPath("data.results[].errorMessage")
                                                    .type(JsonFieldType.STRING)
                                                    .description("에러 메시지 (실패 시)")
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

    @Nested
    @DisplayName("셀러 관리자 가입 신청 일괄 거절 API")
    class BulkRejectSellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 가입 신청 일괄 거절 성공")
        void bulkRejectSellerAdmin_Success() throws Exception {
            // given
            BulkRejectSellerAdminApiRequest request = SellerAdminApiFixtures.bulkRejectRequest();
            willDoNothing().given(bulkRejectUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/seller-admin-applications/bulk-reject")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("sellerAdminIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description(
                                                            "거절할 셀러 관리자 ID 목록"
                                                                    + " [필수, 최대 100건]"))));
        }
    }

    @Nested
    @DisplayName("셀러 관리자 비밀번호 초기화 API")
    class ResetPasswordSellerAdminTest {

        @Test
        @DisplayName("셀러 관리자 비밀번호 초기화 성공")
        void resetPasswordSellerAdmin_Success() throws Exception {
            // given
            willDoNothing().given(resetPasswordUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post(
                                    "/api/v2/admin/seller-admin-applications/{sellerAdminId}/reset-password",
                                    SELLER_ADMIN_ID))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerAdminId")
                                                    .description("셀러 관리자 ID (UUIDv7)"))));
        }
    }
}
