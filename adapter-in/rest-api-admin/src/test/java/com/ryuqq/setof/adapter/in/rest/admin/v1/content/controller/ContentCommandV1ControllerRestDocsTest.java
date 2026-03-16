package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.content.ContentCommandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateContentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateContentDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.ContentCommandV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.ContentQueryV1ApiMapper;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageMetaUseCase;
import com.ryuqq.setof.application.contentpage.port.in.command.ChangeContentPageStatusUseCase;
import com.ryuqq.setof.application.contentpage.port.in.command.RegisterContentPageUseCase;
import com.ryuqq.setof.application.contentpage.port.in.command.UpdateContentPageUseCase;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * ContentCommandV1Controller REST Docs 테스트.
 *
 * <p>콘텐츠 등록/수정/상태변경 v1 API의 REST Docs 스니펫을 생성합니다.
 *
 * <p>테스트 대상 API:
 *
 * <ul>
 *   <li>POST /api/v1/content — 콘텐츠 등록 (contentId null)
 *   <li>POST /api/v1/content — 콘텐츠 수정 (contentId 양수)
 *   <li>PATCH /api/v1/content/{contentId}/display-status — 노출 상태 변경
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentCommandV1Controller REST Docs 테스트")
@WebMvcTest(ContentCommandV1Controller.class)
@WithMockUser
class ContentCommandV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private ContentCommandV1ApiMapper commandMapper;
    @MockBean private ContentQueryV1ApiMapper queryMapper;
    @MockBean private RegisterContentPageUseCase registerContentPageUseCase;
    @MockBean private UpdateContentPageUseCase updateContentPageUseCase;
    @MockBean private ChangeContentPageStatusUseCase changeContentPageStatusUseCase;
    @MockBean private GetContentPageMetaUseCase getContentPageMetaUseCase;

    private static final long CONTENT_ID = ContentCommandV1ApiFixtures.DEFAULT_CONTENT_ID;

    @Nested
    @DisplayName("콘텐츠 등록 API (contentId null)")
    class RegisterContentTest {

        @Test
        @DisplayName("contentId가 null인 요청은 등록 처리 후 ContentV1ApiResponse를 반환한다")
        void enrollContent_Register_Success() throws Exception {
            // given
            CreateContentV1ApiRequest request = ContentCommandV1ApiFixtures.createRequest();
            ContentPage contentPage = ContentCommandV1ApiFixtures.contentPage(CONTENT_ID);
            ContentV1ApiResponse response = ContentCommandV1ApiFixtures.contentResponse(CONTENT_ID);

            given(commandMapper.toRegisterCommand(any())).willReturn(null);
            given(registerContentPageUseCase.execute(any())).willReturn(CONTENT_ID);
            given(getContentPageMetaUseCase.execute(anyLong())).willReturn(contentPage);
            given(queryMapper.toContentResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            post("/api/v1/content")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentId").value(CONTENT_ID))
                    .andExpect(jsonPath("$.data.displayYn").value("Y"))
                    .andExpect(jsonPath("$.data.title").value("메인 콘텐츠"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("contentId")
                                                    .type(JsonFieldType.NULL)
                                                    .description("콘텐츠 ID [null이면 등록, 양수이면 수정]")
                                                    .optional(),
                                            fieldWithPath("displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간 [필수]"),
                                            fieldWithPath("displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "전시 시작일시 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "전시 종료일시 [필수, yyyy-MM-dd HH:mm:ss]"),
                                            fieldWithPath("title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("콘텐츠 제목 [최대 50자]")
                                                    .optional(),
                                            fieldWithPath("memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모 [최대 200자]")
                                                    .optional(),
                                            fieldWithPath("imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표 이미지 URL")
                                                    .optional(),
                                            fieldWithPath("displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 여부 [필수, Y/N]"),
                                            fieldWithPath("components")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("하위 컴포넌트 목록 [최소 1개]"),
                                            fieldWithPath("components[].type")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "컴포넌트 타입 +\n"
                                                                    + "brandComponentDetail,"
                                                                    + " categoryComponentDetail,"
                                                                    + " productComponentDetail,"
                                                                    + " tabComponentDetail,"
                                                                    + " imageComponentDetail,"
                                                                    + " textComponentDetail,"
                                                                    + " titleComponentDetail,"
                                                                    + " blankComponentDetail"),
                                            fieldWithPath("components[].brandComponentId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "브랜드 컴포넌트 ID [brandComponentDetail 전용]")
                                                    .optional(),
                                            fieldWithPath("components[].componentId")
                                                    .type(JsonFieldType.NULL)
                                                    .description(
                                                            "컴포넌트 ID [null이면 신규, 값이 있으면 기존 수정]")
                                                    .optional(),
                                            fieldWithPath("components[].displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("컴포넌트 전시 기간 [필수]"),
                                            fieldWithPath(
                                                            "components[].displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "컴포넌트 전시 시작일시 [필수, yyyy-MM-dd"
                                                                    + " HH:mm:ss]"),
                                            fieldWithPath(
                                                            "components[].displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "컴포넌트 전시 종료일시 [필수, yyyy-MM-dd"
                                                                    + " HH:mm:ss]"),
                                            fieldWithPath("components[].componentName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("컴포넌트 이름"),
                                            fieldWithPath("components[].displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("노출 순서"),
                                            fieldWithPath("components[].displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("컴포넌트 전시 여부 [Y/N]"),
                                            fieldWithPath("components[].categoryId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description(
                                                            "카테고리 ID [brandComponentDetail 전용]")
                                                    .optional(),
                                            fieldWithPath("components[].brandList")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("브랜드 목록 [brandComponentDetail 전용]")
                                                    .optional(),
                                            fieldWithPath("components[].brandList[].brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID")
                                                    .optional(),
                                            fieldWithPath("components[].brandList[].brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명")
                                                    .optional(),
                                            fieldWithPath("components[].componentDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("컴포넌트 상세 설정 [선택]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].componentDetails.componentType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("컴포넌트 타입")
                                                    .optional(),
                                            fieldWithPath("components[].componentDetails.listType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("리스트 타입")
                                                    .optional(),
                                            fieldWithPath("components[].componentDetails.orderType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정렬 타입")
                                                    .optional(),
                                            fieldWithPath("components[].componentDetails.badgeType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("뱃지 타입")
                                                    .optional(),
                                            fieldWithPath("components[].componentDetails.filterYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("필터 사용 여부 [Y/N]")
                                                    .optional(),
                                            fieldWithPath("components[].exposedProducts")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("노출 상품 수 [기본 0]")
                                                    .optional(),
                                            fieldWithPath("components[].viewExtensionId")
                                                    .type(JsonFieldType.NULL)
                                                    .description("뷰 확장 ID [선택]")
                                                    .optional(),
                                            fieldWithPath("components[].viewExtensionDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("뷰 확장 상세 설정 [선택]")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].viewExtensionDetails.viewExtensionType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("뷰 확장 타입")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].viewExtensionDetails.linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("링크 URL")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].viewExtensionDetails.buttonName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("버튼 이름")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].viewExtensionDetails.productCountPerClick")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("클릭당 상품 수")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].viewExtensionDetails.maxClickCount")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 클릭 수")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].viewExtensionDetails.afterMaxActionType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("최대 이후 액션 타입")
                                                    .optional(),
                                            fieldWithPath(
                                                            "components[].viewExtensionDetails.afterMaxActionLinkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("최대 이후 액션 링크 URL")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.contentId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("콘텐츠 ID"),
                                            fieldWithPath("data.displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 여부 (Y/N)"),
                                            fieldWithPath("data.title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("콘텐츠 제목"),
                                            fieldWithPath("data.displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간 정보"),
                                            fieldWithPath("data.displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 시작일시 (KST)"),
                                            fieldWithPath("data.displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 종료일시 (KST)"),
                                            fieldWithPath("data.insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자 (항상 빈 문자열)"),
                                            fieldWithPath("data.updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자 (항상 빈 문자열)"),
                                            fieldWithPath("data.insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시 (KST)"),
                                            fieldWithPath("data.updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (KST)"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("콘텐츠 수정 API (contentId 양수)")
    class UpdateContentTest {

        @Test
        @DisplayName("contentId가 양수인 요청은 수정 처리 후 ContentV1ApiResponse를 반환한다")
        void enrollContent_Update_Success() throws Exception {
            // given
            CreateContentV1ApiRequest request =
                    ContentCommandV1ApiFixtures.updateRequest(CONTENT_ID);
            ContentPage contentPage = ContentCommandV1ApiFixtures.contentPage(CONTENT_ID);
            ContentV1ApiResponse response = ContentCommandV1ApiFixtures.contentResponse(CONTENT_ID);

            given(commandMapper.toUpdateCommand(any())).willReturn(null);
            willDoNothing().given(updateContentPageUseCase).execute(any());
            given(getContentPageMetaUseCase.execute(anyLong())).willReturn(contentPage);
            given(queryMapper.toContentResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            post("/api/v1/content")
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentId").value(CONTENT_ID))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"));
        }
    }

    @Nested
    @DisplayName("콘텐츠 노출 상태 변경 API")
    class ChangeDisplayStatusTest {

        @Test
        @DisplayName("displayYn 'Y'로 노출 상태 변경 성공")
        void updateContentDisplayYn_ActiveY_Success() throws Exception {
            // given
            UpdateContentDisplayYnV1ApiRequest request = ContentCommandV1ApiFixtures.displayYnY();
            ContentPage contentPage = ContentCommandV1ApiFixtures.contentPage(CONTENT_ID);
            ContentV1ApiResponse response = ContentCommandV1ApiFixtures.contentResponse(CONTENT_ID);

            given(commandMapper.toChangeStatusCommand(anyLong(), any())).willReturn(null);
            willDoNothing().given(changeContentPageStatusUseCase).execute(any());
            given(getContentPageMetaUseCase.execute(anyLong())).willReturn(contentPage);
            given(queryMapper.toContentResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            patch("/api/v1/content/{contentId}/display-status", CONTENT_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.contentId").value(CONTENT_ID))
                    .andExpect(jsonPath("$.data.displayYn").value("Y"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("contentId")
                                                    .description("콘텐츠 ID [필수]")),
                                    requestFields(
                                            fieldWithPath("displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 여부 [필수, Y: 전시, N: 비전시]")),
                                    responseFields(
                                            fieldWithPath("data.contentId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("콘텐츠 ID"),
                                            fieldWithPath("data.displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("변경된 전시 여부 (Y/N)"),
                                            fieldWithPath("data.title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("콘텐츠 제목"),
                                            fieldWithPath("data.displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간 정보"),
                                            fieldWithPath("data.displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 시작일시 (KST)"),
                                            fieldWithPath("data.displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 종료일시 (KST)"),
                                            fieldWithPath("data.insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자 (항상 빈 문자열)"),
                                            fieldWithPath("data.updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자 (항상 빈 문자열)"),
                                            fieldWithPath("data.insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시 (KST)"),
                                            fieldWithPath("data.updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (KST)"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("displayYn 'N'으로 노출 상태 변경 성공")
        void updateContentDisplayYn_ActiveN_Success() throws Exception {
            // given
            UpdateContentDisplayYnV1ApiRequest request = ContentCommandV1ApiFixtures.displayYnN();
            ContentPage contentPage = ContentCommandV1ApiFixtures.contentPage(CONTENT_ID);
            ContentV1ApiResponse responseDto =
                    ContentCommandV1ApiFixtures.contentResponse(CONTENT_ID);

            given(commandMapper.toChangeStatusCommand(anyLong(), any())).willReturn(null);
            willDoNothing().given(changeContentPageStatusUseCase).execute(any());
            given(getContentPageMetaUseCase.execute(anyLong())).willReturn(contentPage);
            given(queryMapper.toContentResponse(any())).willReturn(responseDto);

            // when & then
            mockMvc.perform(
                            patch("/api/v1/content/{contentId}/display-status", CONTENT_ID)
                                    .contentType(APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
