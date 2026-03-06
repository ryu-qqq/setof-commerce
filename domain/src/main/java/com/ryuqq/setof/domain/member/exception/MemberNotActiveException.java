package com.ryuqq.setof.domain.member.exception;

/**
 * 비활성 회원 로그인 시도 예외.
 *
 * <p>탈퇴, 정지 등 로그인이 불가능한 상태의 회원이 로그인을 시도할 때 발생합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class MemberNotActiveException extends MemberException {

    private static final MemberErrorCode ERROR_CODE = MemberErrorCode.MEMBER_NOT_ACTIVE;

    public MemberNotActiveException() {
        super(ERROR_CODE);
    }

    public MemberNotActiveException(String memberId) {
        super(ERROR_CODE, String.format("로그인할 수 없는 회원입니다: %s", memberId));
    }
}
