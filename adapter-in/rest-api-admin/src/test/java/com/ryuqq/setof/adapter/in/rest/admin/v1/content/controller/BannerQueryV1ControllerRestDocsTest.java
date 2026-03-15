package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.banner.BannerQueryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerItemV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.BannerQueryV1ApiMapper;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupPageResult;
import com.ryuqq.setof.application.banner.port.in.query.GetBannerGroupDetailUseCase;
import com.ryuqq.setof.application.banner.port.in.query.SearchBannerGroupsUseCase;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
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
 * BannerQueryV1Controller REST Docs 테스트.
 *
 * <p>배너 조회 v1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerQueryV1Controller REST Docs 테스트")
@WebMvcTest(BannerQueryV1Controller.class)
@WithMockUser
class BannerQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchBannerGroupsUseCase searchBannerGroupsUseCase;

    @MockBean private GetBannerGroupDetailUseCase getBannerGroupDetailUseCase;

    @MockBean private BannerQueryV1ApiMapper queryMapper;

    private static final Long BANNER_ID = BannerQueryV1ApiFixtures.DEFAULT_BANNER_GROUP_ID;

    @Nested
    @DisplayName("배너 그룹 목록 조회 API")
    class GetBannersTest {

        @Test
        @DisplayName("배너 그룹 목록 조회 성공")
        void getBanners_Success() throws Exception {
            // given
            BannerGroupPageResult pageResult = BannerQueryV1ApiFixtures.bannerGroupPageResult();
            BannerGroupV1ApiResponse bannerResponse =
                    BannerQueryV1ApiFixtures.bannerGroupResponse();

            given(queryMapper.toSearchParams(any())).willReturn(null);
            given(searchBannerGroupsUseCase.execute(any())).willReturn(pageResult);
            given(queryMapper.toBannerGroupResponse(any())).willReturn(bannerResponse);

            // when & then
            mockMvc.perform(
                            get("/api/v1/content/banners")
                                    .param("bannerType", "CATEGORY")
                                    .param("displayYn", "Y")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("bannerType")
                                                    .description(
                                                            "배너 타입 +\n"
                                                                    + "CATEGORY, MY_PAGE, CART,"
                                                                    + " PRODUCT_DETAIL_DESCRIPTION,"
                                                                    + " RECOMMEND, LOGIN")
                                                    .optional(),
                                            parameterWithName("displayYn")
                                                    .description(
                                                            "전시 여부 +\nY: 전시, N: 미전시 +\n미입력 시 전체 조회")
                                                    .optional(),
                                            parameterWithName("lastDomainId")
                                                    .description("No-Offset 페이징용 마지막 배너 ID")
                                                    .optional(),
                                            parameterWithName("startDate")
                                                    .description(
                                                            "조회 시작일 (yyyy-MM-dd HH:mm:ss, KST)")
                                                    .optional(),
                                            parameterWithName("endDate")
                                                    .description(
                                                            "조회 종료일 (yyyy-MM-dd HH:mm:ss, KST)")
                                                    .optional(),
                                            parameterWithName("searchKeyword")
                                                    .description(
                                                            "검색 키워드 타입 +\n"
                                                                    + "BANNER_NAME: 배너명 검색 +\n"
                                                                    + "INSERT_OPERATOR,"
                                                                    + " UPDATE_OPERATOR는 무시됨")
                                                    .optional(),
                                            parameterWithName("searchWord")
                                                    .description(
                                                            "검색어 (searchKeyword가 BANNER_NAME일 때만"
                                                                    + " 적용)")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 [0 이상] +\n기본값: 0")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 [1~100] +\n기본값: 20")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 응답 데이터"),
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("배너 그룹 목록"),
                                            fieldWithPath("data.content[].bannerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배너 ID (bannerGroupId)"),
                                            fieldWithPath("data.content[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 그룹 제목"),
                                            fieldWithPath("data.content[].bannerType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 타입"),
                                            fieldWithPath("data.content[].displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간 정보"),
                                            fieldWithPath(
                                                            "data.content[].displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 시작일시 (KST)"),
                                            fieldWithPath(
                                                            "data.content[].displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 종료일시 (KST)"),
                                            fieldWithPath("data.content[].displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 여부 (Y/N)"),
                                            fieldWithPath("data.content[].insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시 (KST)"),
                                            fieldWithPath("data.content[].updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (KST)"),
                                            fieldWithPath("data.content[].insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자 (항상 빈 문자열)"),
                                            fieldWithPath("data.content[].updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자 (항상 빈 문자열)"),
                                            fieldWithPath("data.pageable")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 정보"),
                                            fieldWithPath("data.pageable.pageNumber")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                            fieldWithPath("data.pageable.pageSize")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.pageable.offset")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("오프셋"),
                                            fieldWithPath("data.pageable.paged")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("페이징 여부"),
                                            fieldWithPath("data.pageable.unpaged")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비페이징 여부"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 데이터 개수"),
                                            fieldWithPath("data.totalPages")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 페이지 수"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 페이지 여부"),
                                            fieldWithPath("data.numberOfElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 데이터 개수"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.number")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                            fieldWithPath("data.sort")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정렬 정보"),
                                            fieldWithPath("data.sort.unsorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비정렬 여부"),
                                            fieldWithPath("data.sort.sorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬 여부"),
                                            fieldWithPath("data.sort.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬 정보 없음"),
                                            fieldWithPath("data.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("빈 페이지 여부"),
                                            fieldWithPath("data.lastDomainId")
                                                    .type(JsonFieldType.NULL)
                                                    .description("No-Offset 페이징용 마지막 도메인 ID")
                                                    .optional(),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("빈 배너 목록 조회 성공")
        void getBanners_Empty_Success() throws Exception {
            // given
            BannerGroupPageResult emptyResult =
                    BannerQueryV1ApiFixtures.emptyBannerGroupPageResult();

            given(queryMapper.toSearchParams(any())).willReturn(null);
            given(searchBannerGroupsUseCase.execute(any())).willReturn(emptyResult);

            // when & then
            mockMvc.perform(get("/api/v1/content/banners").param("page", "0").param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isEmpty())
                    .andExpect(jsonPath("$.data.totalElements").value(0));
        }
    }

    @Nested
    @DisplayName("배너 아이템(슬라이드) 목록 조회 API")
    class GetBannerItemsTest {

        @Test
        @DisplayName("배너 아이템 목록 조회 성공")
        void getBannerItems_Success() throws Exception {
            // given
            BannerGroup group = BannerQueryV1ApiFixtures.bannerGroup(BANNER_ID);
            BannerItemV1ApiResponse itemResponse = BannerQueryV1ApiFixtures.bannerItemResponse();

            given(getBannerGroupDetailUseCase.execute(anyLong())).willReturn(group);
            given(queryMapper.toBannerItemResponse(any(), any())).willReturn(itemResponse);

            // when & then
            mockMvc.perform(get("/api/v1/content/banner/{bannerId}", BANNER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("bannerId").description("배너 그룹 ID")),
                                    responseFields(
                                            fieldWithPath("data[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("배너 아이템(슬라이드) 목록"),
                                            fieldWithPath("data[].bannerItemId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("배너 아이템 ID (bannerSlideId)"),
                                            fieldWithPath("data[].bannerType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배너 타입 (그룹에서 상속)"),
                                            fieldWithPath("data[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("슬라이드 제목"),
                                            fieldWithPath("data[].displayPeriod")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("전시 기간 정보"),
                                            fieldWithPath("data[].displayPeriod.displayStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 시작일시 (KST)"),
                                            fieldWithPath("data[].displayPeriod.displayEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 종료일시 (KST)"),
                                            fieldWithPath("data[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("이미지 URL"),
                                            fieldWithPath("data[].linkUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("링크 URL"),
                                            fieldWithPath("data[].displayOrder")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전시 순서"),
                                            fieldWithPath("data[].displayYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("전시 여부 (Y/N)"),
                                            fieldWithPath("data[].imageSize")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("이미지 크기 정보"),
                                            fieldWithPath("data[].imageSize.width")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("이미지 너비 (항상 0.0)"),
                                            fieldWithPath("data[].imageSize.height")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("이미지 높이 (항상 0.0)"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("슬라이드가 없는 배너 그룹 조회 성공")
        void getBannerItems_EmptySlides_Success() throws Exception {
            // given
            BannerGroup emptyGroup =
                    BannerQueryV1ApiFixtures.bannerGroupWithSlides(BANNER_ID, List.of());

            given(getBannerGroupDetailUseCase.execute(anyLong())).willReturn(emptyGroup);

            // when & then
            mockMvc.perform(get("/api/v1/content/banner/{bannerId}", BANNER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
