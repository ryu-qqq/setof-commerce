package com.ryuqq.setof.adapter.in.rest.admin.v2.banner;

/**
 * BannerGroupAdminEndpoints - 배너 그룹 Admin API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class
 *
 * <p>API-END-002: static final 상수
 *
 * <p>API-END-003: Path Variable 상수
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerGroupAdminEndpoints {

    private BannerGroupAdminEndpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** 배너 그룹 기본 경로 */
    public static final String BANNER_GROUPS = "/api/v2/admin/banner-groups";

    /** Banner Group ID Path Variable */
    public static final String ID = "/{bannerGroupId}";

    /** Banner Group ID Path Variable 이름 */
    public static final String PATH_BANNER_GROUP_ID = "bannerGroupId";

    /** 노출 상태 변경 경로 */
    public static final String ACTIVE_STATUS = "/active-status";

    /** 소프트 삭제 경로 */
    public static final String REMOVE = "/remove";

    /** 슬라이드 수정 경로 */
    public static final String SLIDES = "/slides";
}
