package com.ryuqq.setof.domain.exchange.exception;

import com.ryuqq.setof.domain.exchange.vo.ExchangeStatus;

/**
 * 유효하지 않은 교환 상태 전이 예외.
 *
 * <p>허용되지 않는 교환 상태 전이를 시도할 때 발생합니다.
 */
public class InvalidExchangeStatusTransitionException extends ExchangeException {

    private static final ExchangeErrorCode ERROR_CODE =
            ExchangeErrorCode.INVALID_EXCHANGE_STATUS_TRANSITION;

    public InvalidExchangeStatusTransitionException() {
        super(ERROR_CODE);
    }

    public InvalidExchangeStatusTransitionException(ExchangeStatus current, ExchangeStatus target) {
        super(ERROR_CODE, String.format("교환 상태를 %s에서 %s(으)로 전이할 수 없습니다", current, target));
    }
}
