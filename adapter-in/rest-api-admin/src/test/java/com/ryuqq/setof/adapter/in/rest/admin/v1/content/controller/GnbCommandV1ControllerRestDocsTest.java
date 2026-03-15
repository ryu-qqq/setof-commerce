package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.navigation.GnbCommandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateGnbV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.GnbCommandV1ApiMapper;
import com.ryuqq.setof.application.navigation.port.in.command.RegisterNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.port.in.command.RemoveNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.port.in.command.UpdateNavigationMenuUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * GnbCommandV1Controller REST Docs 테스트.
 *
 * <p>GNB 등록/수정/삭제 일괄 처리 v1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("GnbCommandV1Controller REST Docs 테스트")
@WebMvcTest(GnbCommandV1Controller.class)
@WithMockUser
class GnbCommandV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterNavigationMenuUseCase registerUseCase;

    @MockBean private UpdateNavigationMenuUseCase updateUseCase;

    @MockBean private RemoveNavigationMenuUseCase removeUseCase;

    @MockBean private GnbCommandV1ApiMapper mapper;

    @Nested
    @DisplayName("GNB 일괄 등록/수정/삭제 API")
    class EnrollGnbsTest {

        @Test
        @DisplayName("등록 + 수정 + 삭제 일괄 처리 성공")
        void enrollGnbs_BulkRequest_Success() throws Exception {
            // given
            UpdateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.bulkRequest();
            given(registerUseCase.execute(any())).willReturn(10L);
            willDoNothing().given(updateUseCase).execute(any());
            willDoNothing().given(removeUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post("/api/v1/content/gnbs")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("toUpdateGnbs")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description(
                                                            "등록/수정 대상 GNB 목록 [선택, gnbId null이면 등록,"
                                                                    + " 있으면 수정]")
                                                    .optional(),
                                            fieldWithPath("toUpdateGnbs[].gnbId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("GNB ID [null이면 신규 등록]")
                                                    .optional(),
                                            fieldWithPath("toUpdateGnbs[].gnbDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("GNB 상세 정보 [필수]"),
                                            fieldWithPath("toUpdateGnbs[].gnbDetails.title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메뉴 제목 [필수]"),
                                            fieldWithPath("toUpdateGnbs[].gnbDetails.linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이동 URL [필수]"),
                                            fieldWithPath("toUpdateGnbs[].gnbDetails.displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("노출 순서 [필수]"),
                                            fieldWithPath("toUpdateGnbs[].gnbDetails.displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간 [필수]"),
                                            fieldWithPath(
                                                            "toUpdateGnbs[].gnbDetails.displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "전시 시작일시 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath(
                                                            "toUpdateGnbs[].gnbDetails.displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "전시 종료일시 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("toUpdateGnbs[].gnbDetails.displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 여부 [필수, Y/N]"),
                                            fieldWithPath("deleteGnbIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("삭제 대상 GNB ID 목록 [선택]")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("신규 등록된 GNB ID 목록"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("등록만 포함된 요청 성공")
        void enrollGnbs_CreateOnly_Success() throws Exception {
            // given
            UpdateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.createOnlyRequest();
            given(registerUseCase.execute(any())).willReturn(11L);

            // when & then
            mockMvc.perform(
                            post("/api/v1/content/gnbs")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("삭제만 포함된 요청 성공")
        void enrollGnbs_DeleteOnly_Success() throws Exception {
            // given
            UpdateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.deleteOnlyRequest();
            willDoNothing().given(removeUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            post("/api/v1/content/gnbs")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
