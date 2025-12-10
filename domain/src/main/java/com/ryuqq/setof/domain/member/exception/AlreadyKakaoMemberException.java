package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;
import java.util.UUID;

/**
 * 이미 카카오 연동된 회원에 대해 카카오 연동 시도 시 발생하는 예외
 *
 * <p>HTTP 응답: 409 CONFLICT
 *
 * @author development-team
 * @since 1.0.0
 */
public final class AlreadyKakaoMemberException extends DomainException {

    public AlreadyKakaoMemberException(UUID memberId) {
        super(
            MemberErrorCode.ALREADY_KAKAO_MEMBER,
            String.format("이미 카카오로 연동된 회원입니다. 회원 ID: %s", memberId),
            Map.of("memberId", memberId)
        );
    }

    public AlreadyKakaoMemberException(String email) {
        super(
            MemberErrorCode.ALREADY_KAKAO_MEMBER,
            String.format("이미 카카오로 연동된 회원입니다. 이메일: %s", email),
            Map.of("email", email)
        );
    }
}
