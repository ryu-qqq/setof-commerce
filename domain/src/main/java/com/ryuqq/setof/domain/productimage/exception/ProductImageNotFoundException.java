package com.ryuqq.setof.domain.productimage.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 상품이미지 Not Found 예외
 *
 * <p>상품이미지를 찾을 수 없을 때 발생합니다. HTTP 응답: 404 NOT FOUND
 */
public class ProductImageNotFoundException extends DomainException {

    public ProductImageNotFoundException(Long id) {
        super(
                ProductImageErrorCode.PRODUCT_IMAGE_NOT_FOUND,
                String.format("상품이미지를 찾을 수 없습니다: %d", id),
                Map.of("productImageId", id != null ? id : "null"));
    }
}
