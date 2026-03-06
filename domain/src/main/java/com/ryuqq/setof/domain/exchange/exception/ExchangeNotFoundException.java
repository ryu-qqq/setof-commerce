package com.ryuqq.setof.domain.exchange.exception;

/**
 * 교환을 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 교환이 존재하지 않을 때 발생합니다.
 */
public class ExchangeNotFoundException extends ExchangeException {

    private static final ExchangeErrorCode ERROR_CODE = ExchangeErrorCode.EXCHANGE_NOT_FOUND;

    public ExchangeNotFoundException() {
        super(ERROR_CODE);
    }

    public ExchangeNotFoundException(String detail) {
        super(ERROR_CODE, String.format("교환을 찾을 수 없습니다: %s", detail));
    }
}
