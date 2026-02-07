package com.ryuqq.setof.application.legacy.user.dto.response;

import java.time.LocalDateTime;

/**
 * 레거시 마이페이지 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param name 이름
 * @param phoneNumber 전화번호
 * @param email 이메일
 * @param socialLoginType 소셜 로그인 타입
 * @param registrationDate 가입일
 * @param userGrade 사용자 등급
 * @param currentMileage 현재 마일리지
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyMyPageResult(
        String name,
        String phoneNumber,
        String email,
        String socialLoginType,
        LocalDateTime registrationDate,
        String userGrade,
        double currentMileage) {

    public static LegacyMyPageResult of(
            String name,
            String phoneNumber,
            String email,
            String socialLoginType,
            LocalDateTime registrationDate,
            String userGrade,
            double currentMileage) {
        return new LegacyMyPageResult(
                name,
                phoneNumber,
                email,
                socialLoginType,
                registrationDate,
                userGrade,
                currentMileage);
    }
}
