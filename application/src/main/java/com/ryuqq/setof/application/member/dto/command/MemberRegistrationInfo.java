package com.ryuqq.setof.application.member.dto.command;

/**
 * 회원 가입 시 Member aggregate에 포함되지 않는 부가 정보.
 *
 * @param encodedPassword BCrypt 해시된 비밀번호
 * @param socialLoginType 소셜 로그인 타입 (EMAIL, KAKAO 등)
 * @param socialPkId 소셜 PK ID
 * @param privacyConsent 개인정보 동의
 * @param serviceTermsConsent 서비스 이용약관 동의
 * @param adConsent 광고 동의
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MemberRegistrationInfo(
        String encodedPassword,
        String socialLoginType,
        String socialPkId,
        boolean privacyConsent,
        boolean serviceTermsConsent,
        boolean adConsent) {}
