package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.dto.query.MemberProfile;
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
 * <p>MemberQueryPort를 래핑하며 트랜잭션 경계를 설정합니다.
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

    /**
     * 레거시 사용자 ID로 회원 조회.
     *
     * @param userId 레거시 user_id
     * @return 회원 도메인 객체
     * @throws MemberNotFoundException 회원이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public Member getByLegacyId(long userId) {
        return memberQueryPort
                .findByLegacyId(userId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(userId)));
    }

    /**
     * 전화번호로 회원 조회.
     *
     * @param phoneNumber 전화번호
     * @return 회원 도메인 객체 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        return memberQueryPort.findByPhoneNumber(phoneNumber);
    }

    /**
     * 전화번호로 가입 여부 확인.
     *
     * @param phoneNumber 전화번호
     * @return 가입 여부
     */
    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return memberQueryPort.existsByPhoneNumber(phoneNumber);
    }

    /**
     * 전화번호로 회원 + 인증 정보 조회 (Optional).
     *
     * @param phoneNumber 전화번호
     * @return 회원 + 인증 정보 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<MemberWithCredentials> findWithCredentialsByPhoneNumber(String phoneNumber) {
        return memberQueryPort.findWithCredentialsByPhoneNumber(phoneNumber);
    }

    /**
     * 전화번호로 회원 + 인증 정보 조회 (로그인용).
     *
     * @param phoneNumber 전화번호
     * @return 회원 + 인증 정보
     * @throws MemberNotFoundException 회원이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public MemberWithCredentials getWithCredentialsByPhoneNumber(String phoneNumber) {
        return memberQueryPort
                .findWithCredentialsByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MemberNotFoundException(phoneNumber));
    }

    /**
     * 레거시 사용자 ID로 회원 프로필 조회.
     *
     * @param userId 레거시 user_id
     * @return 회원 프로필 (등급, 마일리지 포함)
     * @throws MemberNotFoundException 회원이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public MemberProfile getProfileByLegacyId(long userId) {
        return memberQueryPort
                .findProfileByLegacyId(userId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(userId)));
    }
}
