package com.ryuqq.setof.application.member.dto.query;

/**
 * 회원 프로필 조회 결과 (fetchUser API용).
 *
 * @param userId 레거시 user_id
 * @param phoneNumber 전화번호
 * @param name 이름
 * @param email 이메일
 * @param gradeName 등급 이름
 * @param currentMileage 현재 마일리지
 * @param socialLoginType 소셜 로그인 타입
 * @author ryu-qqq
 * @since 1.2.0
 */
public record UserResult(
        long userId,
        String phoneNumber,
        String name,
        String email,
        String gradeName,
        double currentMileage,
        String socialLoginType) {}
