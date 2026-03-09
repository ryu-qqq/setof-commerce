package com.ryuqq.setof.application.member.port.in;

import com.ryuqq.setof.application.member.dto.query.IsExistUserResult;

/**
 * 회원 존재 여부 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface IsExistUserUseCase {

    /**
     * 전화번호로 회원 존재 여부를 확인합니다.
     *
     * @param phoneNumber 전화번호
     * @return 존재 여부 결과
     */
    IsExistUserResult execute(String phoneNumber);
}
