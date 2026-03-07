package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.RefundAccountApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.RefundAccountV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.RegisterRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.request.UpdateRefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper.RefundAccountV1ApiMapper;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.port.in.command.DeleteRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.RegisterRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.UpdateRefundAccountUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * RefundAccountCommandV1Controller REST Docs 테스트.
 *
 * <p>환불 계좌 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("RefundAccountCommandV1Controller REST Docs 테스트")
@WebMvcTest(RefundAccountCommandV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class RefundAccountCommandV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterRefundAccountUseCase registerRefundAccountUseCase;

    @MockBean private UpdateRefundAccountUseCase updateRefundAccountUseCase;

    @MockBean private DeleteRefundAccountUseCase deleteRefundAccountUseCase;

    @MockBean private RefundAccountV1ApiMapper mapper;

    @Nested
    @DisplayName("환불 계좌 등록 API")
    class RegisterRefundAccountTest {

        @Test
        @DisplayName("환불 계좌 등록 성공")
        void registerRefundAccount_Success() throws Exception {
            // given
            Long newRefundAccountId = 1L;
            RegisterRefundAccountV1ApiRequest request = RefundAccountApiFixtures.registerRequest();
            RegisterRefundAccountCommand command = RefundAccountApiFixtures.registerCommand(1L);

            given(mapper.toRegisterCommand(any(), any())).willReturn(command);
            given(registerRefundAccountUseCase.execute(command)).willReturn(newRefundAccountId);

            // when & then
            mockMvc.perform(
                            post(RefundAccountV1Endpoints.REFUND_ACCOUNT)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(newRefundAccountId))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("bankName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행명"),
                                            fieldWithPath("accountNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계좌번호 (숫자만)"),
                                            fieldWithPath("accountHolderName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("예금주명")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("등록된 환불 계좌 ID"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("환불 계좌 수정 API")
    class UpdateRefundAccountTest {

        @Test
        @DisplayName("환불 계좌 수정 성공")
        void updateRefundAccount_Success() throws Exception {
            // given
            long refundAccountId = 1L;
            UpdateRefundAccountV1ApiRequest request = RefundAccountApiFixtures.updateRequest();
            UpdateRefundAccountCommand command =
                    RefundAccountApiFixtures.updateCommand(1L, refundAccountId);

            given(mapper.toUpdateCommand(any(), any(), any())).willReturn(command);
            willDoNothing().given(updateRefundAccountUseCase).execute(command);

            // when & then
            mockMvc.perform(
                            put(RefundAccountV1Endpoints.REFUND_ACCOUNT_BY_ID, refundAccountId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(
                                                            RefundAccountV1Endpoints
                                                                    .PATH_REFUND_ACCOUNT_ID)
                                                    .description("수정할 환불 계좌 ID")),
                                    requestFields(
                                            fieldWithPath("bankName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행명"),
                                            fieldWithPath("accountNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계좌번호 (숫자만)"),
                                            fieldWithPath("accountHolderName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("예금주명")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("응답 데이터 없음"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("환불 계좌 삭제 API")
    class DeleteRefundAccountTest {

        @Test
        @DisplayName("환불 계좌 삭제 성공")
        void deleteRefundAccount_Success() throws Exception {
            // given
            long refundAccountId = 1L;
            DeleteRefundAccountCommand command =
                    RefundAccountApiFixtures.deleteCommand(1L, refundAccountId);

            given(mapper.toDeleteCommand(any(), any())).willReturn(command);
            willDoNothing().given(deleteRefundAccountUseCase).execute(command);

            // when & then
            mockMvc.perform(delete(RefundAccountV1Endpoints.REFUND_ACCOUNT_BY_ID, refundAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(
                                                            RefundAccountV1Endpoints
                                                                    .PATH_REFUND_ACCOUNT_ID)
                                                    .description("삭제할 환불 계좌 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("응답 데이터 없음"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
