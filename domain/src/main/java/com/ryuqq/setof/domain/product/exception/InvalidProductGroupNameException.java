package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 상품그룹명 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class InvalidProductGroupNameException extends DomainException {

    /**
     * 유효하지 않은 상품그룹명으로 예외 생성
     *
     * @param productGroupName 유효하지 않은 상품그룹명
     * @param reason 유효하지 않은 이유
     */
    public InvalidProductGroupNameException(String productGroupName, String reason) {
        super(
                ProductGroupErrorCode.INVALID_PRODUCT_GROUP_NAME,
                String.format(
                        "유효하지 않은 상품그룹명: %s, 사유: %s",
                        productGroupName != null ? productGroupName : "null", reason),
                Map.of(
                        "productGroupName",
                        productGroupName != null ? productGroupName : "null",
                        "reason",
                        reason));
    }
}
