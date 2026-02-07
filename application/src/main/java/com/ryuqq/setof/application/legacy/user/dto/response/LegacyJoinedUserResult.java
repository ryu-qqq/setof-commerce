package com.ryuqq.setof.application.legacy.user.dto.response;

import java.time.LocalDateTime;

/**
 * 레거시 가입 사용자 조회 결과 DTO.
 *
 * <p>isExistUser 엔드포인트용 조회 결과.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param name 이름
 * @param userId 사용자 ID
 * @param socialLoginType 소셜 로그인 타입
 * @param phoneNumber 전화번호
 * @param socialPkId 소셜 PK ID
 * @param currentMileage 현재 마일리지
 * @param joinedDate 가입일
 * @param deleteYn 삭제 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyJoinedUserResult(
        String name,
        long userId,
        String socialLoginType,
        String phoneNumber,
        String socialPkId,
        double currentMileage,
        LocalDateTime joinedDate,
        String deleteYn) {

    public static LegacyJoinedUserResult of(
            String name,
            long userId,
            String socialLoginType,
            String phoneNumber,
            String socialPkId,
            double currentMileage,
            LocalDateTime joinedDate,
            String deleteYn) {
        return new LegacyJoinedUserResult(
                name,
                userId,
                socialLoginType,
                phoneNumber,
                socialPkId,
                currentMileage,
                joinedDate,
                deleteYn);
    }

    public boolean isDeleted() {
        return "Y".equals(deleteYn);
    }
}
