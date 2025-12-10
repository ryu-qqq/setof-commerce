package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 회원 이름에 대한 도메인 예외
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidMemberNameException extends DomainException {

    public InvalidMemberNameException(String value, String reason) {
        super(
                MemberErrorCode.INVALID_MEMBER_NAME,
                String.format(
                        "회원 이름이 올바르지 않습니다. 입력값: %s, 사유: %s",
                        value != null ? value : "null", reason),
                Map.of("invalidValue", value != null ? value : "null", "reason", reason));
    }

    public InvalidMemberNameException(String value) {
        super(
                MemberErrorCode.INVALID_MEMBER_NAME,
                String.format("회원 이름이 올바르지 않습니다. 입력값: %s", value != null ? value : "null"),
                Map.of("invalidValue", value != null ? value : "null"));
    }
}
