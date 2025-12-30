package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** InvalidProductSnapshotException - 잘못된 상품 스냅샷 예외 */
public class InvalidProductSnapshotException extends DomainException {

    public InvalidProductSnapshotException(String message) {
        super(OrderErrorCode.INVALID_PRODUCT_SNAPSHOT, message);
    }
}
