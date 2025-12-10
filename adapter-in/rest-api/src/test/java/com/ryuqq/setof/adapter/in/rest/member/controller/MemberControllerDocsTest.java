package com.ryuqq.setof.adapter.in.rest.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.member.dto.command.KakaoLinkApiRequest;
import com.ryuqq.setof.adapter.in.rest.member.dto.command.RegisterMemberApiRequest;
import com.ryuqq.setof.adapter.in.rest.member.dto.command.ResetPasswordApiRequest;
import com.ryuqq.setof.adapter.in.rest.member.dto.command.WithdrawApiRequest;
import com.ryuqq.setof.adapter.in.rest.member.mapper.MemberApiMapper;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.port.in.command.IntegrateKakaoUseCase;
import com.ryuqq.setof.application.member.port.in.command.RegisterMemberUseCase;
import com.ryuqq.setof.application.member.port.in.command.ResetPasswordUseCase;
import com.ryuqq.setof.application.member.port.in.command.WithdrawMemberUseCase;
import com.ryuqq.setof.application.member.port.in.query.GetCurrentMemberUseCase;
import java.time.Instant;
import java.time.LocalDate;
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
 * MemberController REST Docs 테스트
 *
 * <p>회원 관리 API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@WebMvcTest(controllers = MemberController.class)
