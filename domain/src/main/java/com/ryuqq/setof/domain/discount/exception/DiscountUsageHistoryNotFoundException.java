package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 할인 사용 히스토리를 찾을 수 없을 때 발생하는 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>DomainException 상속
 *   <li>찾으려 했던 ID 정보 포함
 * </ul>
 */
public class DiscountUsageHistoryNotFoundException extends DomainException {

    /**
     * 예외 생성자 - ID로 조회 실패
     *
     * @param id 찾으려 했던 히스토리 ID
     */
    public DiscountUsageHistoryNotFoundException(Long id) {
        super(
                DiscountPolicyErrorCode.DISCOUNT_USAGE_HISTORY_NOT_FOUND,
                String.format("할인 사용 히스토리를 찾을 수 없습니다. ID: %s", id),
                Map.of("id", id != null ? id : "null"));
    }

    /**
     * 예외 생성자 - 주문 ID로 조회 실패
     *
     * @param orderId 찾으려 했던 주문 ID
     * @param isOrderSearch 주문 ID 검색 여부 (오버로딩 구분용)
     */
    public DiscountUsageHistoryNotFoundException(String orderId, boolean isOrderSearch) {
        super(
                DiscountPolicyErrorCode.DISCOUNT_USAGE_HISTORY_NOT_FOUND,
                String.format("해당 주문의 할인 사용 히스토리를 찾을 수 없습니다. OrderId: %s", orderId),
                Map.of("orderId", orderId != null ? orderId : "null"));
    }
}
