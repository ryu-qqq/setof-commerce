package com.ryuqq.setof.application.member.component;

import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.MemberNotFoundException;
import com.ryuqq.setof.domain.core.member.vo.MemberId;
import com.ryuqq.setof.domain.core.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.core.member.vo.SocialId;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Member Reader
 *
 * <p>회원 조회 전용 컴포넌트. Query 책임만 담당.
 *
 * <p>책임:
 *
 * <ul>
 *   <li>회원 ID로 조회
 *   <li>핸드폰 번호로 조회
 *   <li>소셜 ID로 조회
 *   <li>핸드폰 번호 존재 여부 확인
 * </ul>
 *
 * <p>SRP: 조회 책임만 담당하며, 검증 로직은 MemberPolicyValidator에서 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberReader {

    private final MemberQueryPort memberQueryPort;

    public MemberReader(MemberQueryPort memberQueryPort) {
        this.memberQueryPort = memberQueryPort;
    }

    /**
     * 회원 ID로 조회
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 조회된 Member
     * @throws MemberNotFoundException 회원을 찾을 수 없는 경우
     */
    public Member getById(String memberId) {
        MemberId id = MemberId.of(memberId);
        return memberQueryPort.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 핸드폰 번호로 조회
     *
     * @param phoneNumber 핸드폰 번호
     * @return 조회된 Member
     * @throws MemberNotFoundException 회원을 찾을 수 없는 경우
     */
    public Member getByPhoneNumber(String phoneNumber) {
        PhoneNumber phone = PhoneNumber.of(phoneNumber);
        return memberQueryPort.findByPhoneNumber(phone).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 소셜 ID로 조회 (Optional)
     *
     * @param socialId 소셜 ID (카카오 ID 등)
     * @return 조회된 Member (Optional)
     */
    public Optional<Member> findBySocialId(String socialId) {
        SocialId id = SocialId.of(socialId);
        return memberQueryPort.findBySocialId(id);
    }

    /**
     * 핸드폰 번호로 조회 (Optional)
     *
     * @param phoneNumber 핸드폰 번호
     * @return 조회된 Member (Optional)
     */
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        PhoneNumber phone = PhoneNumber.of(phoneNumber);
        return memberQueryPort.findByPhoneNumber(phone);
    }

    /**
     * 핸드폰 번호 존재 여부 확인
     *
     * @param phoneNumber 핸드폰 번호
     * @return 존재하면 true, 없으면 false
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        PhoneNumber phone = PhoneNumber.of(phoneNumber);
        return memberQueryPort.existsByPhoneNumber(phone);
    }
}
