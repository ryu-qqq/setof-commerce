package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.command.CreateBannerItemV2ApiRequest;
import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * BannerItem Admin 통합 테스트용 Fixture
 *
 * @author development-team
 * @since 2.0.0
 */
public final class BannerItemAdminTestFixture {

    private BannerItemAdminTestFixture() {}

    // ===== Banner IDs =====
    public static final Long EXISTING_BANNER_ID = 1L;
    public static final Long NON_EXISTENT_BANNER_ID = 99999L;

    // ===== BannerItem IDs =====
    public static final Long BANNER_ITEM_ID_1 = 1L;
    public static final Long BANNER_ITEM_ID_2 = 2L;
    public static final Long BANNER_ITEM_ID_3 = 3L;

    // ===== BannerItem 속성 =====
    public static final String BANNER_ITEM_TITLE = "프로모션 배너";
    public static final String BANNER_ITEM_IMAGE_URL = "https://cdn.example.com/banner.jpg";
    public static final String BANNER_ITEM_LINK_URL = "https://example.com/promotion";
    public static final Integer BANNER_ITEM_DISPLAY_ORDER = 1;
    public static final String BANNER_ITEM_STATUS_ACTIVE = "ACTIVE";
    public static final String BANNER_ITEM_STATUS_INACTIVE = "INACTIVE";
    public static final Integer BANNER_ITEM_IMAGE_WIDTH = 1920;
    public static final Integer BANNER_ITEM_IMAGE_HEIGHT = 600;

    // ===== Display Date =====
    public static final Instant DISPLAY_START_DATE = Instant.now().minus(1, ChronoUnit.DAYS);
    public static final Instant DISPLAY_END_DATE = Instant.now().plus(30, ChronoUnit.DAYS);

    /** 배너 아이템 생성 요청 생성 */
    public static CreateBannerItemV2ApiRequest createBannerItemRequest() {
        return new CreateBannerItemV2ApiRequest(
                BANNER_ITEM_TITLE,
                BANNER_ITEM_IMAGE_URL,
                BANNER_ITEM_LINK_URL,
                BANNER_ITEM_DISPLAY_ORDER,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                BANNER_ITEM_IMAGE_WIDTH,
                BANNER_ITEM_IMAGE_HEIGHT);
    }

    /** 배너 아이템 생성 요청 생성 (커스텀) */
    public static CreateBannerItemV2ApiRequest createBannerItemRequest(
            String title, String imageUrl, Integer displayOrder) {
        return new CreateBannerItemV2ApiRequest(
                title,
                imageUrl,
                BANNER_ITEM_LINK_URL,
                displayOrder,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                BANNER_ITEM_IMAGE_WIDTH,
                BANNER_ITEM_IMAGE_HEIGHT);
    }

    /** 배너 아이템 일괄 생성 요청 목록 생성 */
    public static List<CreateBannerItemV2ApiRequest> createBannerItemBatchRequests() {
        return List.of(
                createBannerItemRequest("배너 아이템 1", "https://cdn.example.com/banner1.jpg", 1),
                createBannerItemRequest("배너 아이템 2", "https://cdn.example.com/banner2.jpg", 2),
                createBannerItemRequest("배너 아이템 3", "https://cdn.example.com/banner3.jpg", 3));
    }

    /** 배너 아이템 응답 생성 (Mock용) */
    public static BannerItemResponse createBannerItemResponse(Long bannerItemId, Long bannerId) {
        return new BannerItemResponse(
                bannerItemId,
                bannerId,
                BANNER_ITEM_TITLE,
                BANNER_ITEM_IMAGE_URL,
                BANNER_ITEM_LINK_URL,
                BANNER_ITEM_DISPLAY_ORDER,
                BANNER_ITEM_STATUS_ACTIVE,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                BANNER_ITEM_IMAGE_WIDTH,
                BANNER_ITEM_IMAGE_HEIGHT,
                Instant.now());
    }

    /** 배너 아이템 응답 목록 생성 (Mock용) */
    public static List<BannerItemResponse> createBannerItemResponses(Long bannerId) {
        return List.of(
                createBannerItemResponse(BANNER_ITEM_ID_1, bannerId),
                createBannerItemResponse(BANNER_ITEM_ID_2, bannerId),
                createBannerItemResponse(BANNER_ITEM_ID_3, bannerId));
    }
}
