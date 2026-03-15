package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberReadManager - 회원 조회 Manager.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class MemberReadManager {

    private final MemberQueryPort memberQueryPort;

    public MemberReadManager(MemberQueryPort memberQueryPort) {
        this.memberQueryPort = memberQueryPort;
    }

    @Transactional(readOnly = true)
    public Member getById(long memberId) {
        return memberQueryPort
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(memberId)));
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        return memberQueryPort.findByPhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return memberQueryPort.existsByPhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = true)
    public Optional<MemberWithCredentials> findWithCredentialsByPhoneNumber(String phoneNumber) {
        return memberQueryPort.findWithCredentialsByPhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = true)
    public MemberWithCredentials getWithCredentialsByPhoneNumber(String phoneNumber) {
        return memberQueryPort
                .findWithCredentialsByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MemberNotFoundException(phoneNumber));
    }

    @Transactional(readOnly = true)
    public MemberLoginInfo getLoginInfoById(long memberId) {
        return memberQueryPort
                .findLoginInfoById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(memberId)));
    }
}
