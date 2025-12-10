package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;
import java.util.UUID;

/**
 * 이미 탈퇴한 회원에 대한 작업 시 발생하는 예외
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class AlreadyWithdrawnMemberException extends DomainException {

    public AlreadyWithdrawnMemberException(UUID memberId) {
        super(
            MemberErrorCode.ALREADY_WITHDRAWN_MEMBER,
            String.format("이미 탈퇴한 회원입니다. 회원 ID: %s", memberId),
            Map.of("memberId", memberId)
        );
    }
}
