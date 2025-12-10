package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;
import java.util.UUID;

/**
 * 휴면 또는 정지된 회원이 로그인 시도 시 발생하는 예외
 *
 * <p>HTTP 응답: 403 FORBIDDEN
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InactiveMemberException extends DomainException {

    public InactiveMemberException(UUID memberId, String status) {
        super(
            MemberErrorCode.INACTIVE_MEMBER,
            String.format("휴면 또는 정지된 회원입니다. 회원 ID: %s, 상태: %s", memberId, status),
            Map.of("memberId", memberId, "status", status)
        );
    }

    public InactiveMemberException(UUID memberId) {
        super(
            MemberErrorCode.INACTIVE_MEMBER,
            String.format("휴면 또는 정지된 회원입니다. 회원 ID: %s", memberId),
            Map.of("memberId", memberId)
        );
    }
}
