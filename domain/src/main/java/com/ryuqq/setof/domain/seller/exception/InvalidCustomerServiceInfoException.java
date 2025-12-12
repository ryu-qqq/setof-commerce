package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Collections;

/**
 * 잘못된 고객 서비스 정보 예외
 *
 * <p>이메일, 휴대폰, 유선전화 중 하나도 없는 경우 발생합니다.
 */
public class InvalidCustomerServiceInfoException extends DomainException {

    public InvalidCustomerServiceInfoException() {
        super(
                SellerErrorCode.INVALID_CUSTOMER_SERVICE_INFO,
                "이메일, 휴대폰, 유선전화 중 최소 1개는 필수입니다.",
                Collections.emptyMap());
    }
}
