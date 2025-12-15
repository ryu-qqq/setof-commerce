package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 유효하지 않은 금액 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class InvalidMoneyException extends DomainException {

    /**
     * 유효하지 않은 금액으로 예외 생성
     *
     * @param value 유효하지 않은 금액 값
     * @param reason 유효하지 않은 이유
     */
    public InvalidMoneyException(BigDecimal value, String reason) {
        super(
                ProductGroupErrorCode.INVALID_MONEY,
                String.format(
                        "유효하지 않은 금액: %s, 사유: %s",
                        value != null ? value.toString() : "null", reason),
                Map.of("value", value != null ? value.toString() : "null", "reason", reason));
    }
}
