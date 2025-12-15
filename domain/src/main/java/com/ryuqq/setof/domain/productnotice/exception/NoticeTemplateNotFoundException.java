package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 상품고시 템플릿 Not Found 예외
 *
 * <p>상품고시 템플릿을 찾을 수 없을 때 발생합니다. HTTP 응답: 404 NOT FOUND
 */
public class NoticeTemplateNotFoundException extends DomainException {

    public NoticeTemplateNotFoundException(Long id) {
        super(
                ProductNoticeErrorCode.NOTICE_TEMPLATE_NOT_FOUND,
                String.format("상품고시 템플릿을 찾을 수 없습니다: %d", id),
                Map.of("templateId", id != null ? id : "null"));
    }

    public static NoticeTemplateNotFoundException byCategoryId(Long categoryId) {
        return new NoticeTemplateNotFoundException(categoryId);
    }
}
