package com.ryuqq.setof.domain.settlement.exception;

/**
 * 정산 정보를 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 정산 정보가 존재하지 않을 때 발생합니다.
 */
public class SettlementNotFoundException extends SettlementException {

    private static final SettlementErrorCode ERROR_CODE = SettlementErrorCode.SETTLEMENT_NOT_FOUND;

    public SettlementNotFoundException() {
        super(ERROR_CODE);
    }

    public SettlementNotFoundException(Long settlementId) {
        super(ERROR_CODE, String.format("ID가 %d인 정산 정보를 찾을 수 없습니다", settlementId));
    }
}
