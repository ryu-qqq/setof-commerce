package com.ryuqq.setof.domain.exchange.exception;

/**
 * 교환이 이미 존재하는 경우 예외.
 *
 * <p>해당 주문 아이템에 대한 교환이 이미 존재할 때 발생합니다.
 */
public class ExchangeAlreadyExistsException extends ExchangeException {

    private static final ExchangeErrorCode ERROR_CODE = ExchangeErrorCode.EXCHANGE_ALREADY_EXISTS;

    public ExchangeAlreadyExistsException() {
        super(ERROR_CODE);
    }

    public ExchangeAlreadyExistsException(String detail) {
        super(ERROR_CODE, String.format("해당 주문 아이템에 대한 교환이 이미 존재합니다: %s", detail));
    }
}
