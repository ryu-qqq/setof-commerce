package com.ryuqq.setof.adapter.in.rest.admin.v2.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.auth.AuthApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.MyInfoApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.mapper.AuthQueryApiMapper;
import com.ryuqq.setof.application.auth.dto.response.MyInfoResult;
import com.ryuqq.setof.application.auth.port.in.GetMyInfoUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * AuthQueryController REST Docs 테스트.
 *
 * <p>인증 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("AuthQueryController REST Docs 테스트")
@WebMvcTest(AuthQueryController.class)
@WithMockUser
class AuthQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetMyInfoUseCase getMyInfoUseCase;

    @MockBean private AuthQueryApiMapper queryMapper;

    @Nested
    @DisplayName("내 정보 조회 API")
    class GetMyInfoTest {

        @Test
        @DisplayName("내 정보 조회 성공")
        void getMyInfo_Success() throws Exception {
            // given
            String authorization = AuthApiFixtures.bearerToken();
            MyInfoResult result = AuthApiFixtures.myInfoResult();
            MyInfoApiResponse response = AuthApiFixtures.myInfoApiResponse();

            given(queryMapper.extractToken(anyString()))
                    .willReturn(AuthApiFixtures.DEFAULT_ACCESS_TOKEN);
            given(getMyInfoUseCase.execute(anyString())).willReturn(result);
            given(queryMapper.toResponse(any(MyInfoResult.class))).willReturn(response);

            // when & then
            mockMvc.perform(
                            get("/api/v2/admin/auth/me")
                                    .header("Authorization", authorization)
                                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.userId").value(response.userId()))
                    .andExpect(jsonPath("$.data.email").value(response.email()))
                    .andExpect(jsonPath("$.data.name").value(response.name()))
                    .andDo(
                            document.document(
                                    requestHeaders(
                                            headerWithName("Authorization")
                                                    .description("Bearer 액세스 토큰")),
                                    responseFields(
                                            fieldWithPath("data.userId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사용자 ID"),
                                            fieldWithPath("data.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이메일"),
                                            fieldWithPath("data.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사용자 이름"),
                                            fieldWithPath("data.tenantId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("테넌트 ID"),
                                            fieldWithPath("data.tenantName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("테넌트 이름"),
                                            fieldWithPath("data.organizationId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("조직 ID"),
                                            fieldWithPath("data.organizationName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("조직 이름"),
                                            fieldWithPath("data.roles")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("역할 목록"),
                                            fieldWithPath("data.roles[].id")
                                                    .type(JsonFieldType.STRING)
                                                    .description("역할 ID"),
                                            fieldWithPath("data.roles[].name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("역할 이름"),
                                            fieldWithPath("data.permissions")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("권한 목록"),
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
