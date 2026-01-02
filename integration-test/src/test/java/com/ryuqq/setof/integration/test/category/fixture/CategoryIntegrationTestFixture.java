package com.ryuqq.setof.integration.test.category.fixture;

/**
 * Category Integration Test Fixture
 *
 * <p>카테고리 통합 테스트에서 사용하는 상수 및 예상값을 정의합니다.
 *
 * <p>테스트 데이터는 category-test-data.sql과 일치해야 합니다.
 *
 * @since 1.0.0
 */
public final class CategoryIntegrationTestFixture {

    private CategoryIntegrationTestFixture() {
        // Utility class
    }

    // ============================================================
    // Test Data Constants (category-test-data.sql과 일치)
    // ============================================================

    // 대분류 (depth = 0)
    public static final Long FASHION_ID = 1L;
    public static final String FASHION_CODE = "FASHION";
    public static final String FASHION_NAME_KO = "패션";
    public static final int FASHION_DEPTH = 0;

    public static final Long ELECTRONICS_ID = 2L;
    public static final String ELECTRONICS_CODE = "ELECTRONICS";
    public static final String ELECTRONICS_NAME_KO = "전자기기";

    // 중분류 (depth = 1)
    public static final Long FASHION_MEN_ID = 3L;
    public static final String FASHION_MEN_CODE = "FASHION_MEN";
    public static final String FASHION_MEN_NAME_KO = "남성의류";
    public static final int FASHION_MEN_DEPTH = 1;

    public static final Long FASHION_WOMEN_ID = 4L;
    public static final Long ELECTRONICS_MOBILE_ID = 5L;

    // 소분류 (depth = 2)
    public static final Long FASHION_MEN_TOP_ID = 6L;
    public static final String FASHION_MEN_TOP_NAME_KO = "상의";
    public static final int FASHION_MEN_TOP_DEPTH = 2;

    public static final Long FASHION_MEN_BOTTOM_ID = 7L;
    public static final Long FASHION_WOMEN_TOP_ID = 8L;

    public static final String ACTIVE_STATUS = "ACTIVE";
    public static final Long NON_EXISTENT_CATEGORY_ID = 9999L;

    // 카운트
    public static final int ROOT_CATEGORY_COUNT = 2; // 패션, 전자기기
    public static final int FASHION_CHILDREN_COUNT = 2; // 남성의류, 여성의류
    public static final int TOTAL_CATEGORIES = 8;

    // Path 검증 (Path Enumeration 형식: "parent_id,...,id")
    public static final String FASHION_MEN_TOP_PATH = "1,3,6";
    public static final int FASHION_MEN_TOP_PATH_LENGTH = 3; // 패션 > 남성의류 > 상의
}
