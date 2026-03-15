package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.navigation.GnbQueryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.GnbV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.GnbQueryV1ApiMapper;
import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.application.navigation.port.in.query.SearchNavigationMenusUseCase;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
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
 * GnbQueryV1Controller REST Docs 테스트.
 *
 * <p>GNB 조회 v1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("GnbQueryV1Controller REST Docs 테스트")
@WebMvcTest(GnbQueryV1Controller.class)
@WithMockUser
class GnbQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchNavigationMenusUseCase searchNavigationMenusUseCase;

    @MockBean private GnbQueryV1ApiMapper queryMapper;

    @Nested
    @DisplayName("GNB 목록 조회 API")
    class GetGnbsTest {

        @Test
        @DisplayName("GNB 목록 조회 성공")
        void getGnbs_Success() throws Exception {
            // given
            List<NavigationMenu> menus = GnbQueryV1ApiFixtures.multipleNavigationMenus();
            List<GnbV1ApiResponse> responses = GnbQueryV1ApiFixtures.gnbResponseList();

            given(queryMapper.toSearchParams(any()))
                    .willReturn(new NavigationMenuSearchParams(null, null));
            given(searchNavigationMenusUseCase.execute(any())).willReturn(menus);
            given(queryMapper.toGnbResponse(any()))
                    .willReturn(
                            GnbQueryV1ApiFixtures.gnbResponse(
                                    GnbQueryV1ApiFixtures.DEFAULT_GNB_ID));

            // when & then
            mockMvc.perform(
                            get("/api/v1/content/gnbs")
                                    .param("startDate", "2025-01-01 00:00:00")
                                    .param("endDate", "2025-12-31 23:59:59"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("startDate")
                                                    .description(
                                                            "조회 시작일 (yyyy-MM-dd HH:mm:ss, KST) +\n"
                                                                    + "미입력 시 시작일 필터 없음")
                                                    .optional(),
                                            parameterWithName("endDate")
                                                    .description(
                                                            "조회 종료일 (yyyy-MM-dd HH:mm:ss, KST) +\n"
                                                                    + "미입력 시 종료일 필터 없음")
                                                    .optional(),
                                            parameterWithName("searchKeyword")
                                                    .description(
                                                            "검색 키워드 타입 +\n"
                                                                    + "INSERT_OPERATOR,"
                                                                    + " UPDATE_OPERATOR +\n"
                                                                    + "(현재 도메인에서 미지원, 무시됨)")
                                                    .optional(),
                                            parameterWithName("searchWord")
                                                    .description("검색어 (현재 도메인에서 미지원, 무시됨)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("GNB 메뉴 목록"),
                                            fieldWithPath("data[].gnbId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("GNB ID (navigationMenuId)"),
                                            fieldWithPath("data[].gnbDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("GNB 상세 정보"),
                                            fieldWithPath("data[].gnbDetails.title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메뉴 제목"),
                                            fieldWithPath("data[].gnbDetails.linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이동 URL"),
                                            fieldWithPath("data[].gnbDetails.displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전시 순서"),
                                            fieldWithPath("data[].gnbDetails.displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간 정보"),
                                            fieldWithPath(
                                                            "data[].gnbDetails.displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 시작일시 (KST)"),
                                            fieldWithPath(
                                                            "data[].gnbDetails.displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 종료일시 (KST)"),
                                            fieldWithPath("data[].gnbDetails.displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 여부 (Y/N)"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("날짜 필터 없이 GNB 목록 조회 성공")
        void getGnbs_NoFilter_Success() throws Exception {
            // given
            List<NavigationMenu> menus = GnbQueryV1ApiFixtures.multipleNavigationMenus();

            given(queryMapper.toSearchParams(any()))
                    .willReturn(new NavigationMenuSearchParams(null, null));
            given(searchNavigationMenusUseCase.execute(any())).willReturn(menus);
            given(queryMapper.toGnbResponse(any()))
                    .willReturn(
                            GnbQueryV1ApiFixtures.gnbResponse(
                                    GnbQueryV1ApiFixtures.DEFAULT_GNB_ID));

            // when & then
            mockMvc.perform(get("/api/v1/content/gnbs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("빈 GNB 목록 조회 성공")
        void getGnbs_Empty_Success() throws Exception {
            // given
            given(queryMapper.toSearchParams(any()))
                    .willReturn(new NavigationMenuSearchParams(null, null));
            given(searchNavigationMenusUseCase.execute(any())).willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/v1/content/gnbs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
