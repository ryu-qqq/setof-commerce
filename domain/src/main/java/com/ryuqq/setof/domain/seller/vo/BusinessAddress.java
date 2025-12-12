package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidBusinessAddressException;

/**
 * 사업장 주소 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>addressLine1, zipCode 필수
 *   <li>addressLine2 선택
 * </ul>
 *
 * @param addressLine1 기본 주소 (필수)
 * @param addressLine2 상세 주소 (선택)
 * @param zipCode 우편번호 (필수)
 */
public record BusinessAddress(String addressLine1, String addressLine2, String zipCode) {

    private static final int ADDRESS_LINE1_MAX_LENGTH = 200;
    private static final int ADDRESS_LINE2_MAX_LENGTH = 100;
    private static final int ZIP_CODE_MAX_LENGTH = 10;

    /** Compact Constructor - 검증 로직 */
    public BusinessAddress {
        validate(addressLine1, addressLine2, zipCode);
    }

    /**
     * Static Factory Method
     *
     * @param addressLine1 기본 주소 (필수)
     * @param addressLine2 상세 주소 (선택)
     * @param zipCode 우편번호 (필수)
     * @return BusinessAddress 인스턴스
     * @throws InvalidBusinessAddressException 필수값이 없거나 형식이 올바르지 않은 경우
     */
    public static BusinessAddress of(String addressLine1, String addressLine2, String zipCode) {
        return new BusinessAddress(addressLine1, addressLine2, zipCode);
    }

    /**
     * 상세 주소 존재 여부 확인
     *
     * @return 상세 주소가 있으면 true
     */
    public boolean hasAddressLine2() {
        return addressLine2 != null && !addressLine2.isBlank();
    }

    /**
     * 전체 주소 반환
     *
     * @return 기본 주소 + 상세 주소 조합
     */
    public String fullAddress() {
        if (hasAddressLine2()) {
            return addressLine1 + " " + addressLine2;
        }
        return addressLine1;
    }

    private static void validate(String addressLine1, String addressLine2, String zipCode) {
        if (addressLine1 == null || addressLine1.isBlank()) {
            throw new InvalidBusinessAddressException("기본 주소는 필수입니다.");
        }

        if (addressLine1.length() > ADDRESS_LINE1_MAX_LENGTH) {
            throw new InvalidBusinessAddressException("기본 주소는 200자를 초과할 수 없습니다.");
        }

        if (addressLine2 != null && addressLine2.length() > ADDRESS_LINE2_MAX_LENGTH) {
            throw new InvalidBusinessAddressException("상세 주소는 100자를 초과할 수 없습니다.");
        }

        if (zipCode == null || zipCode.isBlank()) {
            throw new InvalidBusinessAddressException("우편번호는 필수입니다.");
        }

        if (zipCode.length() > ZIP_CODE_MAX_LENGTH) {
            throw new InvalidBusinessAddressException("우편번호는 10자를 초과할 수 없습니다.");
        }
    }
}
