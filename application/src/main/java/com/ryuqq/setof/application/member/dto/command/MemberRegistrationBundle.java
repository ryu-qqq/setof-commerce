package com.ryuqq.setof.application.member.dto.command;

import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import com.ryuqq.setof.domain.member.id.MemberId;

/**
 * 회원 가입 시 필요한 도메인 객체를 하나로 묶는 번들.
 *
 * <p>Member, MemberAuth, MemberConsent 도메인 객체를 묶어 파사드에 전달합니다.
 *
 * @param member 회원 도메인 객체
 * @param memberAuth 인증 수단 도메인 객체
 * @param memberConsent 동의 정보 도메인 객체
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MemberRegistrationBundle(
        Member member, MemberAuth memberAuth, MemberConsent memberConsent) {

    /**
     * auto-increment로 생성된 회원 PK를 자식 Aggregate에 일괄 할당합니다.
     *
     * @param memberId 생성된 회원 PK
     */
    public void assignMemberId(Long memberId) {
        MemberId id = MemberId.of(memberId);
        memberAuth.assignMemberId(id);
        memberConsent.assignMemberId(id);
    }
}
