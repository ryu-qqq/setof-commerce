package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.navigation.GnbQueryV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchGnbsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.GnbV1ApiResponse;
import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * GnbQueryV1ApiMapper 단위 테스트.
 *
 * <p>v1 GNB Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("GnbQueryV1ApiMapper 단위 테스트")
class GnbQueryV1ApiMapperTest {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private GnbQueryV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GnbQueryV1ApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("null 날짜 요청을 NavigationMenuSearchParams로 변환한다")
        void toSearchParams_NullDates_Success() {
            // given
            SearchGnbsV1ApiRequest request = GnbQueryV1ApiFixtures.searchRequest();

            // when
            NavigationMenuSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.displayPeriodStart()).isNull();
            assertThat(params.displayPeriodEnd()).isNull();
        }

        @Test
        @DisplayName("날짜 범위 요청을 Instant로 변환한다")
        void toSearchParams_WithDateRange_Success() {
            // given
            SearchGnbsV1ApiRequest request = GnbQueryV1ApiFixtures.searchRequestWithDateRange();

            // when
            NavigationMenuSearchParams params = mapper.toSearchParams(request);

            // then
            LocalDateTime expectedStart = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
            LocalDateTime expectedEnd = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
            assertThat(params.displayPeriodStart())
                    .isEqualTo(expectedStart.atZone(KST).toInstant());
            assertThat(params.displayPeriodEnd()).isEqualTo(expectedEnd.atZone(KST).toInstant());
        }

        @Test
        @DisplayName("시작일만 있는 요청에서 종료일은 null로 변환한다")
        void toSearchParams_StartOnly_EndNull() {
            // given
            SearchGnbsV1ApiRequest request = GnbQueryV1ApiFixtures.searchRequestWithStartOnly();

            // when
            NavigationMenuSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.displayPeriodStart()).isNotNull();
            assertThat(params.displayPeriodEnd()).isNull();
        }
    }

    @Nested
    @DisplayName("toGnbResponse")
    class ToGnbResponseTest {

        @Test
        @DisplayName("NavigationMenu를 GnbV1ApiResponse로 변환한다")
        void toGnbResponse_Success() {
            // given
            NavigationMenu menu = GnbQueryV1ApiFixtures.navigationMenu();

            // when
            GnbV1ApiResponse response = mapper.toGnbResponse(menu);

            // then
            assertThat(response.gnbId()).isEqualTo(GnbQueryV1ApiFixtures.DEFAULT_GNB_ID);
            assertThat(response.gnbDetails()).isNotNull();
            assertThat(response.gnbDetails().title())
                    .isEqualTo(GnbQueryV1ApiFixtures.DEFAULT_GNB_TITLE);
            assertThat(response.gnbDetails().linkUrl())
                    .isEqualTo(GnbQueryV1ApiFixtures.DEFAULT_LINK_URL);
            assertThat(response.gnbDetails().displayOrder())
                    .isEqualTo(GnbQueryV1ApiFixtures.DEFAULT_DISPLAY_ORDER);
        }

        @Test
        @DisplayName("navigationMenuId를 gnbId로 변환한다")
        void toGnbResponse_NavigationMenuIdToGnbId() {
            // given
            NavigationMenu menu = GnbQueryV1ApiFixtures.navigationMenu(42L);

            // when
            GnbV1ApiResponse response = mapper.toGnbResponse(menu);

            // then
            assertThat(response.gnbId()).isEqualTo(42L);
        }

        @Test
        @DisplayName("active=true를 displayYn 'Y'로 변환한다")
        void toGnbResponse_ActiveTrue_DisplayYnY() {
            // given
            NavigationMenu menu = GnbQueryV1ApiFixtures.navigationMenu(1L, true);

            // when
            GnbV1ApiResponse response = mapper.toGnbResponse(menu);

            // then
            assertThat(response.gnbDetails().displayYn()).isEqualTo("Y");
        }

        @Test
        @DisplayName("active=false를 displayYn 'N'으로 변환한다")
        void toGnbResponse_ActiveFalse_DisplayYnN() {
            // given
            NavigationMenu menu = GnbQueryV1ApiFixtures.navigationMenu(1L, false);

            // when
            GnbV1ApiResponse response = mapper.toGnbResponse(menu);

            // then
            assertThat(response.gnbDetails().displayYn()).isEqualTo("N");
        }

        @Test
        @DisplayName("Instant(UTC)를 LocalDateTime(KST)으로 변환한다")
        void toGnbResponse_InstantToLocalDateTime() {
            // given
            NavigationMenu menu = GnbQueryV1ApiFixtures.navigationMenu();
            LocalDateTime expectedStart = GnbQueryV1ApiFixtures.DEFAULT_DISPLAY_START_KST;
            LocalDateTime expectedEnd = GnbQueryV1ApiFixtures.DEFAULT_DISPLAY_END_KST;

            // when
            GnbV1ApiResponse response = mapper.toGnbResponse(menu);

            // then
            assertThat(response.gnbDetails().displayPeriod().displayStartDate())
                    .isEqualTo(expectedStart);
            assertThat(response.gnbDetails().displayPeriod().displayEndDate())
                    .isEqualTo(expectedEnd);
        }

        @Test
        @DisplayName("커스텀 title, linkUrl, displayOrder를 올바르게 변환한다")
        void toGnbResponse_CustomValues() {
            // given
            NavigationMenu menu = GnbQueryV1ApiFixtures.navigationMenu(5L, "남성", "/men", 3);

            // when
            GnbV1ApiResponse response = mapper.toGnbResponse(menu);

            // then
            assertThat(response.gnbId()).isEqualTo(5L);
            assertThat(response.gnbDetails().title()).isEqualTo("남성");
            assertThat(response.gnbDetails().linkUrl()).isEqualTo("/men");
            assertThat(response.gnbDetails().displayOrder()).isEqualTo(3);
        }
    }
}
