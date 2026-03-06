package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import java.util.Map;

/** 유효하지 않은 상태 전이 시 발생하는 예외. */
public class ProductInvalidStatusTransitionException extends DomainException {

    public ProductInvalidStatusTransitionException(ProductStatus from, ProductStatus to) {
        super(
                ProductErrorCode.INVALID_STATUS_TRANSITION,
                String.format("상태 %s에서 %s로 전이할 수 없습니다", from, to),
                Map.of("from", from.name(), "to", to.name()));
    }
}
