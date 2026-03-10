package com.ryuqq.setof.application.member;

import com.ryuqq.setof.application.member.dto.query.IsExistUserResult;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.dto.query.MyPageResult;
import com.ryuqq.setof.application.member.dto.query.UserResult;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Member Application Query 테스트 Fixtures.
 *
 * <p>Member 조회 관련 Query 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public final class MemberQueryFixtures {

    private MemberQueryFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_GRADE_NAME = "NORMAL_GRADE";
    public static final double DEFAULT_MILEAGE = 1000.0;
    public static final String DEFAULT_SOCIAL_LOGIN_TYPE = "EMAIL";
    public static final String DEFAULT_SOCIAL_PK_ID = "kakao_123456789";

    // ===== MemberProfile =====

    public static MemberProfile memberProfile() {
        Member member = MemberFixtures.activeMigratedMember();
        return new MemberProfile(
                member, DEFAULT_GRADE_NAME, DEFAULT_MILEAGE, DEFAULT_SOCIAL_LOGIN_TYPE, null);
    }

    public static MemberProfile memberProfile(Member member) {
        return new MemberProfile(
                member, DEFAULT_GRADE_NAME, DEFAULT_MILEAGE, DEFAULT_SOCIAL_LOGIN_TYPE, null);
    }

    public static MemberProfile kakaoMemberProfile() {
        Member member = MemberFixtures.activeMigratedMember();
        return new MemberProfile(
                member, DEFAULT_GRADE_NAME, DEFAULT_MILEAGE, "KAKAO", DEFAULT_SOCIAL_PK_ID);
    }

    // ===== MemberWithCredentials =====

    public static MemberWithCredentials memberWithCredentials() {
        Member member = MemberFixtures.activeMigratedMember();
        return new MemberWithCredentials(
                member, "$2a$10$hashedPasswordValue", DEFAULT_SOCIAL_LOGIN_TYPE, null);
    }

    public static MemberWithCredentials kakaoMemberWithCredentials() {
        Member member = MemberFixtures.activeMigratedMember();
        return new MemberWithCredentials(member, "", "KAKAO", DEFAULT_SOCIAL_PK_ID);
    }

    public static MemberWithCredentials memberWithCredentials(
            Member member, String socialLoginType) {
        return new MemberWithCredentials(
                member, "$2a$10$hashedPasswordValue", socialLoginType, null);
    }

    // ===== UserResult =====

    public static UserResult userResult(long userId) {
        return new UserResult(
                userId,
                "01012345678",
                "홍길동",
                "test@example.com",
                DEFAULT_GRADE_NAME,
                DEFAULT_MILEAGE,
                DEFAULT_SOCIAL_LOGIN_TYPE,
                null,
                "2024-01-01T00:00",
                "N");
    }

    // ===== IsExistUserResult =====

    public static IsExistUserResult joinedResult(long userId) {
        return IsExistUserResult.joined(
                userId,
                "홍길동",
                DEFAULT_SOCIAL_LOGIN_TYPE,
                "01012345678",
                null,
                DEFAULT_MILEAGE,
                "2024-01-01T00:00",
                "N");
    }

    public static IsExistUserResult notJoinedResult() {
        return IsExistUserResult.notJoined();
    }

    // ===== MyPageResult =====

    public static MyPageResult myPageResult(long userId) {
        return new MyPageResult(
                userId,
                "01012345678",
                "홍길동",
                "test@example.com",
                DEFAULT_GRADE_NAME,
                DEFAULT_MILEAGE,
                DEFAULT_SOCIAL_LOGIN_TYPE,
                Instant.parse("2024-01-01T00:00:00Z"),
                orderStatusCountResults());
    }

    public static MyPageResult emptyOrderMyPageResult(long userId) {
        return new MyPageResult(
                userId,
                "01012345678",
                "홍길동",
                "test@example.com",
                DEFAULT_GRADE_NAME,
                DEFAULT_MILEAGE,
                DEFAULT_SOCIAL_LOGIN_TYPE,
                Instant.parse("2024-01-01T00:00:00Z"),
                Collections.emptyList());
    }

    // ===== OrderStatusCountResult =====

    public static List<OrderStatusCountResult> orderStatusCountResults() {
        return List.of(
                new OrderStatusCountResult("ORDER_PROCESSING", 2),
                new OrderStatusCountResult("ORDER_COMPLETED", 5),
                new OrderStatusCountResult("DELIVERY_PENDING", 1));
    }
}
