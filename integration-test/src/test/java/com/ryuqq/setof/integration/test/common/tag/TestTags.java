package com.ryuqq.setof.integration.test.common.tag;

/**
 * 통합 테스트 태그 상수 정의
 *
 * <p>사용 예시:
 *
 * <pre>
 * {@code @Tag(TestTags.E2E)}
 * class MyE2ETest extends E2ETestBase { }
 *
 * {@code @Tag(TestTags.REPOSITORY)}
 * class MyRepositoryTest extends RepositoryTestBase { }
 * </pre>
 *
 * <p>Gradle 실행:
 *
 * <pre>
 * ./gradlew :integration-test:e2eTest         # E2E만
 * ./gradlew :integration-test:repositoryTest  # Repository만
 * ./gradlew :integration-test:fastTest        # E2E 제외
 * </pre>
 *
 * @see <a href="docs/INTEGRATION_TEST_GUIDE.md">Integration Test Guide</a>
 */
public final class TestTags {

    private TestTags() {
        throw new AssertionError("Cannot instantiate TestTags");
    }

    // ========================================
    // Primary Tags (주요 태그)
    // ========================================

    /**
     * Full Stack E2E 테스트
     *
     * <ul>
     *   <li>Bootstrap 애플리케이션 전체 컨텍스트 로드
     *   <li>REST API -> Application -> Domain -> Repository -> DB
     *   <li>실행: {@code ./gradlew :integration-test:e2eTest}
     * </ul>
     */
    public static final String E2E = "e2e";

    /**
     * Repository 통합 테스트
     *
     * <ul>
     *   <li>JPA/Repository 레이어만 테스트
     *   <li>DB 연동 검증
     *   <li>실행: {@code ./gradlew :integration-test:repositoryTest}
     * </ul>
     */
    public static final String REPOSITORY = "repository";

    // ========================================
    // Secondary Tags (세부 태그)
    // ========================================

    /**
     * 일반 사용자 API 테스트 (web-api)
     *
     * <p>E2E 태그와 함께 사용
     */
    public static final String API = "api";

    /**
     * 관리자 API 테스트 (web-api-admin)
     *
     * <p>E2E 태그와 함께 사용
     */
    public static final String ADMIN = "admin";

    /**
     * 느린 테스트 (fastTest에서 제외)
     *
     * <p>30초 이상 소요되는 테스트에 사용
     */
    public static final String SLOW = "slow";

    // ========================================
    // Domain Tags (도메인별 태그 - 선택적)
    // ========================================

    /** 인증 관련 테스트 */
    public static final String AUTH = "auth";

    /** 회원 관련 테스트 */
    public static final String MEMBER = "member";

    /** 판매자 관련 테스트 */
    public static final String SELLER = "seller";

    /** 상품 관련 테스트 */
    public static final String PRODUCT = "product";

    /** 주문 관련 테스트 */
    public static final String ORDER = "order";

    /** 결제 관련 테스트 */
    public static final String PAYMENT = "payment";

    /** 정책 관련 테스트 (배송, 환불 등) */
    public static final String POLICY = "policy";

    /** 공통 코드 관련 테스트 */
    public static final String COMMON_CODE = "common-code";

    /** 입점 신청 관련 테스트 */
    public static final String SELLER_APPLICATION = "seller-application";

    /** 배송 정책 관련 테스트 */
    public static final String SHIPPING_POLICY = "shipping-policy";

    /** 환불 정책 관련 테스트 */
    public static final String REFUND_POLICY = "refund-policy";

    /** 브랜드 관련 테스트 */
    public static final String BRAND = "brand";

    /** 카테고리 관련 테스트 */
    public static final String CATEGORY = "category";
}
