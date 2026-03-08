package com.ryuqq.setof.adapter.in.rest.v1.productgroup;

/**
 * ProductGroupV1Endpoints - 상품그룹 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 ProductController 경로 호환: 단수/복수 혼재 유지.
 *
 * <ul>
 *   <li>GET /api/v1/product/group/{productGroupId} (단수)
 *   <li>GET /api/v1/products/group (복수)
 *   <li>GET /api/v1/products/group/recent (복수)
 *   <li>GET /api/v1/product/group/brand/{brandId} (단수)
 *   <li>GET /api/v1/product/group/seller/{sellerId} (단수)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductGroupV1Endpoints {

    private ProductGroupV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    // ========================================================================
    // 단건 상세 조회: /api/v1/product/group/{productGroupId}
    // ========================================================================

    /** 단건 상세 조회 기본 경로 (단수 product) */
    public static final String PRODUCT_GROUP_DETAIL = BASE_V1 + "/product/group";

    /** 상품그룹 ID Path Variable 세그먼트 */
    public static final String PRODUCT_GROUP_ID_SEGMENT = "/{productGroupId}";

    /** 상품그룹 ID Path Variable 이름 */
    public static final String PATH_PRODUCT_GROUP_ID = "productGroupId";

    // ========================================================================
    // 목록/최근본상품 조회: /api/v1/products/group, /api/v1/products/group/recent
    // ========================================================================

    /** 목록 조회 기본 경로 (복수 products) */
    public static final String PRODUCT_GROUPS = BASE_V1 + "/products/group";

    /** 최근 본 상품(찜 목록) 경로 세그먼트 */
    public static final String RECENT_SEGMENT = "/recent";

    // ========================================================================
    // 브랜드별 조회: /api/v1/product/group/brand/{brandId}
    // ========================================================================

    /** 브랜드별 조회 기본 경로 (단수 product) */
    public static final String PRODUCT_GROUP_BRAND = BASE_V1 + "/product/group/brand";

    /** 브랜드 ID Path Variable 세그먼트 */
    public static final String BRAND_ID_SEGMENT = "/{brandId}";

    /** 브랜드 ID Path Variable 이름 */
    public static final String PATH_BRAND_ID = "brandId";

    // ========================================================================
    // 셀러별 조회: /api/v1/product/group/seller/{sellerId}
    // ========================================================================

    /** 셀러별 조회 기본 경로 (단수 product) */
    public static final String PRODUCT_GROUP_SELLER = BASE_V1 + "/product/group/seller";

    /** 셀러 ID Path Variable 세그먼트 */
    public static final String SELLER_ID_SEGMENT = "/{sellerId}";

    /** 셀러 ID Path Variable 이름 */
    public static final String PATH_SELLER_ID = "sellerId";
}
