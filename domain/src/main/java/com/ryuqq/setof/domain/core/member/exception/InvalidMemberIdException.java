package com.ryuqq.setof.domain.core.member.exception;

/**
 * 잘못된 회원 ID에 대한 도메인 예외
 *
 * <p>MemberId 생성 시 다음 조건을 위반할 때 발생:</p>
 * <ul>
 *     <li>null 값</li>
 *     <li>0 이하의 값</li>
 * </ul>
 */
public final class InvalidMemberIdException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "회원 ID는 null이 아닌 양수여야 합니다.";

    public InvalidMemberIdException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidMemberIdException(String message) {
        super(message);
    }

    public InvalidMemberIdException(Long invalidValue) {
        super(String.format("잘못된 회원 ID: %s. 회원 ID는 양수여야 합니다.", invalidValue));
    }
}
