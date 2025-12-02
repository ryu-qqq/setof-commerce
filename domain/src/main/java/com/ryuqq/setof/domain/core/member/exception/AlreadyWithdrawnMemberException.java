package com.ryuqq.setof.domain.core.member.exception;

/**
 * 이미 탈퇴한 회원에 대한 작업 시 발생하는 예외
 */
public class AlreadyWithdrawnMemberException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 탈퇴한 회원입니다.";

    public AlreadyWithdrawnMemberException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyWithdrawnMemberException(String message) {
        super(message);
    }
}
