package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 상품고시 ID 예외
 *
 * <p>상품고시 ID가 유효하지 않을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidProductNoticeIdException extends DomainException {

    public InvalidProductNoticeIdException(Long id) {
        super(
                ProductNoticeErrorCode.INVALID_PRODUCT_NOTICE_ID,
                String.format("유효하지 않은 상품고시 ID: %d", id),
                Map.of("productNoticeId", id != null ? id : "null"));
    }
}
