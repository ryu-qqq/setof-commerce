package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 상품설명 Not Found 예외
 *
 * <p>상품설명을 찾을 수 없을 때 발생합니다. HTTP 응답: 404 NOT FOUND
 */
public class ProductDescriptionNotFoundException extends DomainException {

    public ProductDescriptionNotFoundException(Long id) {
        super(
                ProductDescriptionErrorCode.PRODUCT_DESCRIPTION_NOT_FOUND,
                String.format("상품설명을 찾을 수 없습니다: %d", id),
                Map.of("productDescriptionId", id != null ? id : "null"));
    }

    public static ProductDescriptionNotFoundException byProductGroupId(Long productGroupId) {
        ProductDescriptionNotFoundException exception =
                new ProductDescriptionNotFoundException(productGroupId);
        return exception;
    }
}
