package com.ryuqq.setof.adapter.in.rest.v1.refundaccount.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.RefundAccountApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.RefundAccountV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.dto.response.RefundAccountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.refundaccount.mapper.RefundAccountV1ApiMapper;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import com.ryuqq.setof.application.refundaccount.port.in.query.GetRefundAccountUseCase;
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
 * RefundAccountQueryV1Controller REST Docs 테스트.
 *
 * <p>환불 계좌 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("RefundAccountQueryV1Controller REST Docs 테스트")
@WebMvcTest(RefundAccountQueryV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class RefundAccountQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetRefundAccountUseCase getRefundAccountUseCase;

    @MockBean private RefundAccountV1ApiMapper mapper;

    @Nested
    @DisplayName("환불 계좌 조회 API")
    class GetRefundAccountTest {

        @Test
        @DisplayName("환불 계좌 조회 성공")
        void getRefundAccount_Success() throws Exception {
            // given
            long refundAccountId = 1L;
            RefundAccountResult result =
                    RefundAccountApiFixtures.refundAccountResult(refundAccountId);
            RefundAccountV1ApiResponse response =
                    RefundAccountApiFixtures.refundAccountResponse(refundAccountId);

            given(getRefundAccountUseCase.execute(any())).willReturn(result);
            given(mapper.toResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(RefundAccountV1Endpoints.REFUND_ACCOUNT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.refundAccountId").value(refundAccountId))
                    .andExpect(jsonPath("$.data.bankName").value("국민은행"))
                    .andExpect(jsonPath("$.data.accountNumber").value("123456789012"))
                    .andExpect(jsonPath("$.data.accountHolderName").value("홍길동"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data.refundAccountId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("환불 계좌 ID"),
                                            fieldWithPath("data.bankName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행명"),
                                            fieldWithPath("data.accountNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계좌번호"),
                                            fieldWithPath("data.accountHolderName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("예금주명"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
