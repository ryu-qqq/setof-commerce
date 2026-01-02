package com.ryuqq.setof.domain.shipment.vo;

import com.ryuqq.setof.domain.shipment.exception.InvalidInvoiceNumberException;

/**
 * 운송장 번호 Value Object
 *
 * <p>택배사에서 발급한 운송장 번호입니다. 택배사마다 운송장 번호 형식이 다를 수 있습니다. 예: "1234567890", "123-456-789012"
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param value 운송장 번호 값
 */
public record InvoiceNumber(String value) {

    private static final int MAX_LENGTH = 30;

    /** Compact Constructor - 검증 로직 */
    public InvoiceNumber {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 운송장 번호 문자열
     * @return InvoiceNumber 인스턴스
     * @throws InvalidInvoiceNumberException value가 유효하지 않은 경우
     */
    public static InvoiceNumber of(String value) {
        return new InvoiceNumber(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidInvoiceNumberException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidInvoiceNumberException(value);
        }
    }

    /**
     * 하이픈 제거된 운송장 번호 반환
     *
     * <p>스마트택배 API 호출 시 하이픈 없는 번호가 필요할 수 있습니다.
     *
     * @return 하이픈이 제거된 운송장 번호
     */
    public String withoutHyphen() {
        return value.replace("-", "");
    }
}
