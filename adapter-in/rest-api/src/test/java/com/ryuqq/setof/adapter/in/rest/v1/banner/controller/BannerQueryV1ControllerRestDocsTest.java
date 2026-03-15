package com.ryuqq.setof.adapter.in.rest.v1.banner.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.banner.BannerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.banner.BannerV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.banner.dto.response.BannerSlideV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.banner.mapper.BannerV1ApiMapper;
import com.ryuqq.setof.application.banner.port.in.BannerSlideQueryUseCase;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
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
 * BannerQueryV1Controller REST Docs 테스트.
 *
 * <p>배너 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerQueryV1Controller REST Docs 테스트")
@WebMvcTest(BannerQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class BannerQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private BannerSlideQueryUseCase bannerSlideQueryUseCase;

    @MockBean private BannerV1ApiMapper mapper;

    @Nested
    @DisplayName("배너 슬라이드 목록 조회 API")
    class GetBannersTest {

        @Test
        @DisplayName("배너 타입으로 슬라이드 목록 조회 성공")
        void getBanners_Success() throws Exception {
            // given
            List<BannerSlide> slides = BannerApiFixtures.bannerSlideList();
            List<BannerSlideV1ApiResponse> response = BannerApiFixtures.bannerSlideResponseList();

            given(bannerSlideQueryUseCase.fetchDisplayBannerSlides(any())).willReturn(slides);
            given(mapper.toListResponse(slides)).willReturn(response);

            // when & then
            mockMvc.perform(get(BannerV1Endpoints.BANNERS).param("bannerType", "CATEGORY"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].bannerItemId").value(1))
                    .andExpect(jsonPath("$.data[0].title").value("신규 회원 이벤트"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("bannerType")
                                                    .description(
                                                            "배너 타입 (CATEGORY, MY_PAGE, CART,"
                                                                    + " PRODUCT_DETAIL_DESCRIPTION,"
                                                                    + " RECOMMEND, LOGIN)")),
                                    responseFields(
                                            fieldWithPath("data[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("배너 슬라이드 목록"),
                                            fieldWithPath("data[].bannerItemId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배너 아이템 ID"),
                                            fieldWithPath("data[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 제목"),
                                            fieldWithPath("data[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL"),
                                            fieldWithPath("data[].linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("링크 URL"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("단일 슬라이드 조회 성공")
        void getBanners_SingleSlide() throws Exception {
            // given
            List<BannerSlide> slides = List.of(BannerApiFixtures.bannerSlide(1L));
            List<BannerSlideV1ApiResponse> response =
                    List.of(BannerApiFixtures.bannerSlideResponse(1L));

            given(bannerSlideQueryUseCase.fetchDisplayBannerSlides(any())).willReturn(slides);
            given(mapper.toListResponse(slides)).willReturn(response);

            // when & then
            mockMvc.perform(get(BannerV1Endpoints.BANNERS).param("bannerType", "RECOMMEND"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(
                            jsonPath("$.data[0].imageUrl")
                                    .value("https://cdn.example.com/banner/1.jpg"));
        }
    }
}
