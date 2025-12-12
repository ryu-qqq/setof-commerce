package com.ryuqq.setof.adapter.in.rest.admin.auth.paths;

/**
 * Admin API V1 경로 상수 정의 (Legacy)
 *
 * <p>어드민 전용 레거시 API V1 엔드포인트 경로를 상수로 관리합니다.
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
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Deprecated
public final class ApiPaths {

    public static final String API_V1 = "/api/v1";

    private ApiPaths() {
        // 인스턴스화 방지
    }

    // =====================================================
    // Brand BC (Brand Bounded Context) - Legacy V1
    // =====================================================

    /** Brand 도메인 경로 (Legacy V1) */
    @Deprecated
    public static final class Brand {
        public static final String BASE = API_V1 + "/brands";

        /** [GET] 브랜드 목록 조회 */
        public static final String LIST = BASE;

        private Brand() {}
    }

    // =====================================================
    // Category BC (Category Bounded Context) - Legacy V1
    // =====================================================

    /** Category 도메인 경로 (Legacy V1) */
    @Deprecated
    public static final class Category {
        public static final String BASE = API_V1 + "/category";

        /** [GET] 전체 카테고리 트리 조회 */
        public static final String TREE = BASE;

        /** [GET] 특정 카테고리의 하위 카테고리 조회 */
        public static final String CHILDREN = BASE + "/{categoryId}";

        private Category() {}
    }

    // =====================================================
    // Seller BC (Seller Bounded Context) - Legacy V1
    // =====================================================

    /** Seller 도메인 경로 (Legacy V1) */
    @Deprecated
    public static final class Seller {
        public static final String BASE = API_V1 + "/sellers";

        /** [GET] 셀러 목록 조회 */
        public static final String LIST = BASE;

        /** [GET] 셀러 상세 조회 */
        public static final String DETAIL = BASE + "/{sellerId}";

        private Seller() {}
    }
}
