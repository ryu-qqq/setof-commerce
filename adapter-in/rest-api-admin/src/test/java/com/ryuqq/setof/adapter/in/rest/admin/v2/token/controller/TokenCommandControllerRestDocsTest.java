package com.ryuqq.setof.adapter.in.rest.admin.v2.token.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.token.TokenApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.token.dto.command.LoginApiRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * TokenCommandController REST Docs 테스트.
 *
 * <p>인증 토큰 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("TokenCommandController REST Docs 테스트")
@WebMvcTest(TokenCommandController.class)
@WithMockUser
class TokenCommandControllerRestDocsTest extends RestDocsTestSupport {

    @Nested
    @DisplayName("로그인 API")
    class LoginTest {

        @Test
        @DisplayName("로그인 성공")
        void login_Success() throws Exception {
            // given
            LoginApiRequest request = TokenApiFixtures.loginRequest();

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/auth/login")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("identifier")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "사용자 식별자 [필수, 최대 100자] +\n"
                                                                    + "이메일 또는 아이디"),
                                            fieldWithPath("password")
                                                    .type(JsonFieldType.STRING)
                                                    .description("비밀번호 [필수, 최대 100자]")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("로그인 결과 (토큰 정보)"),
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
