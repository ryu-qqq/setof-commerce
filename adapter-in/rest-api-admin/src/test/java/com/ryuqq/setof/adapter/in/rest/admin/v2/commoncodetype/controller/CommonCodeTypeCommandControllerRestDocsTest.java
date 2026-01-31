package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.commoncodetype.CommonCodeTypeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.ChangeActiveStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.RegisterCommonCodeTypeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.UpdateCommonCodeTypeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.mapper.CommonCodeTypeCommandApiMapper;
import com.ryuqq.setof.application.commoncodetype.port.in.command.ChangeCommonCodeTypeStatusUseCase;
import com.ryuqq.setof.application.commoncodetype.port.in.command.RegisterCommonCodeTypeUseCase;
import com.ryuqq.setof.application.commoncodetype.port.in.command.UpdateCommonCodeTypeUseCase;
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
 * CommonCodeTypeCommandController REST Docs 테스트.
 *
 * <p>공통코드 타입 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommonCodeTypeCommandController REST Docs 테스트")
@WebMvcTest(CommonCodeTypeCommandController.class)
@WithMockUser
class CommonCodeTypeCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterCommonCodeTypeUseCase registerUseCase;

    @MockBean private UpdateCommonCodeTypeUseCase updateUseCase;

    @MockBean private ChangeCommonCodeTypeStatusUseCase changeStatusUseCase;

    @MockBean private CommonCodeTypeCommandApiMapper mapper;

    private static final Long TYPE_ID = 1L;

    @Nested
    @DisplayName("공통코드 타입 등록 API")
    class RegisterCommonCodeTypeTest {

        @Test
        @DisplayName("공통코드 타입 등록 성공")
        void registerCommonCodeType_Success() throws Exception {
            // given
            RegisterCommonCodeTypeApiRequest request = CommonCodeTypeApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(TYPE_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/common-code-types")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").value(TYPE_ID))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("code")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "코드 [필수, 최대 50자] +\n"
                                                                    + "영문 대문자, 언더스코어만 허용"),
                                            fieldWithPath("name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이름 [필수, 최대 100자]"),
                                            fieldWithPath("description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명 [최대 500자]")
                                                    .optional(),
                                            fieldWithPath("displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("표시 순서 [필수, 0 이상]")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 공통코드 타입 ID"),
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
    @DisplayName("공통코드 타입 수정 API")
    class UpdateCommonCodeTypeTest {

        @Test
        @DisplayName("공통코드 타입 수정 성공")
        void updateCommonCodeType_Success() throws Exception {
            // given
            UpdateCommonCodeTypeApiRequest request = CommonCodeTypeApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put("/api/v2/admin/common-code-types/{commonCodeTypeId}", TYPE_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("commonCodeTypeId")
                                                    .description("공통코드 타입 ID")),
                                    requestFields(
                                            fieldWithPath("name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이름 [필수, 최대 100자]"),
                                            fieldWithPath("description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명 [최대 500자]")
                                                    .optional(),
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
    @DisplayName("공통코드 타입 상태 변경 API")
    class ChangeActiveStatusTest {

        @Test
        @DisplayName("공통코드 타입 다건 상태 변경 성공")
        void changeActiveStatus_Success() throws Exception {
            // given
            ChangeActiveStatusApiRequest request =
                    CommonCodeTypeApiFixtures.activateRequest(List.of(1L, 2L, 3L));
            willDoNothing().given(changeStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch("/api/v2/admin/common-code-types/active-status")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("ids")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("상태 변경할 공통코드 타입 ID 목록 [필수]"),
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
