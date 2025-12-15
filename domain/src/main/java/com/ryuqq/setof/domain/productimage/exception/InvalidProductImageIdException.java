package com.ryuqq.setof.domain.productimage.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 상품이미지 ID 예외
 *
 * <p>상품이미지 ID가 유효하지 않을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidProductImageIdException extends DomainException {

    public InvalidProductImageIdException(Long id) {
        super(
                ProductImageErrorCode.INVALID_PRODUCT_IMAGE_ID,
                String.format("유효하지 않은 상품이미지 ID: %d", id),
                Map.of("productImageId", id != null ? id : "null"));
    }
}
