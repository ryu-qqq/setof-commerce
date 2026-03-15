package com.ryuqq.setof.application.member.port.out.query;

import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.Optional;

/**
 * 회원 조회 Port.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface MemberQueryPort {

    Optional<Member> findById(Long memberId);

    Optional<Member> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<MemberWithCredentials> findWithCredentialsByPhoneNumber(String phoneNumber);

    Optional<MemberLoginInfo> findLoginInfoById(Long memberId);
}
