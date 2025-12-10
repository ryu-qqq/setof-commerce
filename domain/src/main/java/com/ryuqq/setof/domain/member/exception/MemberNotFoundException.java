package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;
import java.util.UUID;

/**
 * 회원을 찾을 수 없을 때 발생하는 예외
 *
 * <p>HTTP 응답: 404 NOT FOUND
 *
 * @author development-team
 * @since 1.0.0
 */
public final class MemberNotFoundException extends DomainException {

    public MemberNotFoundException(UUID memberId) {
        super(
            MemberErrorCode.MEMBER_NOT_FOUND,
            String.format("회원을 찾을 수 없습니다. 회원 ID: %s", memberId),
            Map.of("memberId", memberId)
        );
    }

    public MemberNotFoundException(String email) {
        super(
            MemberErrorCode.MEMBER_NOT_FOUND,
            String.format("회원을 찾을 수 없습니다. 이메일: %s", email),
            Map.of("email", email)
        );
    }
}
