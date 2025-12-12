package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 셀러 ID 예외
 *
 * <p>셀러 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidSellerIdException extends DomainException {

    public InvalidSellerIdException(Long value) {
        super(
                SellerErrorCode.INVALID_SELLER_ID,
                String.format("셀러 ID는 null이 아닌 양수여야 합니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
