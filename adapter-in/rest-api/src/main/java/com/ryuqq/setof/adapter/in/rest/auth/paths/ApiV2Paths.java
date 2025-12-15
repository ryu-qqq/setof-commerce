package com.ryuqq.setof.adapter.in.rest.auth.paths;

/**
 * API V2 경로 상수 정의
 *
 * <p>신규 REST API 엔드포인트 경로를 상수로 관리합니다.
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
 * <p>버전 분리 정책:
 *
 * <ul>
 *   <li>V2: 신규 구현 (현재 파일)
 *   <li>V1: 레거시 호환 (별도 ApiV1Paths 생성 예정)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ApiV2Paths {

    public static final String API_V2 = "/api/v2";

    private ApiV2Paths() {
        // 인스턴스화 방지
    }

    /** Auth 도메인 경로 */
    public static final class Auth {
        public static final String BASE = API_V2 + "/auth";

        // ===== 상대 경로 (Controller용) =====
        public static final String LOGIN_PATH = "/login";
        public static final String LOGOUT_PATH = "/logout";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LOGIN = BASE + LOGIN_PATH;
        public static final String LOGOUT = BASE + LOGOUT_PATH;

        private Auth() {}
    }

    /** Member 도메인 경로 */
    public static final class Members {
        public static final String BASE = API_V2 + "/members";

        // ===== 상대 경로 (Controller용) =====
        public static final String ME_PATH = "/me";
        public static final String ID_PATH = "/{memberId}";
        public static final String WITHDRAW_PATH = "/withdraw";
        public static final String PASSWORD_RESET_PATH = "/passwordHash/reset";
        public static final String KAKAO_LINK_PATH = "/kakao-link";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String REGISTER = BASE;
        public static final String ME = BASE + ME_PATH;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String WITHDRAW = ME + WITHDRAW_PATH;
        public static final String PASSWORD_RESET = BASE + PASSWORD_RESET_PATH;
        public static final String KAKAO_LINK = ME + KAKAO_LINK_PATH;

        private Members() {}
    }

    /** Health 체크 경로 */
    public static final class Health {
        public static final String BASE = API_V2 + "/health";

        private Health() {}
    }

    /** Bank 도메인 경로 */
    public static final class Banks {
        public static final String BASE = API_V2 + "/banks";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;

        private Banks() {}
    }

    /** ShippingAddress 도메인 경로 (회원의 배송지) */
    public static final class ShippingAddresses {
        public static final String BASE = Members.ME + "/shipping-addresses";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{shippingAddressId}";
        public static final String DEFAULT_PATH = "/default";
        public static final String DELETE_PATH = "/delete";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String SET_DEFAULT = DETAIL + DEFAULT_PATH;
        public static final String DELETE = DETAIL + DELETE_PATH;

        private ShippingAddresses() {}
    }

    /** RefundAccount 도메인 경로 (회원의 환불계좌) */
    public static final class RefundAccount {
        public static final String BASE = Members.ME + "/refund-account";

        // ===== 상대 경로 (Controller용) =====
        public static final String DELETE_PATH = "/delete";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String GET = BASE;
        public static final String REGISTER = BASE;
        public static final String UPDATE = BASE;
        public static final String DELETE = BASE + DELETE_PATH;

        private RefundAccount() {}
    }

    /** Brand 도메인 경로 */
    public static final class Brands {
        public static final String BASE = API_V2 + "/brands";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{brandId}";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;

        private Brands() {}
    }

    /** Product 도메인 경로 (상품 조회) */
    public static final class Products {
        public static final String BASE = API_V2 + "/products";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{productGroupId}";
        public static final String FULL_PATH = "/{productGroupId}/full";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String FULL = BASE + FULL_PATH;

        private Products() {}
    }

    /** Category 도메인 경로 */
    public static final class Categories {
        public static final String BASE = API_V2 + "/categories";

        // ===== 상대 경로 (Controller용) =====
        public static final String ID_PATH = "/{categoryId}";
        public static final String TREE_PATH = "/tree";
        public static final String PATH_PATH = "/{categoryId}/path";

        // ===== 전체 경로 (SecurityPaths용) =====
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + ID_PATH;
        public static final String TREE = BASE + TREE_PATH;
        public static final String PATH = BASE + PATH_PATH;

        private Categories() {}
    }

    // ==================== Admin API 경로 ====================
    // Admin Seller API는 rest-api-admin 모듈로 이동됨

    public static final String API_V2_ADMIN = API_V2 + "/admin";

    /** Admin ShippingPolicy 도메인 경로 (셀러의 배송 정책) */
    public static final class AdminShippingPolicies {
        public static final String BASE = API_V2_ADMIN + "/sellers/{sellerId}/shipping-policies";

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

        private AdminShippingPolicies() {}
    }

    /** Admin RefundPolicy 도메인 경로 (셀러의 환불 정책) */
    public static final class AdminRefundPolicies {
        public static final String BASE = API_V2_ADMIN + "/sellers/{sellerId}/refund-policies";

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

        private AdminRefundPolicies() {}
    }
}
