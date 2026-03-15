package com.ryuqq.setof.application.member.port.out.command;

import com.ryuqq.setof.domain.member.aggregate.Member;

/**
 * 회원 엔티티 Command Port.
 *
 * <p>회원 엔티티(members 테이블)의 저장만 담당합니다. 인증 수단은 MemberAuthCommandPort, 동의 정보는
 * MemberConsentCommandPort에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface MemberCommandPort {

    /**
     * 회원 저장 (신규 INSERT / 기존 UPDATE).
     *
     * <p>member.idValue()가 null이면 INSERT 후 생성된 PK를 반환합니다. null이 아니면 UPDATE(더티체킹) 후 기존 PK를 반환합니다.
     *
     * @param member 회원 도메인 객체
     * @return 저장된 회원의 PK (auto-increment)
     */
    Long persist(Member member);
}
