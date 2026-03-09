package com.ryuqq.setof.application.member.port.in;

import com.ryuqq.setof.application.member.dto.query.UserResult;

/**
 * 회원 프로필 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface GetUserUseCase {

    /**
     * 레거시 user_id로 회원 프로필을 조회합니다.
     *
     * @param userId 레거시 user_id
     * @return 회원 프로필 (등급, 마일리지 포함)
     */
    UserResult execute(long userId);
}
