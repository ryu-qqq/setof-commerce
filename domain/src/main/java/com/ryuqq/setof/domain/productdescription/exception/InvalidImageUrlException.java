package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 이미지 URL 예외
 *
 * <p>이미지 URL이 유효하지 않을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidImageUrlException extends DomainException {

    public InvalidImageUrlException(String reason) {
        super(ProductDescriptionErrorCode.INVALID_IMAGE_URL, reason, Map.of("reason", reason));
    }
}
