package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.CreateGnbV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.UpdateGnbV2ApiRequest;
import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * GNB Admin 통합 테스트용 Fixture
 *
 * @author development-team
 * @since 2.0.0
 */
public final class GnbAdminTestFixture {

    private GnbAdminTestFixture() {}

    // ===== IDs =====
    public static final Long GNB_ID_1 = 1L;
    public static final Long GNB_ID_2 = 2L;
    public static final Long GNB_ID_3 = 3L;
    public static final Long NON_EXISTENT_GNB_ID = 99999L;

    // ===== GNB 속성 =====
    public static final String GNB_TITLE_HOME = "홈";
    public static final String GNB_TITLE_BEST = "베스트";
    public static final String GNB_TITLE_NEW = "신상품";
    public static final String GNB_LINK_HOME = "/home";
    public static final String GNB_LINK_BEST = "/best";
    public static final String GNB_LINK_NEW = "/new";
    public static final int DISPLAY_ORDER_1 = 1;
    public static final int DISPLAY_ORDER_2 = 2;
    public static final int DISPLAY_ORDER_3 = 3;
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";

    // ===== Display Date =====
    public static final Instant DISPLAY_START_DATE = Instant.now().minus(1, ChronoUnit.DAYS);
    public static final Instant DISPLAY_END_DATE = Instant.now().plus(30, ChronoUnit.DAYS);

    /** GNB 생성 요청 생성 */
    public static CreateGnbV2ApiRequest createGnbRequest() {
        return new CreateGnbV2ApiRequest(
                GNB_TITLE_HOME,
                GNB_LINK_HOME,
                DISPLAY_ORDER_1,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE);
    }

    /** GNB 생성 요청 생성 (커스텀) */
    public static CreateGnbV2ApiRequest createGnbRequest(String title, String linkUrl, int order) {
        return new CreateGnbV2ApiRequest(
                title, linkUrl, order, DISPLAY_START_DATE, DISPLAY_END_DATE);
    }

    /** GNB 수정 요청 생성 */
    public static UpdateGnbV2ApiRequest updateGnbRequest() {
        return new UpdateGnbV2ApiRequest(
                GNB_TITLE_HOME + " (수정)",
                GNB_LINK_HOME,
                DISPLAY_ORDER_1,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE.plus(30, ChronoUnit.DAYS));
    }

    /** GNB 응답 생성 (Mock용) */
    public static GnbResponse createGnbResponse(Long gnbId) {
        return GnbResponse.of(
                gnbId,
                GNB_TITLE_HOME,
                GNB_LINK_HOME,
                DISPLAY_ORDER_1,
                STATUS_ACTIVE,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** GNB 응답 생성 (커스텀) */
    public static GnbResponse createGnbResponse(
            Long gnbId, String title, String linkUrl, int order, String status) {
        return GnbResponse.of(
                gnbId,
                title,
                linkUrl,
                order,
                status,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** GNB 응답 목록 생성 (Mock용) */
    public static List<GnbResponse> createGnbResponses() {
        return List.of(
                createGnbResponse(
                        GNB_ID_1, GNB_TITLE_HOME, GNB_LINK_HOME, DISPLAY_ORDER_1, STATUS_ACTIVE),
                createGnbResponse(
                        GNB_ID_2, GNB_TITLE_BEST, GNB_LINK_BEST, DISPLAY_ORDER_2, STATUS_ACTIVE),
                createGnbResponse(
                        GNB_ID_3, GNB_TITLE_NEW, GNB_LINK_NEW, DISPLAY_ORDER_3, STATUS_ACTIVE));
    }
}
