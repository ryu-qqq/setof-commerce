package com.ryuqq.setof.adapter.in.rest.admin.v2.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.auth.AuthApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.LoginApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.mapper.AuthCommandApiMapper;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.port.in.LoginUseCase;
import com.ryuqq.setof.application.auth.port.in.LogoutUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * AuthCommandController REST Docs 테스트.
 *
 * <p>인증 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("AuthCommandController REST Docs 테스트")
@WebMvcTest(AuthCommandController.class)
@WithMockUser
class AuthCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private LoginUseCase loginUseCase;

    @MockBean private LogoutUseCase logoutUseCase;

    @MockBean private AuthCommandApiMapper commandMapper;

    @Nested
    @DisplayName("로그인 API")
    class LoginTest {

        @Test
        @DisplayName("로그인 성공")
        void login_Success() throws Exception {
            // given
            LoginApiRequest request = AuthApiFixtures.loginRequest();
            LoginResult result = AuthApiFixtures.successLoginResult();
            LoginApiResponse response = AuthApiFixtures.loginResponse();

            given(loginUseCase.execute(any())).willReturn(result);
            given(commandMapper.toCommand(any(LoginApiRequest.class))).willReturn(null);
            given(commandMapper.toResponse(any(LoginResult.class))).willReturn(response);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/auth/login")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()))
                    .andExpect(jsonPath("$.data.refreshToken").value(response.refreshToken()))
                    .andExpect(jsonPath("$.data.tokenType").value(response.tokenType()))
                    .andExpect(jsonPath("$.data.expiresIn").value(response.expiresIn()))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("identifier")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "사용자 식별자 (이메일 또는 아이디) [필수, 최대 100자]"),
                                            fieldWithPath("password")
                                                    .type(JsonFieldType.STRING)
                                                    .description("비밀번호 [필수, 최대 100자]")),
                                    responseFields(
                                            fieldWithPath("data.accessToken")
                                                    .type(JsonFieldType.STRING)
                                                    .description("액세스 토큰"),
                                            fieldWithPath("data.refreshToken")
                                                    .type(JsonFieldType.STRING)
                                                    .description("리프레시 토큰"),
                                            fieldWithPath("data.tokenType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("토큰 타입 (Bearer)"),
                                            fieldWithPath("data.expiresIn")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("만료 시간 (초)"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("로그아웃 API")
    class LogoutTest {

        @Test
        @DisplayName("로그아웃 성공")
        void logout_Success() throws Exception {
            // given
            willDoNothing().given(logoutUseCase).execute(any());

            // when & then
            mockMvc.perform(post("/api/v2/admin/auth/logout").contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NULL)
                                                    .description("빈 응답")
                                                    .optional(),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }
    }
}
