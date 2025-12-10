package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 회원 ID에 대한 도메인 예외
 *
 * <p>MemberId 생성 시 다음 조건을 위반할 때 발생:
 *
 * <ul>
 *   <li>null 값
 *   <li>잘못된 UUID 형식
 * </ul>
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public final class InvalidMemberIdException extends DomainException {

    public InvalidMemberIdException(String invalidValue) {
        super(
                MemberErrorCode.INVALID_MEMBER_ID,
                String.format(
                        "잘못된 회원 ID: %s. 회원 ID는 유효한 UUID 형식이어야 합니다.",
                        invalidValue != null ? invalidValue : "null"),
                Map.of("invalidValue", invalidValue != null ? invalidValue : "null"));
    }
}
