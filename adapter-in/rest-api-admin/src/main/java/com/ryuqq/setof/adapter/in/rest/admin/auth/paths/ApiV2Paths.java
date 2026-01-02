package com.ryuqq.setof.adapter.in.rest.admin.auth.paths;

/**
 * Admin API V2 경로 상수 정의
 *
 * <p>어드민 전용 REST API V2 엔드포인트 경로를 상수로 관리합니다.
 *
 * <p>경로 상수 구조:
 *
 * <ul>
 *   <li>*_PATH: 상대 경로 (Controller @PostMapping 등에서 사용)
 *   <li>전체 경로: SecurityPaths 등에서 전체 URL 매칭에 사용
 * </ul>
 *
 * <p>장점:
 *
 * <ul>
 *   <li>컴파일 타임 검증 - 오타 방지
 *   <li>IDE 자동완성/리팩토링 지원
 *   <li>Controller와 Security 경로 동기화 보장
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ApiV2Paths {

    public static final String API_V2 = "/api/v2";
    public static final String API_V2_ADMIN = API_V2 + "/admin";

    private ApiV2Paths() {
        // 인스턴스화 방지
    }

    // =====================================================
    // Auth 도메인 (인증)
    // =====================================================

    /** Auth 도메인 경로 */
    public static final class Auth {
        public static final String BASE = API_V2_ADMIN + "/auth";

        // ===== 상대 경로 (Controller용) =====
        public static final String LOGIN_PATH = "/login";
        public static final String LOGOUT_PATH = "/logout";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LOGIN = BASE + LOGIN_PATH;
        public static final String LOGOUT = BASE + LOGOUT_PATH;

        private Auth() {}
    }

    // =====================================================
    // Brand 도메인
    // =====================================================

    /** Brand 도메인 경로 */
    public static final class Brands {
        public static final String BASE = API_V2_ADMIN + "/brands";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{brandId}";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String REGISTER = BASE;
        public static final String UPDATE = BASE + ID_PATH;

        private Brands() {}
    }

    // =====================================================
    // Category 도메인
    // =====================================================

    /** Category 도메인 경로 */
    public static final class Categories {
        public static final String BASE = API_V2_ADMIN + "/categories";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{categoryId}";
        public static final String TREE_PATH = "/tree";
        public static final String PARENTS_PATH = "/{categoryId}/parents";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String TREE = BASE + TREE_PATH;
        public static final String PARENTS = BASE + PARENTS_PATH;
        public static final String REGISTER = BASE;
        public static final String UPDATE = BASE + ID_PATH;

        private Categories() {}
    }

    // =====================================================
    // Seller 도메인
    // =====================================================

    /** Seller 도메인 경로 */
    public static final class Sellers {
        public static final String BASE = API_V2_ADMIN + "/sellers";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{sellerId}";
        public static final String APPROVE_PATH = "/{sellerId}/approve";
        public static final String REJECT_PATH = "/{sellerId}/reject";
        public static final String SUSPEND_PATH = "/{sellerId}/suspend";
        public static final String DELETE_PATH = "/{sellerId}/delete";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String REGISTER = BASE;
        public static final String UPDATE = BASE + ID_PATH;
        public static final String APPROVE = BASE + APPROVE_PATH;
        public static final String REJECT = BASE + REJECT_PATH;
        public static final String SUSPEND = BASE + SUSPEND_PATH;
        public static final String DELETE = BASE + DELETE_PATH;

        private Sellers() {}
    }

    // =====================================================
    // ShippingPolicy 도메인 (셀러별 배송 정책)
    // =====================================================

    /** ShippingPolicy 도메인 경로 */
    public static final class ShippingPolicies {
        public static final String BASE = Sellers.BASE + "/{sellerId}/shipping-policies";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{shippingPolicyId}";
        public static final String DEFAULT_PATH = "/{shippingPolicyId}/default";
        public static final String DELETE_PATH = "/{shippingPolicyId}/delete";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String REGISTER = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String UPDATE = BASE + ID_PATH;
        public static final String SET_DEFAULT = BASE + DEFAULT_PATH;
        public static final String DELETE = BASE + DELETE_PATH;

        private ShippingPolicies() {}
    }

    // =====================================================
    // RefundPolicy 도메인 (셀러별 환불 정책)
    // =====================================================

    /** RefundPolicy 도메인 경로 */
    public static final class RefundPolicies {
        public static final String BASE = Sellers.BASE + "/{sellerId}/refund-policies";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{refundPolicyId}";
        public static final String DEFAULT_PATH = "/{refundPolicyId}/default";
        public static final String DELETE_PATH = "/{refundPolicyId}/delete";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String REGISTER = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String UPDATE = BASE + ID_PATH;
        public static final String SET_DEFAULT = BASE + DEFAULT_PATH;
        public static final String DELETE = BASE + DELETE_PATH;

        private RefundPolicies() {}
    }

    // =====================================================
    // Product 도메인
    // =====================================================

    /** Product 도메인 경로 */
    public static final class Products {
        public static final String BASE = API_V2_ADMIN + "/products";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{productId}";
        public static final String GROUP_PATH = "/groups";
        public static final String GROUP_ID_PATH = "/groups/{productGroupId}";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String GROUPS = BASE + GROUP_PATH;
        public static final String GROUP_DETAIL = BASE + GROUP_ID_PATH;
        public static final String REGISTER = BASE;
        public static final String UPDATE = BASE + ID_PATH;

        private Products() {}
    }

    // =====================================================
    // ProductGroup 도메인
    // =====================================================

    /** ProductGroup 도메인 경로 */
    public static final class ProductGroups {
        public static final String BASE = API_V2_ADMIN + "/product-groups";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{productGroupId}";
        public static final String STATUS_PATH = "/{productGroupId}/status";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String REGISTER = BASE;
        public static final String UPDATE = BASE + ID_PATH;
        public static final String UPDATE_STATUS = BASE + STATUS_PATH;

        private ProductGroups() {}
    }

    // =====================================================
    // ProductStock 도메인
    // =====================================================

    /** ProductStock 도메인 경로 */
    public static final class ProductStocks {
        // 단일 상품 재고 기본 경로
        public static final String BASE = Products.BASE;

        // ===== 상대 경로 (Controller용) =====
        public static final String SINGLE_STOCK_PATH = "/{productId}/stock";
        public static final String GROUP_STOCKS_PATH = "/groups/{productGroupId}/stocks";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String SINGLE_STOCK = BASE + SINGLE_STOCK_PATH;
        public static final String GROUP_STOCKS = BASE + GROUP_STOCKS_PATH;

        private ProductStocks() {}
    }

    // =====================================================
    // ProductDescription 도메인 (상품그룹 하위 리소스)
    // =====================================================

    /** ProductDescription 도메인 경로 */
    public static final class ProductDescriptions {
        public static final String BASE = ProductGroups.BASE + "/{productGroupId}/description";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String GET = BASE;
        public static final String UPDATE = BASE;

        private ProductDescriptions() {}
    }

    // =====================================================
    // ProductImage 도메인 (상품그룹 하위 리소스)
    // =====================================================

    /** ProductImage 도메인 경로 */
    public static final class ProductImages {
        public static final String BASE = ProductGroups.BASE + "/{productGroupId}/images";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{imageId}";
        public static final String DELETE_PATH = "/{imageId}/delete";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String UPDATE = BASE + ID_PATH;
        public static final String DELETE = BASE + DELETE_PATH;

        private ProductImages() {}
    }

    // =====================================================
    // ProductNotice 도메인 (상품그룹 하위 리소스)
    // =====================================================

    /** ProductNotice 도메인 경로 */
    public static final class ProductNotices {
        public static final String BASE = ProductGroups.BASE + "/{productGroupId}/notice";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String GET = BASE;
        public static final String UPDATE = BASE;

        private ProductNotices() {}
    }

    // =====================================================
    // Health Check
    // =====================================================

    /** Health 체크 경로 */
    public static final class Health {
        public static final String BASE = API_V2_ADMIN + "/health";

        private Health() {}
    }
}
