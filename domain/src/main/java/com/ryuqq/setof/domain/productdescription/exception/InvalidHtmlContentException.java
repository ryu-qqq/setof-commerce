package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 HTML 컨텐츠 예외
 *
 * <p>HTML 컨텐츠가 유효하지 않을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidHtmlContentException extends DomainException {

    public InvalidHtmlContentException(String reason) {
        super(ProductDescriptionErrorCode.INVALID_HTML_CONTENT, reason, Map.of("reason", reason));
    }
}
