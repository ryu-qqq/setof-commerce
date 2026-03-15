package com.ryuqq.setof.application.member.port.out.query;

import com.ryuqq.setof.domain.member.aggregate.MemberAuth;

/**
 * 회원 인증 수단 Query Port.
 *
 * <p>인증 수단 조회를 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface MemberAuthQueryPort {

    /**
     * 회원 ID로 전화번호 인증 수단을 조회합니다.
     *
     * @param memberId 회원 PK
     * @return 전화번호 인증 수단 도메인 객체
     * @throws com.ryuqq.setof.domain.member.exception.MemberNotFoundException 인증 수단이 존재하지 않는 경우
     */
    MemberAuth findPhoneAuthByMemberId(Long memberId);
}
