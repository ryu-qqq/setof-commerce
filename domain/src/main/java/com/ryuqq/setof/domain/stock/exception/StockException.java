package com.ryuqq.setof.domain.stock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 재고 도메인 예외 기본 클래스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class StockException extends DomainException {

    public StockException(StockErrorCode errorCode) {
        super(errorCode);
    }

    public StockException(StockErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
