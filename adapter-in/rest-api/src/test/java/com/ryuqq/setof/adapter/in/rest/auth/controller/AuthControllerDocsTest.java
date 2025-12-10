package com.ryuqq.setof.adapter.in.rest.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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
import com.ryuqq.setof.adapter.in.rest.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.auth.mapper.AuthApiMapper;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.port.in.command.LocalLoginUseCase;
import com.ryuqq.setof.application.member.port.in.command.LogoutMemberUseCase;
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
 * AuthController REST Docs 테스트
 *
 * <p>인증 API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@WebMvcTest(controllers = AuthController.class)
@Import({AuthController.class, AuthControllerDocsTest.TestSecurityConfig.class})
@DisplayName("AuthController REST Docs")
class AuthControllerDocsTest extends RestDocsTestSupport {

    @MockitoBean private LocalLoginUseCase localLoginUseCase;

    @MockitoBean private LogoutMemberUseCase logoutMemberUseCase;

    @MockitoBean private AuthApiMapper authApiMapper;

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
    @DisplayName("POST /api/v2/auth/login - 로그인 API 문서")
    void login() throws Exception {
        // Given
        LoginApiRequest request = new LoginApiRequest("01012345678", "Password1!");

        LocalLoginCommand command = new LocalLoginCommand("01012345678", "Password1!");
        TokenPairResponse tokens =
                new TokenPairResponse(
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjg5MDAwMDAwfQ.xxx",
                        3600L,
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjg5NjAwMDAwfQ.yyy",
                        604800L);
        LocalLoginResponse loginResponse =
                new LocalLoginResponse("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa", tokens);

        when(authApiMapper.toLocalLoginCommand(any(LoginApiRequest.class))).thenReturn(command);
        when(localLoginUseCase.execute(any(LocalLoginCommand.class))).thenReturn(loginResponse);
        when(authApiMapper.toTokenApiResponse(any(TokenPairResponse.class)))
                .thenReturn(
                        new com.ryuqq.setof.adapter.in.rest.auth.dto.response.TokenApiResponse(
                                tokens.accessToken(),
                                tokens.accessTokenExpiresIn(),
                                null,
                                null,
                                null));

        // When & Then
        mockMvc.perform(
                        post(ApiV2Paths.Auth.LOGIN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.expiresIn").exists())
                .andDo(
                        document(
                                "auth-login",
                                requestFields(
                                        fieldWithPath("csPhoneNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("핸드폰 번호 (010으로 시작하는 11자리)"),
                                        fieldWithPath("password")
                                                .type(JsonFieldType.STRING)
                                                .description("비밀번호")),
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
                                                .description(
                                                        "Access Token (HttpOnly Cookie로도 전달됨)"),
                                        fieldWithPath("data.expiresIn")
                                                .type(JsonFieldType.NUMBER)
                                                .description("토큰 만료 시간 (초)"),
                                        fieldWithPath("data.isNewMember")
                                                .type(JsonFieldType.NULL)
                                                .description("신규 회원 여부 (OAuth2 전용)")
                                                .optional(),
                                        fieldWithPath("data.needsIntegration")
                                                .type(JsonFieldType.NULL)
                                                .description("LOCAL 회원 통합 필요 여부 (OAuth2 전용)")
                                                .optional(),
                                        fieldWithPath("data.memberId")
                                                .type(JsonFieldType.NULL)
                                                .description("회원 ID (통합 필요 시)")
                                                .optional(),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName("POST /api/v2/auth/logout - 로그아웃 API 문서")
    void logout() throws Exception {
        // Given
        String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
        String phoneNumber = "01012345678";
        MemberPrincipal principal = MemberPrincipal.of(memberId, phoneNumber);

        LogoutMemberCommand command = new LogoutMemberCommand(memberId);

        when(authApiMapper.toLogoutCommand(memberId)).thenReturn(command);
        doNothing().when(logoutMemberUseCase).execute(any(LogoutMemberCommand.class));
        doNothing().when(tokenCookieWriter).deleteTokenCookies(any());

        // When & Then
        mockMvc.perform(post(ApiV2Paths.Auth.LOGOUT).with(user(principal)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "auth-logout",
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
                                                .description("응답 데이터 (로그아웃 시 null)"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }
}
