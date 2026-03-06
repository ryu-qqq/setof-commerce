package com.ryuqq.setof.domain.member.exception;

/**
 * 회원을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 회원이 존재하지 않을 때 발생합니다.
 */
public class MemberNotFoundException extends MemberException {

    private static final MemberErrorCode ERROR_CODE = MemberErrorCode.MEMBER_NOT_FOUND;

    public MemberNotFoundException() {
        super(ERROR_CODE);
    }

    public MemberNotFoundException(String memberId) {
        super(ERROR_CODE, String.format("ID가 %s인 회원을 찾을 수 없습니다", memberId));
    }

    /**
     * 커스텀 메시지로 예외 생성.
     *
     * @param message 커스텀 메시지
     * @return MemberNotFoundException
     */
    public static MemberNotFoundException withMessage(String message) {
        return new MemberNotFoundException(ERROR_CODE, message);
    }

    private MemberNotFoundException(MemberErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
