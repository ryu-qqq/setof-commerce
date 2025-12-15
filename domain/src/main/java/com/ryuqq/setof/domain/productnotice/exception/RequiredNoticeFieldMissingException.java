package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.List;
import java.util.Map;

/**
 * 필수 고시 필드 누락 예외
 *
 * <p>상품고시에 필수 필드가 누락되었을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class RequiredNoticeFieldMissingException extends DomainException {

    public RequiredNoticeFieldMissingException(List<String> missingFieldKeys) {
        super(
                ProductNoticeErrorCode.REQUIRED_FIELD_MISSING,
                String.format("필수 고시 항목이 누락되었습니다: %s", String.join(", ", missingFieldKeys)),
                Map.of("missingFields", missingFieldKeys));
    }

    public RequiredNoticeFieldMissingException(String missingFieldKey) {
        this(List.of(missingFieldKey));
    }
}
