package com.ryuqq.setof.adapter.in.rest.admin.v1.category;

/**
 * CategoryAdminV1Endpoints - 카테고리 Admin V1 API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 CategoryController 경로 호환:
 *
 * <ul>
 *   <li>GET /api/v1/category - 전체 트리
 *   <li>GET /api/v1/category/{categoryId} - 하위 카테고리
 *   <li>GET /api/v1/category/parent/{categoryId} - 상위 카테고리
 *   <li>GET /api/v1/category/parents - 다건 조회 (categoryIds)
 *   <li>GET /api/v1/category/page - 페이징 조회
 * </ul>
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
    // 카테고리 경로
    // ========================================================================

    /** 카테고리 기본 경로 */
    public static final String CATEGORY = BASE_V1 + "/category";

    /** 하위 카테고리 조회 (GET /api/v1/category/{categoryId}) */
    public static final String CATEGORY_ID = "/{categoryId}";

    /** 상위 카테고리 조회 (GET /api/v1/category/parent/{categoryId}) */
    public static final String PARENT = "/parent" + CATEGORY_ID;

    /** 다건 카테고리 조회 (GET /api/v1/category/parents) */
    public static final String PARENTS = "/parents";

    /** 카테고리 페이징 조회 (GET /api/v1/category/page) */
    public static final String PAGE = "/page";

    // ========================================================================
    // Path Variable 상수
    // ========================================================================

    /** Category ID Path Variable 이름 */
    public static final String PATH_CATEGORY_ID = "categoryId";
}
