package com.ryuqq.setof.integration.test.brand.fixture;

/**
 * Brand Integration Test Fixture
 *
 * <p>브랜드 통합 테스트에서 사용하는 상수 및 예상값을 정의합니다.
 *
 * <p>테스트 데이터는 brand-test-data.sql과 일치해야 합니다.
 *
 * @since 1.0.0
 */
public final class BrandIntegrationTestFixture {

    private BrandIntegrationTestFixture() {
        // Utility class
    }

    // ============================================================
    // Test Data Constants (brand-test-data.sql과 일치)
    // ============================================================

    public static final Long NIKE_ID = 1L;
    public static final String NIKE_CODE = "NIKE001";
    public static final String NIKE_NAME_KO = "나이키";
    public static final String NIKE_NAME_EN = "Nike";
    public static final String NIKE_LOGO_URL = "https://cdn.example.com/brands/nike.png";
    public static final String ACTIVE_STATUS = "ACTIVE";

    public static final Long ADIDAS_ID = 2L;
    public static final String ADIDAS_CODE = "ADIDAS001";
    public static final String ADIDAS_NAME_KO = "아디다스";

    public static final Long PUMA_ID = 3L;
    public static final Long NEW_BALANCE_ID = 4L;
    public static final Long REEBOK_ID = 5L;
    public static final String INACTIVE_STATUS = "INACTIVE";

    public static final Long NON_EXISTENT_BRAND_ID = 9999L;

    public static final int TOTAL_ACTIVE_BRANDS = 4;
    public static final int TOTAL_ALL_BRANDS = 5;

    // ============================================================
    // API Paths
    // ============================================================

    public static final String BRANDS_BASE_PATH = "/brands";
    public static final String BRAND_DETAIL_PATH = "/brands/{brandId}";

    // ============================================================
    // Search Parameters
    // ============================================================

    public static final String SEARCH_KEYWORD_NIKE = "나이키";
    public static final String SEARCH_KEYWORD_PARTIAL = "스"; // 아디다스, 뉴발란스 매칭
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
}
