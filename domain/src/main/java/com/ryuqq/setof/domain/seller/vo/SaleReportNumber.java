package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidSaleReportNumberException;

/**
 * 통신판매업 신고번호 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>nullable 허용 - 선택적 필드
 * </ul>
 *
 * @param value 통신판매업 신고번호 값 (null 허용)
 */
public record SaleReportNumber(String value) {

    private static final int MAX_LENGTH = 50;

    /** Compact Constructor - 검증 로직 */
    public SaleReportNumber {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 통신판매업 신고번호 값 (null 허용)
     * @return SaleReportNumber 인스턴스
     * @throws InvalidSaleReportNumberException value가 최대 길이를 초과한 경우
     */
    public static SaleReportNumber of(String value) {
        return new SaleReportNumber(value);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 값이 있으면 true, null이면 false
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }

    private static void validate(String value) {
        if (value == null) {
            return;
        }

        if (value.isBlank()) {
            throw new InvalidSaleReportNumberException(value, "통신판매업 신고번호는 빈 문자열일 수 없습니다.");
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidSaleReportNumberException(value, "통신판매업 신고번호는 50자를 초과할 수 없습니다.");
        }
    }
}
