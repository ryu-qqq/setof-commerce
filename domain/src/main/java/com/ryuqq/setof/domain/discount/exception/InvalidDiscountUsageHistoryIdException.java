package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 할인 사용 히스토리 ID가 유효하지 않을 때 발생하는 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>DomainException 상속
 *   <li>상세 메시지 포함
 * </ul>
 */
public class InvalidDiscountUsageHistoryIdException extends DomainException {

    /**
     * 예외 생성자
     *
     * @param value 유효하지 않은 ID 값
     * @param detailMessage 상세 메시지
     */
    public InvalidDiscountUsageHistoryIdException(Long value, String detailMessage) {
        super(
                DiscountPolicyErrorCode.INVALID_DISCOUNT_USAGE_HISTORY_ID,
                String.format("%s 입력값: %s", detailMessage, value != null ? value : "null"),
                Map.of("value", value != null ? value : "null"));
    }
}
