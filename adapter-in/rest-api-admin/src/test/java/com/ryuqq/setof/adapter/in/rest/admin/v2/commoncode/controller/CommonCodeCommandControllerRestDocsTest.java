package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.commoncode.CommonCodeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.ChangeActiveStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.RegisterCommonCodeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.UpdateCommonCodeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.mapper.CommonCodeCommandApiMapper;
import com.ryuqq.setof.application.commoncode.port.in.command.ChangeCommonCodeStatusUseCase;
import com.ryuqq.setof.application.commoncode.port.in.command.RegisterCommonCodeUseCase;
import com.ryuqq.setof.application.commoncode.port.in.command.UpdateCommonCodeUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * CommonCodeCommandController REST Docs 테스트.
 *
 * <p>공통코드 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommonCodeCommandController REST Docs 테스트")
@WebMvcTest(CommonCodeCommandController.class)
@WithMockUser
class CommonCodeCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterCommonCodeUseCase registerUseCase;

    @MockBean private UpdateCommonCodeUseCase updateUseCase;

    @MockBean private ChangeCommonCodeStatusUseCase changeStatusUseCase;

    @MockBean private CommonCodeCommandApiMapper mapper;

    private static final Long CODE_ID = 100L;

    @Nested
    @DisplayName("공통코드 등록 API")
    class RegisterCommonCodeTest {

        @Test
        @DisplayName("공통코드 등록 성공")
        void registerCommonCode_Success() throws Exception {
            // given
            RegisterCommonCodeApiRequest request = CommonCodeApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(CODE_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/common-codes")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").value(CODE_ID))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("commonCodeTypeId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("공통 코드 타입 ID [필수]"),
                                            fieldWithPath("code")
                                                    .type(JsonFieldType.STRING)
                                                    .description("코드 [필수, 최대 50자]"),
                                            fieldWithPath("displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명 [필수, 최대 100자]"),
                                            fieldWithPath("displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("표시 순서 [필수, 0 이상]")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 공통코드 ID"),
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
    @DisplayName("공통코드 수정 API")
    class UpdateCommonCodeTest {

        @Test
        @DisplayName("공통코드 수정 성공")
        void updateCommonCode_Success() throws Exception {
            // given
            UpdateCommonCodeApiRequest request = CommonCodeApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put("/api/v2/admin/common-codes/{id}", CODE_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andDo(
                            document.document(
                                    pathParameters(parameterWithName("id").description("공통코드 ID")),
                                    requestFields(
                                            fieldWithPath("displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명 [필수, 최대 100자]"),
                                            fieldWithPath("displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("표시 순서 [필수, 0 이상]")),
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

    @Nested
    @DisplayName("공통코드 상태 변경 API")
    class ChangeActiveStatusTest {

        @Test
        @DisplayName("공통코드 다건 상태 변경 성공")
        void changeActiveStatus_Success() throws Exception {
            // given
            ChangeActiveStatusApiRequest request =
                    CommonCodeApiFixtures.activateRequest(List.of(1L, 2L, 3L));
            willDoNothing().given(changeStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v2/admin/common-codes/active-status")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("ids")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상태 변경할 공통코드 ID 목록 [필수]"),
                                            fieldWithPath("active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description(
                                                            "변경할 활성화 상태 [필수] +\n"
                                                                    + "true: 활성화, false: 비활성화")),
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
