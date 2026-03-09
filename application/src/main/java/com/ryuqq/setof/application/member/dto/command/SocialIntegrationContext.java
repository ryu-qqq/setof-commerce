package com.ryuqq.setof.application.member.dto.command;

/**
 * 소셜 로그인 통합 Context.
 *
 * <p>기존 전화번호 회원에 소셜 로그인 정보를 통합할 때 사용됩니다.
 *
 * @param userId 레거시 user_id
 * @param socialLoginType 소셜 로그인 타입 (KAKAO 등)
 * @param socialPkId 소셜 PK ID
 * @param gender 성별
 * @param dateOfBirth 생년월일
 * @author ryu-qqq
 * @since 1.2.0
 */
public record SocialIntegrationContext(
        long userId,
        String socialLoginType,
        String socialPkId,
        String gender,
        String dateOfBirth) {}
