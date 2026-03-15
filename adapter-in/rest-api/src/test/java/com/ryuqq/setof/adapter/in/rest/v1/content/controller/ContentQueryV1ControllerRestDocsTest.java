package com.ryuqq.setof.adapter.in.rest.v1.content.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.content.ContentApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.content.ContentV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentMetaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.OnDisplayContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.mapper.ContentV1ApiMapper;
import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageDetailUseCase;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageMetaUseCase;
import com.ryuqq.setof.application.contentpage.port.in.GetOnDisplayContentPageIdsUseCase;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ContentQueryV1Controller REST Docs 테스트.
 *
 * <p>콘텐츠 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentQueryV1Controller REST Docs 테스트")
@WebMvcTest(ContentQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class ContentQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetOnDisplayContentPageIdsUseCase getOnDisplayContentPageIdsUseCase;

    @MockBean private GetContentPageMetaUseCase getContentPageMetaUseCase;

    @MockBean private GetContentPageDetailUseCase getContentPageDetailUseCase;

    @MockBean private ContentV1ApiMapper mapper;

    @Nested
    @DisplayName("전시 중인 콘텐츠 ID 목록 조회 API")
    class FetchOnDisplayContentsTest {

        @Test
        @DisplayName("전시 중인 콘텐츠 ID 목록 조회 성공")
        void fetchOnDisplayContents_Success() throws Exception {
            // given
            Set<Long> contentIds = ContentApiFixtures.onDisplayContentIds();
            OnDisplayContentV1ApiResponse response = ContentApiFixtures.onDisplayResponse();

            given(getOnDisplayContentPageIdsUseCase.execute()).willReturn(contentIds);
            given(mapper.toOnDisplayResponse(contentIds)).willReturn(response);

            // when & then
            mockMvc.perform(get(ContentV1Endpoints.CONTENT + "/on-display"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentIds").isArray())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data.contentIds")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("전시 중인 콘텐츠 ID 목록"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("전시 중인 콘텐츠가 없는 경우 빈 ID 목록 반환")
        void fetchOnDisplayContents_Empty() throws Exception {
            // given
            given(getOnDisplayContentPageIdsUseCase.execute()).willReturn(Set.of());
            given(mapper.toOnDisplayResponse(Set.of()))
                    .willReturn(new OnDisplayContentV1ApiResponse(Set.of()));

            // when & then
            mockMvc.perform(get(ContentV1Endpoints.CONTENT + "/on-display"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentIds").isArray())
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }

    @Nested
    @DisplayName("콘텐츠 메타데이터 조회 API")
    class FetchContentMetaDataTest {

        @Test
        @DisplayName("콘텐츠 메타데이터 조회 성공")
        void fetchContentMetaData_Success() throws Exception {
            // given
            long contentId = 1L;
            ContentPage page = ContentApiFixtures.contentPage(contentId);
            ContentMetaV1ApiResponse response = ContentApiFixtures.contentMetaResponse(contentId);

            given(getContentPageMetaUseCase.execute(eq(contentId))).willReturn(page);
            given(mapper.toMetaResponse(page)).willReturn(response);

            // when & then
            mockMvc.perform(get(ContentV1Endpoints.CONTENT + "/meta/{contentId}", contentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentId").value(contentId))
                    .andExpect(jsonPath("$.data.title").value("메인 콘텐츠"))
                    .andExpect(jsonPath("$.data.componentDetails").isArray())
                    .andExpect(jsonPath("$.data.componentDetails.length()").value(0))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("contentId").description("콘텐츠 ID")),
                                    responseFields(
                                            fieldWithPath("data.contentId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("콘텐츠 ID"),
                                            fieldWithPath("data.displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간"),
                                            fieldWithPath("data.displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 시작일"),
                                            fieldWithPath("data.displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 종료일"),
                                            fieldWithPath("data.title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("콘텐츠 제목"),
                                            fieldWithPath("data.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모"),
                                            fieldWithPath("data.imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL"),
                                            fieldWithPath("data.componentDetails")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("컴포넌트 상세 목록 (메타 조회 시 항상 빈 리스트)"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("콘텐츠 상세 조회 API")
    class FetchContentTest {

        @Test
        @DisplayName("콘텐츠 상세 조회 성공 (bypass=N)")
        void fetchContent_Success() throws Exception {
            // given
            long contentId = 1L;
            ContentPageDetailResult detail =
                    ContentApiFixtures.contentPageDetailResultEmpty(contentId);
            ContentV1ApiResponse response = ContentApiFixtures.contentResponse(contentId);

            given(getContentPageDetailUseCase.execute(any())).willReturn(detail);
            given(mapper.toContentResponse(any(), any(), any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get(ContentV1Endpoints.CONTENT + "/{contentId}", contentId)
                                    .param("bypass", "N"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentId").value(contentId))
                    .andExpect(jsonPath("$.data.title").value("메인 콘텐츠"))
                    .andExpect(jsonPath("$.data.componentDetails").isArray())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("contentId").description("콘텐츠 ID")),
                                    queryParameters(
                                            parameterWithName("bypass")
                                                    .description("전시 기간 체크 우회 여부 (Y/N). 미입력 시 N 처리")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.contentId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("콘텐츠 ID"),
                                            fieldWithPath("data.displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간"),
                                            fieldWithPath("data.displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 시작일"),
                                            fieldWithPath("data.displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 종료일"),
                                            fieldWithPath("data.title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("콘텐츠 제목"),
                                            fieldWithPath("data.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모"),
                                            fieldWithPath("data.imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL"),
                                            fieldWithPath("data.componentDetails")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("컴포넌트 상세 목록"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("콘텐츠 상세 조회 성공 (bypass=Y, 전시 기간 우회)")
        void fetchContent_WithBypass() throws Exception {
            // given
            long contentId = 1L;
            ContentPageDetailResult detail =
                    ContentApiFixtures.contentPageDetailResultEmpty(contentId);
            ContentV1ApiResponse response = ContentApiFixtures.contentResponse(contentId);

            given(getContentPageDetailUseCase.execute(any())).willReturn(detail);
            given(mapper.toContentResponse(any(), any(), any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get(ContentV1Endpoints.CONTENT + "/{contentId}", contentId)
                                    .param("bypass", "Y"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentId").value(contentId))
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("bypass 파라미터 없이 콘텐츠 상세 조회 성공")
        void fetchContent_NoBypassParam() throws Exception {
            // given
            long contentId = 1L;
            ContentPageDetailResult detail =
                    ContentApiFixtures.contentPageDetailResultEmpty(contentId);
            ContentV1ApiResponse response = ContentApiFixtures.contentResponse(contentId);

            given(getContentPageDetailUseCase.execute(any())).willReturn(detail);
            given(mapper.toContentResponse(any(), any(), any())).willReturn(response);

            // when & then
            mockMvc.perform(get(ContentV1Endpoints.CONTENT + "/{contentId}", contentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentId").value(contentId))
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
