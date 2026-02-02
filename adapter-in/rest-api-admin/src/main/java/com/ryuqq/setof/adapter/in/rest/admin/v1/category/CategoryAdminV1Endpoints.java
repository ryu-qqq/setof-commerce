package com.ryuqq.setof.adapter.in.rest.admin.v1.category;

/**
 * CategoryAdminV1Endpoints - 카테고리 Admin V1 API 엔드포인트 상수.
 *
 * <p>레거시 호환을 위한 V1 엔드포인트 정의.
 *
 * <p>API-END-001: Endpoints final class
 *
 * <p>API-END-002: static final 상수
 *
 * <p>API-END-003: Path Variable 상수
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CategoryAdminV1Endpoints {

    private CategoryAdminV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 카테고리 조회 경로
    // ========================================================================

    /** 전체 카테고리 트리 조회 경로 */
    public static final String CATEGORY = BASE_V1 + "/category";

    /** 자식 카테고리 조회 경로 */
    public static final String CATEGORY_CHILDREN = BASE_V1 + "/category/{categoryId}";

    /** 부모 카테고리 조회 경로 */
    public static final String CATEGORY_PARENT = BASE_V1 + "/category/parent/{categoryId}";

    /** 여러 카테고리의 부모 조회 경로 */
    public static final String CATEGORY_PARENTS = BASE_V1 + "/category/parents";

    /** 카테고리 페이징 조회 경로 */
    public static final String CATEGORY_PAGE = BASE_V1 + "/category/page";

    // ========================================================================
    // 카테고리 매핑 경로
    // ========================================================================

    /** 외부 카테고리 매핑 경로 */
    public static final String CATEGORY_EXTERNAL_MAPPING =
            BASE_V1 + "/category/external/{siteId}/mapping";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Category ID Path Variable 이름 */
    public static final String PATH_CATEGORY_ID = "categoryId";

    /** Site ID Path Variable 이름 */
    public static final String PATH_SITE_ID = "siteId";
}
