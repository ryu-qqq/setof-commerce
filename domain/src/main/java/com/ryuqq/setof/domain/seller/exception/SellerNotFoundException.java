package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 셀러 미존재 예외
 *
 * <p>요청한 셀러 ID에 해당하는 셀러가 존재하지 않을 때 발생합니다.
 */
public class SellerNotFoundException extends DomainException {

    public SellerNotFoundException(Long sellerId) {
        super(
                SellerErrorCode.SELLER_NOT_FOUND,
                String.format("셀러를 찾을 수 없습니다. sellerId: %d", sellerId),
                Map.of("sellerId", sellerId != null ? sellerId : "null"));
    }
}
