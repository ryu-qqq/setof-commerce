package com.ryuqq.setof.domain.claim.exception;

import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * ClaimStatusException - 클레임 상태 예외
 *
 * <p>클레임 상태 전이 규칙 위반 시 발생하는 예외입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public class ClaimStatusException extends DomainException {

    /**
     * 승인할 수 없는 상태
     *
     * @param currentStatus 현재 상태
     * @return ClaimStatusException
     */
    public static ClaimStatusException cannotApprove(ClaimStatus currentStatus) {
        return new ClaimStatusException(ClaimErrorCode.CLAIM_CANNOT_APPROVE, currentStatus);
    }

    /**
     * 반려할 수 없는 상태
     *
     * @param currentStatus 현재 상태
     * @return ClaimStatusException
     */
    public static ClaimStatusException cannotReject(ClaimStatus currentStatus) {
        return new ClaimStatusException(ClaimErrorCode.CLAIM_CANNOT_REJECT, currentStatus);
    }

    /**
     * 처리 시작할 수 없는 상태
     *
     * @param currentStatus 현재 상태
     * @return ClaimStatusException
     */
    public static ClaimStatusException cannotStartProcessing(ClaimStatus currentStatus) {
        return new ClaimStatusException(
                ClaimErrorCode.CLAIM_CANNOT_START_PROCESSING, currentStatus);
    }

    /**
     * 완료 처리할 수 없는 상태
     *
     * @param currentStatus 현재 상태
     * @return ClaimStatusException
     */
    public static ClaimStatusException cannotComplete(ClaimStatus currentStatus) {
        return new ClaimStatusException(ClaimErrorCode.CLAIM_CANNOT_COMPLETE, currentStatus);
    }

    /**
     * 취소할 수 없는 상태
     *
     * @param currentStatus 현재 상태
     * @return ClaimStatusException
     */
    public static ClaimStatusException cannotCancel(ClaimStatus currentStatus) {
        return new ClaimStatusException(ClaimErrorCode.CLAIM_CANNOT_CANCEL, currentStatus);
    }

    private ClaimStatusException(ClaimErrorCode errorCode, ClaimStatus currentStatus) {
        super(
                errorCode,
                String.format("클레임 상태 오류 - 현재 상태: %s", currentStatus.description()),
                Map.of("currentStatus", currentStatus.name()));
    }
}
