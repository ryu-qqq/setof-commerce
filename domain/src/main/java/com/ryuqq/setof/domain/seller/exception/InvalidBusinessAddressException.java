package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 사업장 주소 예외
 *
 * <p>사업장 주소가 올바르지 않은 경우 발생합니다.
 */
public class InvalidBusinessAddressException extends DomainException {

    public InvalidBusinessAddressException(String reason) {
        super(
                SellerErrorCode.INVALID_BUSINESS_ADDRESS,
                String.format("사업장 주소가 올바르지 않습니다. reason: %s", reason),
                Map.of("reason", reason));
    }
}
