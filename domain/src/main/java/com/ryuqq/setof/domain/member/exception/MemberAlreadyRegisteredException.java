package com.ryuqq.setof.domain.member.exception;

/**
 * 이미 가입된 회원 예외.
 *
 * <p>동일한 인증 제공자로 이미 가입된 회원이 재가입을 시도할 때 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class MemberAlreadyRegisteredException extends MemberException {

    private static final MemberErrorCode ERROR_CODE = MemberErrorCode.MEMBER_ALREADY_REGISTERED;

    public MemberAlreadyRegisteredException() {
        super(ERROR_CODE);
    }

    public MemberAlreadyRegisteredException(String authProvider) {
        super(ERROR_CODE, String.format("이미 %s(으)로 가입된 회원입니다", authProvider));
    }
}
