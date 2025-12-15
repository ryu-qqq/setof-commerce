package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 상품그룹 ID 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class InvalidProductGroupIdException extends DomainException {

    /**
     * 유효하지 않은 상품그룹 ID로 예외 생성
     *
     * @param productGroupId 유효하지 않은 상품그룹 ID 값
     */
    public InvalidProductGroupIdException(Long productGroupId) {
        super(
                ProductGroupErrorCode.INVALID_PRODUCT_GROUP_ID,
                String.format("유효하지 않은 상품그룹 ID: %s", productGroupId),
                Map.of("productGroupId", productGroupId != null ? productGroupId : "null"));
    }
}
