package com.ryuqq.setof.application.member.port.out.command;

import com.ryuqq.setof.domain.member.aggregate.MemberAuth;

/**
 * 회원 인증 수단 Command Port.
 *
 * <p>인증 수단(PHONE, KAKAO 등)의 저장을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface MemberAuthCommandPort {

    /**
     * 인증 수단 저장 (신규 INSERT / 기존 UPDATE).
     *
     * @param memberAuth 인증 수단 도메인 객체
     */
    void persist(MemberAuth memberAuth);
}
