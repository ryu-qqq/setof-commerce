package com.ryuqq.setof.application.member.manager.query;

import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.SocialId;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Member Read Manager
 *
 * <p>단일 Query Port 조회를 담당하는 Read Manager
 *
 * <p>읽기 전용 트랜잭션 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberReadManager {

    private final MemberQueryPort memberQueryPort;

    public MemberReadManager(MemberQueryPort memberQueryPort) {
        this.memberQueryPort = memberQueryPort;
    }

    /**
     * 회원 ID로 조회 (필수)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 조회된 Member
     * @throws MemberNotFoundException 회원을 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Member findById(String memberId) {
        MemberId id = MemberId.of(memberId);
        return memberQueryPort
                .findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id.value()));
    }

    /**
     * 회원 ID로 조회 (선택)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 조회된 Member (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Member> findByIdOptional(String memberId) {
        MemberId id = MemberId.of(memberId);
        return memberQueryPort.findById(id);
    }

    /**
     * 핸드폰 번호로 조회 (필수)
     *
     * @param phoneNumber 핸드폰 번호
     * @return 조회된 Member
     * @throws MemberNotFoundException 회원을 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Member findByPhoneNumber(String phoneNumber) {
        PhoneNumber phone = PhoneNumber.of(phoneNumber);
        return memberQueryPort
                .findByPhoneNumber(phone)
                .orElseThrow(() -> new MemberNotFoundException(phoneNumber));
    }

    /**
     * 핸드폰 번호로 조회 (선택)
     *
     * @param phoneNumber 핸드폰 번호
     * @return 조회된 Member (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Member> findByPhoneNumberOptional(String phoneNumber) {
        PhoneNumber phone = PhoneNumber.of(phoneNumber);
        return memberQueryPort.findByPhoneNumber(phone);
    }

    /**
     * 소셜 ID로 조회 (선택)
     *
     * @param socialId 소셜 ID (카카오 ID 등)
     * @return 조회된 Member (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Member> findBySocialId(String socialId) {
        SocialId id = SocialId.of(socialId);
        return memberQueryPort.findBySocialId(id);
    }

    /**
     * 핸드폰 번호 존재 여부 확인
     *
     * @param phoneNumber 핸드폰 번호
     * @return 존재하면 true, 없으면 false
     */
    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        PhoneNumber phone = PhoneNumber.of(phoneNumber);
        return memberQueryPort.existsByPhoneNumber(phone);
    }
}
