package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 템플릿 ID 예외
 *
 * <p>템플릿 ID가 유효하지 않을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidNoticeTemplateIdException extends DomainException {

    public InvalidNoticeTemplateIdException(Long id) {
        super(
                ProductNoticeErrorCode.INVALID_NOTICE_TEMPLATE_ID,
                String.format("유효하지 않은 템플릿 ID: %d", id),
                Map.of("templateId", id != null ? id : "null"));
    }
}
