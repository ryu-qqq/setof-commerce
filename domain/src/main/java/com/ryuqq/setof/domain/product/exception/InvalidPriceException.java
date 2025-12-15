package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 유효하지 않은 가격 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class InvalidPriceException extends DomainException {

    /**
     * 유효하지 않은 가격으로 예외 생성
     *
     * @param regularPrice 정가
     * @param currentPrice 판매가
     * @param reason 유효하지 않은 이유
     */
    public InvalidPriceException(BigDecimal regularPrice, BigDecimal currentPrice, String reason) {
        super(
                ProductGroupErrorCode.INVALID_PRICE,
                String.format(
                        "유효하지 않은 가격 - 정가: %s, 판매가: %s, 사유: %s", regularPrice, currentPrice, reason),
                Map.of(
                        "regularPrice", regularPrice != null ? regularPrice.toString() : "null",
                        "currentPrice", currentPrice != null ? currentPrice.toString() : "null",
                        "reason", reason));
    }
}
