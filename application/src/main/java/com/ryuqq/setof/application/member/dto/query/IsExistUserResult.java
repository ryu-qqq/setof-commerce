package com.ryuqq.setof.application.member.dto.query;

/**
 * 회원 존재 여부 조회 결과 (isExistUser API용).
 *
 * @param joined 가입 여부
 * @param userId 레거시 user_id (가입된 경우)
 * @param name 이름 (가입된 경우)
 * @param socialLoginType 소셜 로그인 타입
 * @param phoneNumber 전화번호
 * @param socialPkId 소셜 PK ID
 * @param currentMileage 현재 마일리지
 * @param joinedDate 가입일
 * @param deleteYn 삭제 여부
 * @author ryu-qqq
 * @since 1.2.0
 */
public record IsExistUserResult(
        boolean joined,
        Long userId,
        String name,
        String socialLoginType,
        String phoneNumber,
        String socialPkId,
        double currentMileage,
        String joinedDate,
        String deleteYn) {

    public static IsExistUserResult notJoined() {
        return new IsExistUserResult(false, null, null, null, null, null, 0, null, null);
    }

    public static IsExistUserResult joined(
            long userId,
            String name,
            String socialLoginType,
            String phoneNumber,
            String socialPkId,
            double currentMileage,
            String joinedDate,
            String deleteYn) {
        return new IsExistUserResult(
                true,
                userId,
                name,
                socialLoginType,
                phoneNumber,
                socialPkId,
                currentMileage,
                joinedDate,
                deleteYn);
    }
}
