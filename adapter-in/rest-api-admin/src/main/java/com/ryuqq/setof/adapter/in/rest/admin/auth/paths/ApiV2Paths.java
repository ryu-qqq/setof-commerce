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

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String REGISTER = BASE;
        public static final String UPDATE = BASE + ID_PATH;

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
    // Health Check
    // =====================================================

    /** Health 체크 경로 */
    public static final class Health {
        public static final String BASE = API_V2_ADMIN + "/health";

        private Health() {}
    }
}
