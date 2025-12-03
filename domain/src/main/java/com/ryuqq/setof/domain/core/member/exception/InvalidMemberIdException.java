package com.ryuqq.setof.domain.core.member.exception;

import com.ryuqq.setof.domain.core.exception.DomainException;

/**
 * 잘못된 회원 ID에 대한 도메인 예외
 *
 * <p>MemberId 생성 시 다음 조건을 위반할 때 발생:
 *
 * <ul>
 *   <li>null 값
 *   <li>잘못된 UUID 형식
 * </ul>
 */
public final class InvalidMemberIdException extends DomainException {

    private static final String DEFAULT_MESSAGE = "회원 ID는 null이 아닌 유효한 UUID여야 합니다.";

    public InvalidMemberIdException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidMemberIdException(String invalidValue) {
        super(String.format("잘못된 회원 ID: %s. 회원 ID는 유효한 UUID 형식이어야 합니다.", invalidValue));
    }
}
