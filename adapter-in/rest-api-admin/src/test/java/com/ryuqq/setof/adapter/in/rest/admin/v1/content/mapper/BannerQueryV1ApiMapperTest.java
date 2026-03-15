package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.banner.BannerQueryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchBannersV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerItemV1ApiResponse;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupSearchParams;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BannerQueryV1ApiMapper 단위 테스트.
 *
 * <p>v1 배너 Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerQueryV1ApiMapper 단위 테스트")
class BannerQueryV1ApiMapperTest {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private BannerQueryV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BannerQueryV1ApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("기본 검색 요청을 BannerGroupSearchParams로 변환한다")
        void toSearchParams_Default_Success() {
            // given
            SearchBannersV1ApiRequest request = BannerQueryV1ApiFixtures.searchRequest();

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.bannerType()).isNull();
            assertThat(params.active()).isNull();
            assertThat(params.displayPeriodStart()).isNull();
            assertThat(params.displayPeriodEnd()).isNull();
            assertThat(params.titleKeyword()).isNull();
            assertThat(params.page()).isZero();
            assertThat(params.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("null 페이지 값을 기본값(0, 20)으로 처리한다")
        void toSearchParams_NullPage_UsesDefault() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithNullPage();

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isZero();
            assertThat(params.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("displayYn 'Y'를 active=true로 변환한다")
        void toSearchParams_DisplayYnY_ActiveTrue() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithDisplayYn("Y");

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isTrue();
        }

        @Test
        @DisplayName("displayYn 'N'을 active=false로 변환한다")
        void toSearchParams_DisplayYnN_ActiveFalse() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithDisplayYn("N");

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isFalse();
        }

        @Test
        @DisplayName("displayYn null을 active=null로 변환한다")
        void toSearchParams_DisplayYnNull_ActiveNull() {
            // given
            SearchBannersV1ApiRequest request = BannerQueryV1ApiFixtures.searchRequest();

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isNull();
        }