@Import({MemberController.class, MemberControllerDocsTest.TestSecurityConfig.class})
@DisplayName("MemberController REST Docs")
class MemberControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private RegisterMemberUseCase registerMemberUseCase;

    @MockitoBean private GetCurrentMemberUseCase getCurrentMemberUseCase;

    @MockitoBean private WithdrawMemberUseCase withdrawMemberUseCase;

    @MockitoBean private ResetPasswordUseCase resetPasswordUseCase;

    @MockitoBean private IntegrateKakaoUseCase integrateKakaoUseCase;

    @MockitoBean private MemberApiMapper memberApiMapper;

    @MockitoBean private TokenCookieWriter tokenCookieWriter;

    @Autowired private WebApplicationContext webApplicationContext;

    /**
     * Spring Security를 포함한 MockMvc 설정
     *
     * <p>Spring Security의 @AuthenticationPrincipal이 제대로 동작하도록 springSecurity() configurer를 적용합니다.
     */
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
    @DisplayName("POST /api/v2/members - 회원가입 API 문서")
    void register() throws Exception {
        // Given
        RegisterMemberApiRequest request =
                new RegisterMemberApiRequest(
                        "01012345678",
                        "user@example.com",
                        "Password1!",
                        "홍길동",
                        LocalDate.of(1990, 1, 15),
                        "MALE",
                        true,
                        true,
                        false);

        TokenPairResponse tokens =
                new TokenPairResponse(
                        "eyJhbGciOiJIUzI1NiJ9.xxx", 3600L, "eyJhbGciOiJIUzI1NiJ9.yyy", 604800L);
        RegisterMemberResponse registerResponse =
                new RegisterMemberResponse("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa", tokens);

        when(memberApiMapper.toRegisterCommand(any(RegisterMemberApiRequest.class)))
                .thenReturn(
                        new RegisterMemberCommand(
                                request.phoneNumber(),
                                request.email(),
                                request.password(),
                                request.name(),
                                request.dateOfBirth(),
                                request.gender(),
                                null));
        when(registerMemberUseCase.execute(any(RegisterMemberCommand.class)))
                .thenReturn(registerResponse);

        // When & Then
        mockMvc.perform(
                        post(ApiV2Paths.Members.REGISTER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andDo(
                        document(
                                "member-register",
                                requestFields(
                                        fieldWithPath("csPhoneNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("핸드폰 번호 (010으로 시작하는 11자리)"),
                                        fieldWithPath("email")
                                                .type(JsonFieldType.STRING)
                                                .description("이메일 주소")
                                                .optional(),
                                        fieldWithPath("password")
                                                .type(JsonFieldType.STRING)
                                                .description("비밀번호 (8자 이상, 영문+숫자+특수문자 포함)"),
                                        fieldWithPath("name")
                                                .type(JsonFieldType.STRING)
                                                .description("이름 (2~50자)"),
                                        fieldWithPath("dateOfBirth")
                                                .type(JsonFieldType.STRING)
                                                .description("생년월일 (YYYY-MM-DD)")
                                                .optional(),
                                        fieldWithPath("gender")
                                                .type(JsonFieldType.STRING)
                                                .description("성별 (MALE, FEMALE)")
                                                .optional(),
                                        fieldWithPath("privacyConsent")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("개인정보 수집 동의"),
                                        fieldWithPath("serviceTermsConsent")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("서비스 이용약관 동의"),
                                        fieldWithPath("adConsent")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("광고 수신 동의 여부")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각 (ISO 8601 형식)"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 데이터"),
                                        fieldWithPath("data.accessToken")
                                                .type(JsonFieldType.STRING)
                                                .description("Access Token"),
                                        fieldWithPath("data.expiresIn")
                                                .type(JsonFieldType.NUMBER)
                                                .description("토큰 만료 시간 (초)"),
                                        fieldWithPath("data.isNewMember")
                                                .type(JsonFieldType.NULL)
                                                .description("신규 회원 여부")
                                                .optional(),
                                        fieldWithPath("data.needsIntegration")
                                                .type(JsonFieldType.NULL)
                                                .description("LOCAL 회원 통합 필요 여부")
                                                .optional(),
                                        fieldWithPath("data.memberId")
                                                .type(JsonFieldType.NULL)
                                                .description("회원 ID")
                                                .optional(),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }

    @Test
    @DisplayName("GET /api/v2/members/me - 내 정보 조회 API 문서")
    void getCurrentMember() throws Exception {
        // Given
        String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
        String phoneNumber = "01012345678";
        MemberPrincipal principal = MemberPrincipal.of(memberId, phoneNumber);

        MemberDetailResponse memberDetail =
                new MemberDetailResponse(
                        memberId,
                        "01012345678",
                        "user@example.com",
                        "홍길동",
                        LocalDate.of(1990, 1, 15),
                        "MALE",
                        "LOCAL",
                        "ACTIVE",
                        Instant.parse("2025-12-05T10:30:00Z"));

        when(memberApiMapper.toGetCurrentMemberQuery(memberId))
                .thenReturn(new GetCurrentMemberQuery(memberId));
        when(getCurrentMemberUseCase.execute(any(GetCurrentMemberQuery.class)))
                .thenReturn(memberDetail);

        // When & Then
        mockMvc.perform(get(ApiV2Paths.Members.ME).with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.memberId").exists())
                .andDo(
                        document(
                                "member-me",
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각 (ISO 8601 형식)"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("응답 데이터"),
                                        fieldWithPath("data.memberId")
                                                .type(JsonFieldType.STRING)
                                                .description("회원 ID"),
                                        fieldWithPath("data.csPhoneNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("핸드폰 번호"),
                                        fieldWithPath("data.email")
                                                .type(JsonFieldType.STRING)
                                                .description("이메일 주소"),
                                        fieldWithPath("data.name")
                                                .type(JsonFieldType.STRING)
                                                .description("이름"),
                                        fieldWithPath("data.dateOfBirth")
                                                .type(JsonFieldType.STRING)
                                                .description("생년월일"),
                                        fieldWithPath("data.gender")
                                                .type(JsonFieldType.STRING)
                                                .description("성별"),
                                        fieldWithPath("data.provider")
                                                .type(JsonFieldType.STRING)
                                                .description("인증 제공자 (LOCAL, KAKAO)"),
                                        fieldWithPath("data.status")
                                                .type(JsonFieldType.STRING)
                                                .description("회원 상태 (ACTIVE, WITHDRAWN)"),
                                        fieldWithPath("data.createdAt")
                                                .type(JsonFieldType.STRING)
                                                .description("가입일시"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }

    @Test
    @DisplayName("PATCH /api/v2/members/me/withdraw - 회원 탈퇴 API 문서")
    void withdraw() throws Exception {
        // Given
        String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
        String phoneNumber = "01012345678";
        MemberPrincipal principal = MemberPrincipal.of(memberId, phoneNumber);
        WithdrawApiRequest request = new WithdrawApiRequest("서비스 이용 불만족");

        when(memberApiMapper.toWithdrawCommand(any(), any(WithdrawApiRequest.class)))
                .thenReturn(new WithdrawMemberCommand(memberId, request.reason()));
        doNothing().when(withdrawMemberUseCase).execute(any(WithdrawMemberCommand.class));
        doNothing().when(tokenCookieWriter).deleteTokenCookies(any());

        // When & Then
        mockMvc.perform(
                        patch(ApiV2Paths.Members.WITHDRAW)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(user(principal))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "member-withdraw",
                                requestFields(
                                        fieldWithPath("reason")
                                                .type(JsonFieldType.STRING)
                                                .description("탈퇴 사유")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각 (ISO 8601 형식)"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NULL)
                                                .description("응답 데이터 (탈퇴 시 null)"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }

    @Test
    @DisplayName("POST /api/v2/members/passwordHash/reset - 비밀번호 재설정 API 문서")
    void resetPassword() throws Exception {
        // Given
        ResetPasswordApiRequest request =
                new ResetPasswordApiRequest("01012345678", "NewPassword1!");

        when(memberApiMapper.toResetPasswordCommand(any(ResetPasswordApiRequest.class)))
                .thenReturn(new ResetPasswordCommand(request.phoneNumber(), request.newPassword()));
        doNothing().when(resetPasswordUseCase).execute(any(ResetPasswordCommand.class));

        // When & Then
        mockMvc.perform(
                        post(ApiV2Paths.Members.PASSWORD_RESET)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "member-passwordHash-reset",
                                requestFields(
                                        fieldWithPath("csPhoneNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("핸드폰 번호 (010으로 시작하는 11자리)"),
                                        fieldWithPath("newPassword")
                                                .type(JsonFieldType.STRING)
                                                .description("새 비밀번호 (8자 이상, 영문+숫자+특수문자 포함)")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각 (ISO 8601 형식)"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NULL)
                                                .description("응답 데이터 (재설정 시 null)"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }

    @Test
    @DisplayName("POST /api/v2/members/me/kakao-link - 카카오 계정 연동 API 문서")
    void linkKakao() throws Exception {
        // Given
        String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
        String phoneNumber = "01012345678";
        MemberPrincipal principal = MemberPrincipal.of(memberId, phoneNumber);
        KakaoLinkApiRequest request = new KakaoLinkApiRequest("1234567890");

        when(memberApiMapper.toIntegrateKakaoCommand(any(), any(KakaoLinkApiRequest.class)))
                .thenReturn(IntegrateKakaoCommand.withoutProfile(memberId, request.kakaoId()));
        doNothing().when(integrateKakaoUseCase).execute(any(IntegrateKakaoCommand.class));

        // When & Then
        mockMvc.perform(
                        post(ApiV2Paths.Members.KAKAO_LINK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(user(principal))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "member-kakao-link",
                                requestFields(
                                        fieldWithPath("kakaoId")
                                                .type(JsonFieldType.STRING)
                                                .description("카카오 ID")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각 (ISO 8601 형식)"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NULL)
                                                .description("응답 데이터 (연동 시 null)"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보")
                                                .optional())));
    }
}
