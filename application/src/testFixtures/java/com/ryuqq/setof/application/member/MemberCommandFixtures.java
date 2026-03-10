package com.ryuqq.setof.application.member;

import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.SocialIntegrationContext;
import com.ryuqq.setof.application.member.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.member.dto.command.UpdatePasswordContext;
import com.ryuqq.setof.application.member.dto.command.WithdrawalCommand;

/**
 * Member Application Command 테스트 Fixtures.
 *
 * <p>Member 등록/수정/탈퇴 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public final class MemberCommandFixtures {

    private MemberCommandFixtures() {}

    // ===== 상수 =====
    public static final long DEFAULT_USER_ID = 1001L;
    public static final String DEFAULT_PHONE_NUMBER = "01012345678";
    public static final String DEFAULT_NAME = "홍길동";
    public static final String DEFAULT_PASSWORD = "password123!";
    public static final String DEFAULT_ENCODED_PASSWORD = "$2a$10$hashedPasswordValue";
    public static final String DEFAULT_SOCIAL_PK_ID = "kakao_123456789";
    public static final String DEFAULT_WITHDRAWAL_REASON = "서비스 이용 불편";

    // ===== JoinCommand =====

    public static JoinCommand joinCommand() {
        return new JoinCommand(
                DEFAULT_PHONE_NUMBER,
                DEFAULT_PASSWORD,
                DEFAULT_NAME,
                "EMAIL",
                null,
                true,
                true,
                false);
    }

    public static JoinCommand joinCommandWithSocial() {
        return new JoinCommand(
                DEFAULT_PHONE_NUMBER,
                DEFAULT_PASSWORD,
                DEFAULT_NAME,
                "KAKAO",
                DEFAULT_SOCIAL_PK_ID,
                true,
                true,
                true);
    }

    public static JoinCommand joinCommand(String phoneNumber, String name) {
        return new JoinCommand(
                phoneNumber, DEFAULT_PASSWORD, name, "EMAIL", null, true, true, false);
    }

    // ===== KakaoLoginCommand =====

    public static KakaoLoginCommand kakaoLoginCommand() {
        return new KakaoLoginCommand(
                DEFAULT_PHONE_NUMBER,
                DEFAULT_NAME,
                "test@example.com",
                DEFAULT_SOCIAL_PK_ID,
                "M",
                "1990-01-01",
                false);
    }

    public static KakaoLoginCommand kakaoLoginCommandWithIntegration() {
        return new KakaoLoginCommand(
                DEFAULT_PHONE_NUMBER,
                DEFAULT_NAME,
                "test@example.com",
                DEFAULT_SOCIAL_PK_ID,
                "M",
                "1990-01-01",
                true);
    }

    public static KakaoLoginCommand kakaoLoginCommand(String phoneNumber, boolean integration) {
        return new KakaoLoginCommand(
                phoneNumber,
                DEFAULT_NAME,
                "test@example.com",
                DEFAULT_SOCIAL_PK_ID,
                "M",
                "1990-01-01",
                integration);
    }

    // ===== ResetPasswordCommand =====

    public static ResetPasswordCommand resetPasswordCommand() {
        return new ResetPasswordCommand(DEFAULT_PHONE_NUMBER, "newPassword123!");
    }

    public static ResetPasswordCommand resetPasswordCommand(
            String phoneNumber, String newPassword) {
        return new ResetPasswordCommand(phoneNumber, newPassword);
    }

    // ===== WithdrawalCommand =====

    public static WithdrawalCommand withdrawalCommand() {
        return new WithdrawalCommand(DEFAULT_USER_ID, DEFAULT_WITHDRAWAL_REASON);
    }

    public static WithdrawalCommand withdrawalCommand(long userId, String reason) {
        return new WithdrawalCommand(userId, reason);
    }

    // ===== MemberRegistrationInfo =====

    public static MemberRegistrationInfo memberRegistrationInfo() {
        return new MemberRegistrationInfo(
                DEFAULT_ENCODED_PASSWORD, "EMAIL", null, true, true, false);
    }

    public static MemberRegistrationInfo kakaoMemberRegistrationInfo() {
        return new MemberRegistrationInfo("", "KAKAO", DEFAULT_SOCIAL_PK_ID, true, true, true);
    }

    // ===== StatusChangeContext =====

    public static StatusChangeContext statusChangeContext() {
        return new StatusChangeContext(DEFAULT_USER_ID, DEFAULT_WITHDRAWAL_REASON);
    }

    public static StatusChangeContext statusChangeContext(long userId, String reason) {
        return new StatusChangeContext(userId, reason);
    }

    // ===== UpdatePasswordContext =====

    public static UpdatePasswordContext updatePasswordContext() {
        return new UpdatePasswordContext(DEFAULT_USER_ID, DEFAULT_ENCODED_PASSWORD);
    }

    public static UpdatePasswordContext updatePasswordContext(long userId, String encodedPassword) {
        return new UpdatePasswordContext(userId, encodedPassword);
    }

    // ===== SocialIntegrationContext =====

    public static SocialIntegrationContext socialIntegrationContext() {
        return new SocialIntegrationContext(
                DEFAULT_USER_ID, "KAKAO", DEFAULT_SOCIAL_PK_ID, "M", "1990-01-01");
    }

    public static SocialIntegrationContext socialIntegrationContext(long userId) {
        return new SocialIntegrationContext(
                userId, "KAKAO", DEFAULT_SOCIAL_PK_ID, "M", "1990-01-01");
    }
}