        @Test
        @DisplayName("searchKeyword가 BANNER_NAME일 때 searchWord를 titleKeyword로 변환한다")
        void toSearchParams_BannerName_TitleKeyword() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithBannerName("메인 배너");

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.titleKeyword()).isEqualTo("메인 배너");
        }

        @Test
        @DisplayName("searchKeyword가 INSERT_OPERATOR일 때 titleKeyword는 null이다")
        void toSearchParams_InsertOperator_TitleKeywordNull() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithOperator("INSERT_OPERATOR");

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.titleKeyword()).isNull();
        }

        @Test
        @DisplayName("searchKeyword가 UPDATE_OPERATOR일 때 titleKeyword는 null이다")
        void toSearchParams_UpdateOperator_TitleKeywordNull() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithOperator("UPDATE_OPERATOR");

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.titleKeyword()).isNull();
        }

        @Test
        @DisplayName("LocalDateTime(KST)을 Instant(UTC)로 변환한다")
        void toSearchParams_LocalDateTimeToInstant() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithDateRange();

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            LocalDateTime expectedStart = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
            LocalDateTime expectedEnd = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
            assertThat(params.displayPeriodStart())
                    .isEqualTo(expectedStart.atZone(KST).toInstant());
            assertThat(params.displayPeriodEnd()).isEqualTo(expectedEnd.atZone(KST).toInstant());
        }

        @Test
        @DisplayName("배너 타입 필터를 그대로 전달한다")
        void toSearchParams_WithBannerType() {
            // given
            SearchBannersV1ApiRequest request =
                    BannerQueryV1ApiFixtures.searchRequestWithBannerType("CATEGORY");

            // when
            BannerGroupSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.bannerType()).isEqualTo("CATEGORY");
        }
    }

    @Nested
    @DisplayName("toBannerGroupResponse")
    class ToBannerGroupResponseTest {

        @Test
        @DisplayName("BannerGroup을 BannerGroupV1ApiResponse로 변환한다")
        void toBannerGroupResponse_Success() {
            // given
            BannerGroup group = BannerQueryV1ApiFixtures.bannerGroup();

            // when
            BannerGroupV1ApiResponse response = mapper.toBannerGroupResponse(group);

            // then
            assertThat(response.bannerId())
                    .isEqualTo(BannerQueryV1ApiFixtures.DEFAULT_BANNER_GROUP_ID);
            assertThat(response.title()).isEqualTo(BannerQueryV1ApiFixtures.DEFAULT_BANNER_TITLE);
            assertThat(response.bannerType()).isEqualTo(BannerType.CATEGORY.name());
        }

        @Test
        @DisplayName("active=true를 displayYn 'Y'로 변환한다")
        void toBannerGroupResponse_ActiveTrue_DisplayYnY() {
            // given
            BannerGroup group = BannerQueryV1ApiFixtures.bannerGroup(1L, true);

            // when
            BannerGroupV1ApiResponse response = mapper.toBannerGroupResponse(group);

            // then
            assertThat(response.displayYn()).isEqualTo("Y");
        }

        @Test
        @DisplayName("active=false를 displayYn 'N'으로 변환한다")
        void toBannerGroupResponse_ActiveFalse_DisplayYnN() {
            // given
            BannerGroup group = BannerQueryV1ApiFixtures.bannerGroup(2L, false);

            // when
            BannerGroupV1ApiResponse response = mapper.toBannerGroupResponse(group);

            // then
            assertThat(response.displayYn()).isEqualTo("N");
        }

        @Test
        @DisplayName("Instant(UTC)를 LocalDateTime(KST)으로 변환한다")
        void toBannerGroupResponse_InstantToLocalDateTime() {
            // given
            BannerGroup group = BannerQueryV1ApiFixtures.bannerGroup();
            LocalDateTime expectedStart = BannerQueryV1ApiFixtures.DEFAULT_DISPLAY_START_KST;
            LocalDateTime expectedEnd = BannerQueryV1ApiFixtures.DEFAULT_DISPLAY_END_KST;

            // when
            BannerGroupV1ApiResponse response = mapper.toBannerGroupResponse(group);

            // then
            assertThat(response.displayPeriod().displayStartDate()).isEqualTo(expectedStart);
            assertThat(response.displayPeriod().displayEndDate()).isEqualTo(expectedEnd);
        }

        @Test
        @DisplayName("insertOperator, updateOperator는 빈 문자열로 반환한다")
        void toBannerGroupResponse_OperatorsEmpty() {
            // given
            BannerGroup group = BannerQueryV1ApiFixtures.bannerGroup();

            // when
            BannerGroupV1ApiResponse response = mapper.toBannerGroupResponse(group);

            // then
            assertThat(response.insertOperator()).isEmpty();
            assertThat(response.updateOperator()).isEmpty();
        }

        @Test
        @DisplayName("createdAt, updatedAt을 KST LocalDateTime으로 변환한다")
        void toBannerGroupResponse_CreatedAtUpdatedAt_KST() {
            // given
            BannerGroup group = BannerQueryV1ApiFixtures.bannerGroup();
            LocalDateTime expectedCreatedKst = BannerQueryV1ApiFixtures.DEFAULT_CREATED_KST;

            // when
            BannerGroupV1ApiResponse response = mapper.toBannerGroupResponse(group);

            // then
            assertThat(response.insertDate()).isEqualTo(expectedCreatedKst);
            assertThat(response.updateDate()).isEqualTo(expectedCreatedKst);
        }
    }

    @Nested
    @DisplayName("toBannerItemResponse")
    class ToBannerItemResponseTest {

        @Test
        @DisplayName("BannerSlide를 BannerItemV1ApiResponse로 변환한다")
        void toBannerItemResponse_Success() {
            // given
            BannerSlide slide =
                    BannerQueryV1ApiFixtures.bannerSlide(
                            BannerQueryV1ApiFixtures.DEFAULT_BANNER_SLIDE_ID);
            BannerType bannerType = BannerQueryV1ApiFixtures.DEFAULT_BANNER_TYPE;

            // when
            BannerItemV1ApiResponse response = mapper.toBannerItemResponse(slide, bannerType);

            // then
            assertThat(response.bannerItemId())
                    .isEqualTo(BannerQueryV1ApiFixtures.DEFAULT_BANNER_SLIDE_ID);
            assertThat(response.title()).isEqualTo(BannerQueryV1ApiFixtures.DEFAULT_SLIDE_TITLE);
            assertThat(response.bannerType()).isEqualTo(BannerType.CATEGORY.name());
            assertThat(response.imageUrl()).isEqualTo(BannerQueryV1ApiFixtures.DEFAULT_IMAGE_URL);
            assertThat(response.linkUrl()).isEqualTo(BannerQueryV1ApiFixtures.DEFAULT_LINK_URL);
            assertThat(response.displayOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("active=true 슬라이드를 displayYn 'Y'로 변환한다")
        void toBannerItemResponse_ActiveTrue_DisplayYnY() {
            // given
            BannerSlide slide = BannerQueryV1ApiFixtures.bannerSlide(10L, true);

            // when
            BannerItemV1ApiResponse response =
                    mapper.toBannerItemResponse(
                            slide, BannerQueryV1ApiFixtures.DEFAULT_BANNER_TYPE);

            // then
            assertThat(response.displayYn()).isEqualTo("Y");
        }

        @Test
        @DisplayName("active=false 슬라이드를 displayYn 'N'으로 변환한다")
        void toBannerItemResponse_ActiveFalse_DisplayYnN() {
            // given
            BannerSlide slide = BannerQueryV1ApiFixtures.bannerSlide(10L, false);

            // when
            BannerItemV1ApiResponse response =
                    mapper.toBannerItemResponse(
                            slide, BannerQueryV1ApiFixtures.DEFAULT_BANNER_TYPE);

            // then
            assertThat(response.displayYn()).isEqualTo("N");
        }

        @Test
        @DisplayName("imageSize는 width=0.0, height=0.0으로 반환한다")
        void toBannerItemResponse_ImageSizeZero() {
            // given
            BannerSlide slide = BannerQueryV1ApiFixtures.bannerSlide(10L);

            // when
            BannerItemV1ApiResponse response =
                    mapper.toBannerItemResponse(
                            slide, BannerQueryV1ApiFixtures.DEFAULT_BANNER_TYPE);

            // then
            assertThat(response.imageSize().width()).isZero();
            assertThat(response.imageSize().height()).isZero();
        }

        @Test
        @DisplayName("BannerGroup의 bannerType이 slide 응답에 반영된다")
        void toBannerItemResponse_BannerTypeFromGroup() {
            // given
            BannerSlide slide = BannerQueryV1ApiFixtures.bannerSlide(10L);
            BannerType bannerType = BannerType.MY_PAGE;

            // when
            BannerItemV1ApiResponse response = mapper.toBannerItemResponse(slide, bannerType);

            // then
            assertThat(response.bannerType()).isEqualTo("MY_PAGE");
        }

        @Test
        @DisplayName("Instant(UTC)를 LocalDateTime(KST)으로 변환한다")
        void toBannerItemResponse_InstantToLocalDateTime() {
            // given
            BannerSlide slide = BannerQueryV1ApiFixtures.bannerSlide(10L);
            LocalDateTime expectedStart = BannerQueryV1ApiFixtures.DEFAULT_DISPLAY_START_KST;
            LocalDateTime expectedEnd = BannerQueryV1ApiFixtures.DEFAULT_DISPLAY_END_KST;

            // when
            BannerItemV1ApiResponse response =
                    mapper.toBannerItemResponse(
                            slide, BannerQueryV1ApiFixtures.DEFAULT_BANNER_TYPE);

            // then
            assertThat(response.displayPeriod().displayStartDate()).isEqualTo(expectedStart);
            assertThat(response.displayPeriod().displayEndDate()).isEqualTo(expectedEnd);
        }
    }
}
