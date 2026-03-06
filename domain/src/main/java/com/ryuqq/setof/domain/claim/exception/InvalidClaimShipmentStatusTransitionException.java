package com.ryuqq.setof.domain.claim.exception;

import com.ryuqq.setof.domain.claim.vo.ClaimShipmentStatus;

/**
 * 유효하지 않은 클레임 배송 상태 전이 예외.
 *
 * <p>허용되지 않는 클레임 배송 상태 전이를 시도할 때 발생합니다.
 */
public class InvalidClaimShipmentStatusTransitionException extends ClaimShipmentException {

    private static final ClaimShipmentErrorCode ERROR_CODE =
            ClaimShipmentErrorCode.INVALID_CLAIM_SHIPMENT_STATUS_TRANSITION;

    public InvalidClaimShipmentStatusTransitionException() {
        super(ERROR_CODE);
    }

    public InvalidClaimShipmentStatusTransitionException(
            ClaimShipmentStatus from, ClaimShipmentStatus to) {
        super(ERROR_CODE, String.format("클레임 배송 상태를 %s에서 %s(으)로 전이할 수 없습니다", from, to));
    }
}
