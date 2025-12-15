package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 상품고시 Not Found 예외
 *
 * <p>상품고시를 찾을 수 없을 때 발생합니다. HTTP 응답: 404 NOT FOUND
 */
public class ProductNoticeNotFoundException extends DomainException {

    public ProductNoticeNotFoundException(Long id) {
        super(
                ProductNoticeErrorCode.PRODUCT_NOTICE_NOT_FOUND,
                String.format("상품고시를 찾을 수 없습니다: %d", id),
                Map.of("productNoticeId", id != null ? id : "null"));
    }

    public static ProductNoticeNotFoundException byProductGroupId(Long productGroupId) {
        ProductNoticeNotFoundException exception =
                new ProductNoticeNotFoundException(productGroupId);
        return exception;
    }
}
