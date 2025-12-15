package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 상품설명 ID 예외
 *
 * <p>상품설명 ID가 유효하지 않을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidProductDescriptionIdException extends DomainException {

    public InvalidProductDescriptionIdException(Long id) {
        super(
                ProductDescriptionErrorCode.INVALID_PRODUCT_DESCRIPTION_ID,
                String.format("유효하지 않은 상품설명 ID: %d", id),
                Map.of("productDescriptionId", id != null ? id : "null"));
    }
}
