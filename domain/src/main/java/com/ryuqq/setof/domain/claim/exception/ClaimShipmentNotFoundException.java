package com.ryuqq.setof.domain.claim.exception;

/**
 * 클레임 배송을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 클레임 배송이 존재하지 않을 때 발생합니다.
 */
public class ClaimShipmentNotFoundException extends ClaimShipmentException {

    private static final ClaimShipmentErrorCode ERROR_CODE =
            ClaimShipmentErrorCode.CLAIM_SHIPMENT_NOT_FOUND;

    public ClaimShipmentNotFoundException() {
        super(ERROR_CODE);
    }

    public ClaimShipmentNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 클레임 배송을 찾을 수 없습니다", id));
    }
}
