package com.ryuqq.setof.domain.shippingaddress.vo;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidDeliveryAddressException;

/**
 * 배송 주소 Value Object (Composite VO)
 *
 * <p>도로명주소/지번주소와 상세주소, 우편번호를 함께 관리합니다. 도로명주소 또는 지번주소 중 하나는 반드시 입력해야 합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>도로명주소: 최대 200자 (nullable)
 *   <li>지번주소: 최대 200자 (nullable)
 *   <li>상세주소: 최대 100자 (nullable)
 *   <li>우편번호: 5~10자, 필수
 * </ul>
 *
 * @param roadAddress 도로명 주소 (nullable)
 * @param jibunAddress 지번 주소 (nullable)
 * @param detailAddress 상세 주소 (nullable)
 * @param zipCode 우편번호
 */
public record DeliveryAddress(
        String roadAddress, String jibunAddress, String detailAddress, String zipCode) {

    private static final int ADDRESS_MAX_LENGTH = 200;
    private static final int DETAIL_MAX_LENGTH = 100;
    private static final int ZIP_CODE_MIN_LENGTH = 5;
    private static final int ZIP_CODE_MAX_LENGTH = 10;

    /** Compact Constructor - 검증 로직 */
    public DeliveryAddress {
        validateAddresses(roadAddress, jibunAddress);
        validateDetailAddress(detailAddress);
        validateZipCode(zipCode);
    }

    /**
     * Static Factory Method - 도로명주소 기반
     *
     * @param roadAddress 도로명 주소
     * @param detailAddress 상세 주소
     * @param zipCode 우편번호
     * @return DeliveryAddress 인스턴스
     */
    public static DeliveryAddress ofRoadAddress(
            String roadAddress, String detailAddress, String zipCode) {
        return new DeliveryAddress(roadAddress, null, detailAddress, zipCode);
    }

    /**
     * Static Factory Method - 지번주소 기반
     *
     * @param jibunAddress 지번 주소
     * @param detailAddress 상세 주소
     * @param zipCode 우편번호
     * @return DeliveryAddress 인스턴스
     */
    public static DeliveryAddress ofJibunAddress(
            String jibunAddress, String detailAddress, String zipCode) {
        return new DeliveryAddress(null, jibunAddress, detailAddress, zipCode);
    }

    /**
     * Static Factory Method - 도로명 + 지번 모두
     *
     * @param roadAddress 도로명 주소
     * @param jibunAddress 지번 주소
     * @param detailAddress 상세 주소
     * @param zipCode 우편번호
     * @return DeliveryAddress 인스턴스
     */
    public static DeliveryAddress of(
            String roadAddress, String jibunAddress, String detailAddress, String zipCode) {
        return new DeliveryAddress(roadAddress, jibunAddress, detailAddress, zipCode);
    }

    private static void validateAddresses(String roadAddress, String jibunAddress) {
        boolean hasRoad = roadAddress != null && !roadAddress.isBlank();
        boolean hasJibun = jibunAddress != null && !jibunAddress.isBlank();

        if (!hasRoad && !hasJibun) {
            throw InvalidDeliveryAddressException.addressRequired();
        }
        if (hasRoad && roadAddress.length() > ADDRESS_MAX_LENGTH) {
            throw InvalidDeliveryAddressException.addressTooLong("roadAddress", ADDRESS_MAX_LENGTH);
        }
        if (hasJibun && jibunAddress.length() > ADDRESS_MAX_LENGTH) {
            throw InvalidDeliveryAddressException.addressTooLong(
                    "jibunAddress", ADDRESS_MAX_LENGTH);
        }
    }

    private static void validateDetailAddress(String detailAddress) {
        if (detailAddress != null && detailAddress.length() > DETAIL_MAX_LENGTH) {
            throw InvalidDeliveryAddressException.addressTooLong(
                    "detailAddress", DETAIL_MAX_LENGTH);
        }
    }

    private static void validateZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) {
            throw InvalidDeliveryAddressException.zipCodeRequired();
        }
        if (zipCode.length() < ZIP_CODE_MIN_LENGTH || zipCode.length() > ZIP_CODE_MAX_LENGTH) {
            throw InvalidDeliveryAddressException.invalidZipCode(zipCode);
        }
    }

    /**
     * 도로명주소 존재 여부
     *
     * @return 도로명주소가 있으면 true
     */
    public boolean hasRoadAddress() {
        return roadAddress != null && !roadAddress.isBlank();
    }

    /**
     * 지번주소 존재 여부
     *
     * @return 지번주소가 있으면 true
     */
    public boolean hasJibunAddress() {
        return jibunAddress != null && !jibunAddress.isBlank();
    }

    /**
     * 대표 주소 반환 (도로명 우선)
     *
     * @return 도로명주소가 있으면 도로명, 없으면 지번
     */
    public String primaryAddress() {
        return hasRoadAddress() ? roadAddress : jibunAddress;
    }

    /**
     * 전체 주소 문자열 반환 (대표주소 + 상세주소)
     *
     * @return 전체 주소 문자열
     */
    public String fullAddress() {
        String primary = primaryAddress();
        if (detailAddress != null && !detailAddress.isBlank()) {
            return primary + " " + detailAddress;
        }
        return primary;
    }
}
