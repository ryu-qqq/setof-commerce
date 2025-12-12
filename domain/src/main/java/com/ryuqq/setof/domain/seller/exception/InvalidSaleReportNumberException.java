package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 통신판매업 신고번호 예외
 *
 * <p>통신판매업 신고번호 형식이 올바르지 않은 경우 발생합니다.
 */
public class InvalidSaleReportNumberException extends DomainException {

    public InvalidSaleReportNumberException(String value, String reason) {
        super(
                SellerErrorCode.INVALID_SALE_REPORT_NUMBER,
                String.format("통신판매업 신고번호가 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
