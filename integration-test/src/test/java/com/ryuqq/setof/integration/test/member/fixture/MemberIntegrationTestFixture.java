package com.ryuqq.setof.integration.test.member.fixture;

import java.time.LocalDate;

/**
 * Member Integration Test Fixture
 *
 * <p>회원 통합 테스트에서 사용하는 상수 및 예상값을 정의합니다.
 *
 * <p>테스트 데이터는 member-test-data.sql과 일치해야 합니다.
 *
 * @since 1.0.0
 */
public final class MemberIntegrationTestFixture {

    private MemberIntegrationTestFixture() {
        // Utility class
    }

    // ============================================================
    // Test Data Constants (member-test-data.sql과 일치)
    // ============================================================

    // Member 1: Active LOCAL member
    public static final String ACTIVE_LOCAL_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d01";
    public static final String ACTIVE_LOCAL_PHONE = "01012345678";
    public static final String ACTIVE_LOCAL_EMAIL = "active@example.com";
    public static final String ACTIVE_LOCAL_NAME = "활성회원";
    public static final String ACTIVE_LOCAL_PASSWORD = "Password1!";
    public static final LocalDate ACTIVE_LOCAL_BIRTH = LocalDate.of(1990, 1, 15);
    public static final String ACTIVE_LOCAL_GENDER = "M";
    public static final String ACTIVE_LOCAL_PROVIDER = "LOCAL";
    public static final String ACTIVE_STATUS = "ACTIVE";

    // Member 2: Active KAKAO member
    public static final String KAKAO_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d02";
    public static final String KAKAO_PHONE = "01087654321";
    public static final String KAKAO_EMAIL = "kakao@example.com";
    public static final String KAKAO_NAME = "카카오회원";
    public static final String KAKAO_SOCIAL_ID = "kakao_12345678";
    public static final String KAKAO_PROVIDER = "KAKAO";

    // Member 3: Withdrawn member
    public static final String WITHDRAWN_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d03";
    public static final String WITHDRAWN_PHONE = "01099998888";
    public static final String WITHDRAWN_EMAIL = "withdrawn@example.com";
    public static final String WITHDRAWN_NAME = "탈퇴회원";
    public static final String WITHDRAWN_STATUS = "WITHDRAWN";
    public static final String WITHDRAWN_REASON = "RARELY_USED";

    // Member 4: Inactive member
    public static final String INACTIVE_MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d04";
    public static final String INACTIVE_PHONE = "01044445555";
    public static final String INACTIVE_EMAIL = "inactive@example.com";
    public static final String INACTIVE_NAME = "휴면회원";
    public static final String INACTIVE_STATUS = "INACTIVE";

    // Test data for new registration
    public static final String NEW_MEMBER_PHONE = "01055556666";
    public static final String NEW_MEMBER_EMAIL = "newuser@example.com";
    public static final String NEW_MEMBER_PASSWORD = "Password1!";
    public static final String NEW_MEMBER_NAME = "신규회원";
    public static final LocalDate NEW_MEMBER_BIRTH = LocalDate.of(1990, 5, 15);
    public static final String NEW_MEMBER_GENDER = "M";

    // Invalid test data
    public static final String INVALID_PHONE_FORMAT = "0201234567";
    public static final String WEAK_PASSWORD = "password123";
    public static final String NON_EXISTENT_PHONE = "01000000000";

    // Counts
    public static final int TOTAL_MEMBERS = 4;
    public static final int ACTIVE_MEMBERS_COUNT = 2;
    public static final int LOCAL_MEMBERS_COUNT = 3;
    public static final int KAKAO_MEMBERS_COUNT = 1;
}
