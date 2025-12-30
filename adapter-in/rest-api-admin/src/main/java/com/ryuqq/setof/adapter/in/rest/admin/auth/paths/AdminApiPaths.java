package com.ryuqq.setof.adapter.in.rest.admin.auth.paths;

/**
 * Admin API 경로 상수 정의
 *
 * <p>모든 Admin REST API 엔드포인트 경로를 중앙 집중 관리합니다. Controller에서 @RequestMapping에 사용됩니다.
 *
 * <p>경로 구조:
 *
 * <ul>
 *   <li>/api/v1/admin/* - 모든 Admin API
 *   <li>관리 API: sellers, products, orders, categories (@PreAuthorize 권한 검사)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class AdminApiPaths {

    public static final String API_VERSION = "/api/v1";

    /** Admin 서비스 기본 경로 - Gateway 라우팅용 */
    public static final String ADMIN_SERVICE_BASE = API_VERSION + "/admin";

    private AdminApiPaths() {}

    /**
     * 셀러 관련 API 경로
     *
     * <p>셀러 관리 API입니다. @PreAuthorize로 권한 검사를 수행합니다.
     */
    public static final class Sellers {
        public static final String BASE = ADMIN_SERVICE_BASE + "/sellers";
        public static final String BY_ID = "/{sellerId}";

        private Sellers() {}
    }

    /**
     * 상품 관련 API 경로
     *
     * <p>상품 관리 API입니다. @PreAuthorize로 권한 검사를 수행합니다.
     */
    public static final class Products {
        public static final String BASE = ADMIN_SERVICE_BASE + "/products";
        public static final String BY_ID = "/{productId}";
        public static final String GROUPS = "/groups";
        public static final String GROUP_BY_ID = "/groups/{productGroupId}";

        private Products() {}
    }

    /**
     * 주문 관련 API 경로
     *
     * <p>주문 관리 API입니다. @PreAuthorize로 권한 검사를 수행합니다.
     */
    public static final class Orders {
        public static final String BASE = ADMIN_SERVICE_BASE + "/orders";
        public static final String BY_ID = "/{orderId}";

        private Orders() {}
    }

    /**
     * 카테고리 관련 API 경로
     *
     * <p>카테고리 관리 API입니다. @PreAuthorize로 권한 검사를 수행합니다.
     */
    public static final class Categories {
        public static final String BASE = ADMIN_SERVICE_BASE + "/categories";
        public static final String BY_ID = "/{categoryId}";

        private Categories() {}
    }

    /**
     * 브랜드 관련 API 경로
     *
     * <p>브랜드 관리 API입니다.
     */
    public static final class Brands {
        public static final String BASE = ADMIN_SERVICE_BASE + "/brands";
        public static final String BY_ID = "/{brandId}";

        private Brands() {}
    }

    /** 헬스체크 및 모니터링 API 경로 */
    public static final class Actuator {
        public static final String BASE = "/actuator";
        public static final String HEALTH = "/health";
        public static final String INFO = "/info";

        private Actuator() {}
    }

    /** OpenAPI (Swagger) 문서 경로 */
    public static final class OpenApi {
        public static final String DOCS = ADMIN_SERVICE_BASE + "/api-docs/**";
        public static final String SWAGGER_UI = ADMIN_SERVICE_BASE + "/swagger-ui/**";
        public static final String SWAGGER_UI_HTML = ADMIN_SERVICE_BASE + "/swagger-ui.html";
        public static final String SWAGGER_REDIRECT = ADMIN_SERVICE_BASE + "/swagger";

        private OpenApi() {}
    }

    /** REST Docs 문서 경로 */
    public static final class Docs {
        public static final String BASE = "/docs";
        public static final String ALL = "/docs/**";
        public static final String INDEX = "/docs/index.html";

        private Docs() {}
    }

    /** Health Check 경로 */
    public static final class Health {
        public static final String CHECK = API_VERSION + "/health";

        private Health() {}
    }
}
