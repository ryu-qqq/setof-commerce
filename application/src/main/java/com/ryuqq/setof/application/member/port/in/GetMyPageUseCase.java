package com.ryuqq.setof.application.member.port.in;

import com.ryuqq.setof.application.member.dto.query.MyPageResult;

/**
 * 마이페이지 조회 UseCase.
 *
 * <p>회원 프로필과 주문 상태별 건수를 함께 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface GetMyPageUseCase {

    /**
     * 마이페이지를 조회합니다.
     *
     * @param userId 레거시 user_id
     * @return 마이페이지 결과 (프로필 + 주문 건수)
     */
    MyPageResult execute(long userId);
}
