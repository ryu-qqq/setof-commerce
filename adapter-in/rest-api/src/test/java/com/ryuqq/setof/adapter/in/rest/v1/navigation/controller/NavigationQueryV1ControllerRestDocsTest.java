package com.ryuqq.setof.adapter.in.rest.v1.navigation.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.NavigationApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.NavigationV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.dto.response.NavigationMenuV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.navigation.mapper.NavigationV1ApiMapper;
import com.ryuqq.setof.application.navigation.port.in.NavigationMenuQueryUseCase;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;
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
 * NavigationQueryV1Controller REST Docs 테스트.
 *
 * <p>네비게이션 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("NavigationQueryV1Controller REST Docs 테스트")
@WebMvcTest(NavigationQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class NavigationQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private NavigationMenuQueryUseCase navigationMenuQueryUseCase;

    @MockBean private NavigationV1ApiMapper mapper;

    @Nested
    @DisplayName("GNB 메뉴 목록 조회 API")
    class GetGnbsTest {

        @Test
        @DisplayName("GNB 메뉴 목록 조회 성공")
        void getGnbs_Success() throws Exception {
            // given
            List<NavigationMenu> menus = NavigationApiFixtures.navigationMenuList();
            List<NavigationMenuV1ApiResponse> response =
                    NavigationApiFixtures.navigationMenuResponseList();

            given(navigationMenuQueryUseCase.fetchNavigationMenus()).willReturn(menus);
            given(mapper.toListResponse(menus)).willReturn(response);

            // when & then
            mockMvc.perform(get(NavigationV1Endpoints.GNBS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(3))
                    .andExpect(jsonPath("$.data[0].gnbId").value(1))
                    .andExpect(jsonPath("$.data[0].title").value("신상품"))
                    .andExpect(jsonPath("$.data[0].linkUrl").value("/new-arrivals"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    responseFields(
                                            fieldWithPath("data[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("GNB 메뉴 목록"),
                                            fieldWithPath("data[].gnbId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("GNB ID"),
                                            fieldWithPath("data[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메뉴 타이틀"),
                                            fieldWithPath("data[].linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이동 링크 URL"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("GNB 메뉴가 없는 경우 빈 목록 반환")
        void getGnbs_Empty() throws Exception {
            // given
            given(navigationMenuQueryUseCase.fetchNavigationMenus()).willReturn(List.of());
            given(mapper.toListResponse(List.of())).willReturn(List.of());

            // when & then
            mockMvc.perform(get(NavigationV1Endpoints.GNBS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(0))
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
