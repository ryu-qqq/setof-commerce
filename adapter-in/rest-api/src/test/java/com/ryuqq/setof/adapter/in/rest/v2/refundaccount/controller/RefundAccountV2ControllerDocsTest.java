package com.ryuqq.setof.adapter.in.rest.v2.refundaccount.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.RegisterRefundAccountV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.dto.command.UpdateRefundAccountV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.refundaccount.mapper.RefundAccountV2ApiMapper;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.port.in.command.DeleteRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.RegisterRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.command.UpdateRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.in.query.GetRefundAccountUseCase;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * RefundAccountV2Controller REST Docs 테스트
 *
 * <p>환불계좌 관리 API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = RefundAccountV2Controller.class)
@Import({
    RefundAccountV2Controller.class,
    RefundAccountV2ControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("RefundAccountV2Controller REST Docs")
class RefundAccountV2ControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private RegisterRefundAccountUseCase registerRefundAccountUseCase;

    @MockitoBean private GetRefundAccountUseCase getRefundAccountUseCase;

    @MockitoBean private UpdateRefundAccountUseCase updateRefundAccountUseCase;

    @MockitoBean private DeleteRefundAccountUseCase deleteRefundAccountUseCase;

    @MockitoBean private RefundAccountV2ApiMapper refundAccountV2ApiMapper;

    @Autowired private WebApplicationContext webApplicationContext;

    private MemberPrincipal principal;
    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String PHONE_NUMBER = "01012345678";
    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @BeforeEach
    void setUpWithSecurity(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(springSecurity())
                        .apply(
                                documentationConfiguration(restDocumentation)
                                        .operationPreprocessors()
                                        .withRequestDefaults(prettyPrint())
                                        .withResponseDefaults(prettyPrint()))
                        .build();
        this.principal = MemberPrincipal.of(MEMBER_ID, PHONE_NUMBER);
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @Test
    @DisplayName("GET /api/v2/members/me/refund-account - 환불계좌 조회 API 문서")
    void getRefundAccount() throws Exception {
        // Given
        RefundAccountResponse response =
                RefundAccountResponse.of(
                        1L,
                        1L,
                        "KB국민은행",
                        "004",
                        "123-***-***-0123",
                        "홍길동",
                        true,
                        FIXED_TIME,
                        FIXED_TIME,
                        FIXED_TIME);

        when(getRefundAccountUseCase.execute(any(UUID.class))).thenReturn(Optional.of(response));

        // When & Then
        mockMvc.perform(get(ApiV2Paths.RefundAccount.BASE).with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.bankName").value("KB국민은행"))
                .andDo(
                        document(
                                "refund-account-get",
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data.id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불계좌 ID"),
                                        fieldWithPath("data.bankId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("은행 ID"),
                                        fieldWithPath("data.bankName")
                                                .type(JsonFieldType.STRING)
                                                .description("은행명"),
                                        fieldWithPath("data.bankCode")
                                                .type(JsonFieldType.STRING)
                                                .description("은행 코드"),
                                        fieldWithPath("data.maskedAccountNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("마스킹된 계좌번호"),
                                        fieldWithPath("data.accountHolderName")
                                                .type(JsonFieldType.STRING)
                                                .description("예금주명"),
                                        fieldWithPath("data.isVerified")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("검증 완료 여부"),
                                        fieldWithPath("data.verifiedAt")
                                                .type(JsonFieldType.STRING)
                                                .description("검증 완료 일시"),
                                        fieldWithPath("data.createdAt")
                                                .type(JsonFieldType.STRING)
                                                .description("생성일시"),
                                        fieldWithPath("data.modifiedAt")
                                                .type(JsonFieldType.STRING)
                                                .description("수정일시"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }

    @Test
    @DisplayName("POST /api/v2/members/me/refund-account - 환불계좌 등록 API 문서")
    void registerRefundAccount() throws Exception {
        // Given
        RegisterRefundAccountV2ApiRequest request =
                new RegisterRefundAccountV2ApiRequest(1L, "1234567890123", "홍길동");

        RefundAccountResponse response =
                RefundAccountResponse.of(
                        1L,
                        1L,
                        "KB국민은행",
                        "004",
                        "123-***-***-0123",
                        "홍길동",
                        true,
                        FIXED_TIME,
                        FIXED_TIME,
                        FIXED_TIME);

        when(refundAccountV2ApiMapper.toRegisterCommand(any(UUID.class), any()))
                .thenReturn(
                        RegisterRefundAccountCommand.of(
                                UUID.fromString(MEMBER_ID), 1L, "1234567890123", "홍길동"));
        when(registerRefundAccountUseCase.execute(any(RegisterRefundAccountCommand.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(
                        post(ApiV2Paths.RefundAccount.BASE)
                                .with(user(principal))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andDo(
                        document(
                                "refund-account-register",
                                requestFields(
                                        fieldWithPath("bankId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("은행 ID"),
                                        fieldWithPath("accountNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("계좌번호"),
                                        fieldWithPath("accountHolderName")
                                                .type(JsonFieldType.STRING)
                                                .description("예금주명")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data.id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불계좌 ID"),
                                        fieldWithPath("data.bankId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("은행 ID"),
                                        fieldWithPath("data.bankName")
                                                .type(JsonFieldType.STRING)
                                                .description("은행명"),
                                        fieldWithPath("data.bankCode")
                                                .type(JsonFieldType.STRING)
                                                .description("은행 코드"),
                                        fieldWithPath("data.maskedAccountNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("마스킹된 계좌번호"),
                                        fieldWithPath("data.accountHolderName")
                                                .type(JsonFieldType.STRING)
                                                .description("예금주명"),
                                        fieldWithPath("data.isVerified")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("검증 완료 여부"),
                                        fieldWithPath("data.verifiedAt")
                                                .type(JsonFieldType.STRING)
                                                .description("검증 완료 일시"),
                                        fieldWithPath("data.createdAt")
                                                .type(JsonFieldType.STRING)
                                                .description("생성일시"),
                                        fieldWithPath("data.modifiedAt")
                                                .type(JsonFieldType.STRING)
                                                .description("수정일시"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }

    @Test
    @DisplayName("PUT /api/v2/members/me/refund-account - 환불계좌 수정 API 문서")
    void updateRefundAccount() throws Exception {
        // Given
        UpdateRefundAccountV2ApiRequest request =
                new UpdateRefundAccountV2ApiRequest(2L, "9876543210987", "김철수");

        RefundAccountResponse existingResponse =
                RefundAccountResponse.of(
                        1L,
                        1L,
                        "KB국민은행",
                        "004",
                        "123-***-***-0123",
                        "홍길동",
                        true,
                        FIXED_TIME,
                        FIXED_TIME,
                        FIXED_TIME);

        RefundAccountResponse updatedResponse =
                RefundAccountResponse.of(
                        1L,
                        2L,
                        "신한은행",
                        "088",
                        "987-***-***-0987",
                        "김철수",
                        true,
                        FIXED_TIME,
                        FIXED_TIME,
                        FIXED_TIME);

        when(getRefundAccountUseCase.execute(any(UUID.class)))
                .thenReturn(Optional.of(existingResponse));
        when(refundAccountV2ApiMapper.toUpdateCommand(any(UUID.class), any(Long.class), any()))
                .thenReturn(
                        UpdateRefundAccountCommand.of(
                                UUID.fromString(MEMBER_ID), 1L, 2L, "9876543210987", "김철수"));
        when(updateRefundAccountUseCase.execute(any(UpdateRefundAccountCommand.class)))
                .thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(
                        put(ApiV2Paths.RefundAccount.BASE)
                                .with(user(principal))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.bankName").value("신한은행"))
                .andDo(
                        document(
                                "refund-account-update",
                                requestFields(
                                        fieldWithPath("bankId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("은행 ID"),
                                        fieldWithPath("accountNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("계좌번호"),
                                        fieldWithPath("accountHolderName")
                                                .type(JsonFieldType.STRING)
                                                .description("예금주명")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data.id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불계좌 ID"),
                                        fieldWithPath("data.bankId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("은행 ID"),
                                        fieldWithPath("data.bankName")
                                                .type(JsonFieldType.STRING)
                                                .description("은행명"),
                                        fieldWithPath("data.bankCode")
                                                .type(JsonFieldType.STRING)
                                                .description("은행 코드"),
                                        fieldWithPath("data.maskedAccountNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("마스킹된 계좌번호"),
                                        fieldWithPath("data.accountHolderName")
                                                .type(JsonFieldType.STRING)
                                                .description("예금주명"),
                                        fieldWithPath("data.isVerified")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("검증 완료 여부"),
                                        fieldWithPath("data.verifiedAt")
                                                .type(JsonFieldType.STRING)
                                                .description("검증 완료 일시"),
                                        fieldWithPath("data.createdAt")
                                                .type(JsonFieldType.STRING)
                                                .description("생성일시"),
                                        fieldWithPath("data.modifiedAt")
                                                .type(JsonFieldType.STRING)
                                                .description("수정일시"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }

    @Test
    @DisplayName("PATCH /api/v2/members/me/refund-account/delete - 환불계좌 삭제 API 문서")
    void deleteRefundAccount() throws Exception {
        // Given
        RefundAccountResponse existingResponse =
                RefundAccountResponse.of(
                        1L,
                        1L,
                        "KB국민은행",
                        "004",
                        "123-***-***-0123",
                        "홍길동",
                        true,
                        FIXED_TIME,
                        FIXED_TIME,
                        FIXED_TIME);

        when(getRefundAccountUseCase.execute(any(UUID.class)))
                .thenReturn(Optional.of(existingResponse));
        when(refundAccountV2ApiMapper.toDeleteCommand(any(UUID.class), any(Long.class)))
                .thenReturn(DeleteRefundAccountCommand.of(UUID.fromString(MEMBER_ID), 1L));
        doNothing().when(deleteRefundAccountUseCase).execute(any(DeleteRefundAccountCommand.class));

        // When & Then
        mockMvc.perform(
                        patch(ApiV2Paths.RefundAccount.BASE + ApiV2Paths.RefundAccount.DELETE_PATH)
                                .with(user(principal))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "refund-account-delete",
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NULL)
                                                .description("응답 데이터 (삭제 시 null)"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }
}
