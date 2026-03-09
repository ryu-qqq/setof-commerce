package com.ryuqq.setof.application.member.dto.command;

/**
 * 회원 가입 Command.
 *
 * @param phoneNumber 전화번호
 * @param password 비밀번호 (평문)
 * @param name 이름
 * @param socialLoginType 소셜 로그인 타입 (EMAIL, KAKAO 등)
 * @param socialPkId 소셜 PK ID
 * @param privacyConsent 개인정보 동의
 * @param serviceTermsConsent 서비스 이용약관 동의
 * @param adConsent 광고 동의
 * @author ryu-qqq
 * @since 1.2.0
 */
public record JoinCommand(
        String phoneNumber,
        String password,
        String name,
        String socialLoginType,
        String socialPkId,
        boolean privacyConsent,
        boolean serviceTermsConsent,
        boolean adConsent) {

    public JoinCommand {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("phoneNumber must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }
}
