package com.ryuqq.setof.application.member.dto.query;

/**
 * 회원 존재 여부 조회 결과 (isExistUser API용).
 *
 * @param joined 가입 여부
 * @param userId 레거시 user_id (가입된 경우)
 * @param name 이름 (가입된 경우)
 * @author ryu-qqq
 * @since 1.2.0
 */
public record IsExistUserResult(boolean joined, Long userId, String name) {

    public static IsExistUserResult notJoined() {
        return new IsExistUserResult(false, null, null);
    }

    public static IsExistUserResult joined(long userId, String name) {
        return new IsExistUserResult(true, userId, name);
    }
}
