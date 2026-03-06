package com.ryuqq.setof.domain.member.exception;

/**
 * 회원 인증 정보를 찾을 수 없는 경우 예외.
 *
 * <p>요청한 인증 제공자 또는 회원 ID에 해당하는 인증 정보가 존재하지 않을 때 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class MemberAuthNotFoundException extends MemberException {

    private static final MemberErrorCode ERROR_CODE = MemberErrorCode.MEMBER_AUTH_NOT_FOUND;

    public MemberAuthNotFoundException() {
        super(ERROR_CODE);
    }

    public MemberAuthNotFoundException(String detail) {
        super(ERROR_CODE, String.format("인증 정보를 찾을 수 없습니다: %s", detail));
    }
}
