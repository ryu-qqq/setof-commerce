package com.ryuqq.setof.application.member.component;

import com.ryuqq.setof.application.member.port.out.PasswordEncoderPort;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.core.member.exception.DuplicatePhoneNumberException;
import com.ryuqq.setof.domain.core.member.exception.InactiveMemberException;
import com.ryuqq.setof.domain.core.member.exception.InvalidPasswordException;
import org.springframework.stereotype.Component;

/**
 * Member Policy Validator
 *
 * <p>회원 비즈니스 정책 검증 전용 컴포넌트. Validation 책임만 담당.
 *
 * <p>책임:
 *
 * <ul>
 *   <li>로그인 가능 여부 검증
 *   <li>비밀번호 재설정 가능 여부 검증
 *   <li>회원 탈퇴 가능 여부 검증
 *   <li>핸드폰 번호 중복 검증
 * </ul>
 *
 * <p>SRP: 검증 책임만 담당하며, 조회 로직은 MemberReader에서 처리
 *
 * <p>CQS: 모든 메서드는 void 반환 (Command-Query Separation 준수)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberPolicyValidator {

    private final PasswordEncoderPort passwordEncoderPort;

    public MemberPolicyValidator(PasswordEncoderPort passwordEncoderPort) {
        this.passwordEncoderPort = passwordEncoderPort;
    }

    /**
     * 로그인 가능 여부 검증
     *
     * <p>검증 항목:
     *
     * <ol>
     *   <li>카카오 회원이 아닌지 확인
     *   <li>비밀번호 일치 여부 확인
     *   <li>회원 상태 확인 (탈퇴/휴면/정지)
     * </ol>
     *
     * @param member 검증할 Member
     * @param rawPassword 평문 비밀번호
     * @throws AlreadyKakaoMemberException 카카오 회원인 경우
     * @throws InvalidPasswordException 비밀번호 불일치
     * @throws AlreadyWithdrawnMemberException 탈퇴한 회원인 경우
     * @throws InactiveMemberException 휴면 또는 정지된 회원인 경우
     */
    public void validateCanLogin(Member member, String rawPassword) {
        validateNotKakaoMember(member);
        validatePassword(rawPassword, member.getPasswordValue());
        validateMemberStatus(member);
    }

    /**
     * 비밀번호 재설정 가능 여부 검증
     *
     * <p>검증 항목:
     *
     * <ol>
     *   <li>카카오 회원이 아닌지 확인
     * </ol>
     *
     * @param member 검증할 Member
     * @throws AlreadyKakaoMemberException 카카오 회원인 경우
     */
    public void validateCanResetPassword(Member member) {
        validateNotKakaoMember(member);
    }

    /**
     * 회원 탈퇴 가능 여부 검증
     *
     * <p>검증 항목:
     *
     * <ol>
     *   <li>이미 탈퇴한 회원이 아닌지 확인
     * </ol>
     *
     * @param member 검증할 Member
     * @throws AlreadyWithdrawnMemberException 이미 탈퇴한 회원인 경우
     */
    public void validateCanWithdraw(Member member) {
        validateNotWithdrawn(member);
    }

    /**
     * 핸드폰 번호 중복 검증
     *
     * @param exists 핸드폰 번호 존재 여부
     * @throws DuplicatePhoneNumberException 이미 존재하는 경우
     */
    public void validatePhoneNumberNotDuplicate(boolean exists) {
        if (exists) {
            throw new DuplicatePhoneNumberException();
        }
    }

    private void validateNotKakaoMember(Member member) {
        if (member.isKakaoMember()) {
            throw new AlreadyKakaoMemberException();
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoderPort.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException();
        }
    }

    private void validateMemberStatus(Member member) {
        if (member.isWithdrawn()) {
            throw new AlreadyWithdrawnMemberException();
        }
        if (!member.isActive()) {
            throw new InactiveMemberException();
        }
    }

    private void validateNotWithdrawn(Member member) {
        if (member.isWithdrawn()) {
            throw new AlreadyWithdrawnMemberException();
        }
    }
}
