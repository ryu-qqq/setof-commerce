package com.ryuqq.setof.application.member.port.out.command;

import com.ryuqq.setof.domain.member.aggregate.MemberConsent;

/**
 * 회원 동의 정보 Command Port.
 *
 * <p>개인정보, 서비스 약관, 광고 동의 저장을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface MemberConsentCommandPort {

    /**
     * 동의 정보 저장 (신규 INSERT / 기존 UPDATE).
     *
     * @param memberConsent 동의 정보 도메인 객체
     */
    void persist(MemberConsent memberConsent);
}
