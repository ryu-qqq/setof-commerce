package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.controller;

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

import com.ryuqq.setof.adapter.in.rest.admin.banner.BannerGroupApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.ChangeBannerGroupStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.RegisterBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerSlidesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.mapper.BannerGroupCommandApiMapper;
import com.ryuqq.setof.application.banner.port.in.command.ChangeBannerGroupStatusUseCase;
import com.ryuqq.setof.application.banner.port.in.command.RegisterBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.RemoveBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerGroupUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerSlidesUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * BannerGroupCommandController REST Docs 테스트.
 *
 * <p>배너 그룹 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerGroupCommandController REST Docs 테스트")
@WebMvcTest(BannerGroupCommandController.class)
@WithMockUser
class BannerGroupCommandControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private RegisterBannerGroupUseCase registerUseCase;

    @MockBean private UpdateBannerGroupUseCase updateUseCase;

    @MockBean private UpdateBannerSlidesUseCase updateSlidesUseCase;

    @MockBean private ChangeBannerGroupStatusUseCase changeStatusUseCase;

    @MockBean private RemoveBannerGroupUseCase removeUseCase;

    @MockBean private BannerGroupCommandApiMapper mapper;

    private static final Long BANNER_GROUP_ID = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;

    @Nested
    @DisplayName("배너 그룹 등록 API")
    class RegisterBannerGroupTest {

        @Test
        @DisplayName("배너 그룹 등록 성공")
        void registerBannerGroup_Success() throws Exception {
            // given
            RegisterBannerGroupApiRequest request = BannerGroupApiFixtures.registerRequest();
            given(registerUseCase.execute(any())).willReturn(BANNER_GROUP_ID);

            // when & then
            mockMvc.perform(
                            post("/api/v2/admin/banner-groups")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.bannerGroupId").value(BANNER_GROUP_ID))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 그룹명 [필수]"),
                                            fieldWithPath("bannerType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 타입 [필수, ex: MAIN_BANNER]"),
                                            fieldWithPath("displayStartAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("노출 시작 시각 [필수, ISO 8601]"),
                                            fieldWithPath("displayEndAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("노출 종료 시각 [필수, ISO 8601]"),
                                            fieldWithPath("active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("노출 여부 [필수]"),
                                            fieldWithPath("slides")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("슬라이드 등록 목록 [필수, 1개 이상]"),
                                            fieldWithPath("slides[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("슬라이드 제목 [필수]"),
                                            fieldWithPath("slides[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]"),
                                            fieldWithPath("slides[].linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("링크 URL [필수]"),
                                            fieldWithPath("slides[].displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("노출 순서 [필수, 0 이상]"),
                                            fieldWithPath("slides[].displayStartAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("슬라이드 노출 시작 시각 [필수, ISO 8601]"),
                                            fieldWithPath("slides[].displayEndAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("슬라이드 노출 종료 시각 [필수, ISO 8601]"),
                                            fieldWithPath("slides[].active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("슬라이드 노출 여부 [필수]")),
                                    responseFields(
                                            fieldWithPath("data.bannerGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("생성된 배너 그룹 ID"),
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
    @DisplayName("배너 그룹 수정 API")
    class UpdateBannerGroupTest {

        @Test
        @DisplayName("배너 그룹 수정 성공")
        void updateBannerGroup_Success() throws Exception {
            // given
            UpdateBannerGroupApiRequest request = BannerGroupApiFixtures.updateRequest();
            willDoNothing().given(updateUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put("/api/v2/admin/banner-groups/{bannerGroupId}", BANNER_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("bannerGroupId")
                                                    .description("수정할 배너 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 그룹명 [필수]"),
                                            fieldWithPath("bannerType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 타입 [필수, ex: MAIN_BANNER]"),
                                            fieldWithPath("displayStartAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("노출 시작 시각 [필수, ISO 8601]"),
                                            fieldWithPath("displayEndAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("노출 종료 시각 [필수, ISO 8601]"),
                                            fieldWithPath("active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("노출 여부 [필수]"))));
        }
    }

    @Nested
    @DisplayName("배너 슬라이드 일괄 수정 API")
    class UpdateBannerSlidesTest {

        @Test
        @DisplayName("배너 슬라이드 일괄 수정 성공")
        void updateBannerSlides_Success() throws Exception {
            // given
            UpdateBannerSlidesApiRequest request = BannerGroupApiFixtures.updateSlidesRequest();
            willDoNothing().given(updateSlidesUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            put(
                                            "/api/v2/admin/banner-groups/{bannerGroupId}/slides",
                                            BANNER_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("bannerGroupId")
                                                    .description("슬라이드를 수정할 배너 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("slides")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("슬라이드 수정 목록 [필수]"),
                                            fieldWithPath("slides[].slideId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "슬라이드 ID [선택, null이면 신규 생성,"
                                                                    + " 값이 있으면 기존 슬라이드 수정]")
                                                    .optional(),
                                            fieldWithPath("slides[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("슬라이드 제목 [필수]"),
                                            fieldWithPath("slides[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL [필수]"),
                                            fieldWithPath("slides[].linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("링크 URL [필수]"),
                                            fieldWithPath("slides[].displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("노출 순서 [필수, 0 이상]"),
                                            fieldWithPath("slides[].displayStartAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("슬라이드 노출 시작 시각 [필수, ISO 8601]"),
                                            fieldWithPath("slides[].displayEndAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("슬라이드 노출 종료 시각 [필수, ISO 8601]"),
                                            fieldWithPath("slides[].active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("슬라이드 노출 여부 [필수]"))));
        }
    }

    @Nested
    @DisplayName("배너 그룹 노출 상태 변경 API")
    class ChangeBannerGroupStatusTest {

        @Test
        @DisplayName("배너 그룹 노출 활성화 성공")
        void changeBannerGroupStatus_Active_Success() throws Exception {
            // given
            ChangeBannerGroupStatusApiRequest request =
                    BannerGroupApiFixtures.changeStatusActiveRequest();
            willDoNothing().given(changeStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch(
                                            "/api/v2/admin/banner-groups/{bannerGroupId}/active-status",
                                            BANNER_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("bannerGroupId")
                                                    .description("상태를 변경할 배너 그룹 ID")),
                                    requestFields(
                                            fieldWithPath("active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("변경할 노출 여부 [필수]"))));
        }

        @Test
        @DisplayName("배너 그룹 노출 비활성화 성공")
        void changeBannerGroupStatus_Inactive_Success() throws Exception {
            // given
            ChangeBannerGroupStatusApiRequest request =
                    BannerGroupApiFixtures.changeStatusInactiveRequest();
            willDoNothing().given(changeStatusUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch(
                                            "/api/v2/admin/banner-groups/{bannerGroupId}/active-status",
                                            BANNER_GROUP_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("배너 그룹 삭제 API")
    class RemoveBannerGroupTest {

        @Test
        @DisplayName("배너 그룹 소프트 삭제 성공")
        void removeBannerGroup_Success() throws Exception {
            // given
            willDoNothing().given(removeUseCase).execute(any());

            // when & then
            mockMvc.perform(
                            patch(
                                    "/api/v2/admin/banner-groups/{bannerGroupId}/remove",
                                    BANNER_GROUP_ID))
                    .andExpect(status().isNoContent())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("bannerGroupId")
                                                    .description("삭제할 배너 그룹 ID"))));
        }
    }
}
