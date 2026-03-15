package com.ryuqq.setof.adapter.in.rest.admin.navigation;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchGnbsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.GnbV1ApiResponse;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * GNB Query v1 API 테스트 Fixtures.
 *
 * <p>GNB 조회 API 테스트에서 사용되는 Request/Response/Domain 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class GnbQueryV1ApiFixtures {

    private GnbQueryV1ApiFixtures() {}

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    // ===== 기본 상수 =====
    public static final Long DEFAULT_GNB_ID = 1L;
    public static final String DEFAULT_GNB_TITLE = "홈";
    public static final String DEFAULT_LINK_URL = "/";
    public static final int DEFAULT_DISPLAY_ORDER = 1;

    // UTC 기준 Instant
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.parse("2025-01-01T00:00:00Z");
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.parse("2099-12-31T14:59:59Z");
    public static final Instant DEFAULT_CREATED_AT = Instant.parse("2025-01-01T01:30:00Z");
    public static final Instant DEFAULT_UPDATED_AT = Instant.parse("2025-01-01T01:30:00Z");

    // KST LocalDateTime (Instant → KST 변환 결과)
    public static final LocalDateTime DEFAULT_DISPLAY_START_KST =
            LocalDateTime.of(2025, 1, 1, 9, 0, 0);
    public static final LocalDateTime DEFAULT_DISPLAY_END_KST =
            LocalDateTime.of(2099, 12, 31, 23, 59, 59);

    // ===== Search Request Fixtures =====

    public static SearchGnbsV1ApiRequest searchRequest() {
        return new SearchGnbsV1ApiRequest(null, null, null, null);
    }

    public static SearchGnbsV1ApiRequest searchRequestWithDateRange() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        return new SearchGnbsV1ApiRequest(start, end, null, null);
    }

    public static SearchGnbsV1ApiRequest searchRequestWithStartOnly() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 0, 0, 0);
        return new SearchGnbsV1ApiRequest(start, null, null, null);
    }

    // ===== Domain Object Fixtures =====

    public static NavigationMenu navigationMenu() {
        return navigationMenu(DEFAULT_GNB_ID);
    }

    public static NavigationMenu navigationMenu(Long id) {
        return navigationMenu(id, true);
    }

    public static NavigationMenu navigationMenu(Long id, boolean active) {
        DisplayPeriod period = DisplayPeriod.of(DEFAULT_DISPLAY_START_AT, DEFAULT_DISPLAY_END_AT);
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(id),
                DEFAULT_GNB_TITLE,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                period,
                active,
                DeletionStatus.active(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static NavigationMenu navigationMenu(
            Long id, String title, String linkUrl, int displayOrder) {
        DisplayPeriod period = DisplayPeriod.of(DEFAULT_DISPLAY_START_AT, DEFAULT_DISPLAY_END_AT);
        return NavigationMenu.reconstitute(
                NavigationMenuId.of(id),
                title,
                linkUrl,
                displayOrder,
                period,
                true,
                DeletionStatus.active(),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    public static List<NavigationMenu> multipleNavigationMenus() {
        return List.of(
                navigationMenu(1L, "홈", "/", 1),
                navigationMenu(2L, "남성", "/men", 2),
                navigationMenu(3L, "여성", "/women", 3));
    }

    // ===== API Response Fixtures =====

    public static GnbV1ApiResponse gnbResponse() {
        return gnbResponse(DEFAULT_GNB_ID);
    }

    public static GnbV1ApiResponse gnbResponse(Long id) {
        GnbV1ApiResponse.DisplayPeriodResponse displayPeriod =
                GnbV1ApiResponse.DisplayPeriodResponse.of(
                        DEFAULT_DISPLAY_START_KST, DEFAULT_DISPLAY_END_KST);
        GnbV1ApiResponse.GnbDetailsResponse details =
                GnbV1ApiResponse.GnbDetailsResponse.of(
                        DEFAULT_GNB_TITLE,
                        DEFAULT_LINK_URL,
                        DEFAULT_DISPLAY_ORDER,
                        displayPeriod,
                        "Y");
        return GnbV1ApiResponse.of(id, details);
    }

    public static List<GnbV1ApiResponse> gnbResponseList() {
        return List.of(gnbResponse(1L), gnbResponse(2L), gnbResponse(3L));
    }

    // ===== 헬퍼 메서드 =====

    public static Instant toKstInstant(LocalDateTime ldt) {
        return ldt.atZone(KST).toInstant();
    }

    public static LocalDateTime toKstLocalDateTime(Instant instant) {
        return instant.atZone(KST).toLocalDateTime();
    }
}
