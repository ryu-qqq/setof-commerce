package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

import java.util.Map;
import java.util.UUID;

/**
 * 카카오 회원이 비밀번호 변경 시도 시 발생하는 예외
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class KakaoMemberCannotChangePasswordException extends DomainException {

    public KakaoMemberCannotChangePasswordException(UUID memberId) {
        super(
            MemberErrorCode.KAKAO_MEMBER_CANNOT_CHANGE_PASSWORD,
            String.format("카카오 회원은 비밀번호를 변경할 수 없습니다. 회원 ID: %s", memberId),
            Map.of("memberId", memberId)
        );
    }
}
