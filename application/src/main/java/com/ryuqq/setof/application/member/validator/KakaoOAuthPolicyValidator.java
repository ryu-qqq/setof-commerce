package com.ryuqq.setof.application.member.validator;

import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.member.exception.InactiveMemberException;
import org.springframework.stereotype.Component;

/**
 * Kakao OAuth Policy Validator
 *
 * <p>카카오 OAuth 관련 비즈니스 정책 검증 전용 컴포넌트
 *
 * <p>책임:
 *
 * <ul>
 *   <li>카카오 계정 통합 가능 여부 검증
 *   <li>기존 카카오 회원 로그인 가능 여부 검증
 * </ul>
 *
 * <p>CQS: 모든 메서드는 void 반환 (Command-Query Separation 준수)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class KakaoOAuthPolicyValidator {

    /**
     * 카카오 계정 통합 가능 여부 검증
     *
     * <p>검증 항목:
     *
     * <ol>
     *   <li>이미 카카오 회원이 아닌지 확인
     *   <li>회원 상태가 활성 상태인지 확인
     * </ol>
     *
     * @param member 검증할 Member
     * @throws AlreadyKakaoMemberException 이미 카카오 회원인 경우
     * @throws InactiveMemberException 휴면 또는 정지된 회원인 경우
     */
    public void validateCanIntegrateKakao(Member member) {
        validateNotKakaoMember(member);
        validateMemberActive(member);
    }

    /**
     * 기존 카카오 회원 로그인 가능 여부 검증
     *
     * <p>검증 항목:
     *
     * <ol>
     *   <li>회원 상태가 활성 상태인지 확인
     * </ol>
     *
     * @param member 검증할 Member
     * @throws InactiveMemberException 휴면 또는 정지된 회원인 경우
     */
    public void validateCanKakaoLogin(Member member) {
        validateMemberActive(member);
    }

    private void validateNotKakaoMember(Member member) {
        if (member.isKakaoMember()) {
            throw new AlreadyKakaoMemberException(member.getId().value());
        }
    }

    private void validateMemberActive(Member member) {
        if (!member.isActive()) {
            throw new InactiveMemberException(member.getId().value());
        }
    }
}
