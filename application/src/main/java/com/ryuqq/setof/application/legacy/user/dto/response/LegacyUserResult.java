package com.ryuqq.setof.application.legacy.user.dto.response;

import java.time.LocalDateTime;

/**
 * 레거시 사용자 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param userId 사용자 ID
 * @param name 이름
 * @param phoneNumber 전화번호
 * @param socialLoginType 소셜 로그인 타입
 * @param socialPkId 소셜 PK ID
 * @param gradeName 등급명
 * @param currentMileage 현재 마일리지
 * @param joinedDate 가입일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyUserResult(
        long userId,
        String name,
        String phoneNumber,
        String socialLoginType,
        String socialPkId,
        String gradeName,
        double currentMileage,
        LocalDateTime joinedDate) {

    public static LegacyUserResult of(
            long userId,
            String name,
            String phoneNumber,
            String socialLoginType,
            String socialPkId,
            String gradeName,
            double currentMileage,
            LocalDateTime joinedDate) {
        return new LegacyUserResult(
                userId,
                name,
                phoneNumber,
                socialLoginType,
                socialPkId,
                gradeName,
                currentMileage,
                joinedDate);
    }
}
