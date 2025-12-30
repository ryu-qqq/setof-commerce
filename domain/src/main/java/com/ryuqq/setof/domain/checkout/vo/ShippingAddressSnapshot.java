package com.ryuqq.setof.domain.checkout.vo;

import com.ryuqq.setof.domain.checkout.exception.InvalidShippingAddressException;

/**
 * ShippingAddressSnapshot Value Object
 *
 * <p>결제 시점의 배송지 정보 스냅샷입니다. 결제 후 원본 배송지가 변경되어도 주문의 배송지는 보존됩니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>필수 필드 검증
 * </ul>
 *
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param address 배송 주소
 * @param addressDetail 상세 주소
 * @param zipCode 우편번호
 * @param memo 배송 메모 (nullable)
 */
public record ShippingAddressSnapshot(
        String receiverName,
        String receiverPhone,
        String address,
        String addressDetail,
        String zipCode,
        String memo) {

    /** Compact Constructor - 검증 로직 */
    public ShippingAddressSnapshot {
        validateRequired(receiverName, "수령인 이름");
        validateRequired(receiverPhone, "수령인 연락처");
        validateRequired(address, "배송 주소");
        validateRequired(zipCode, "우편번호");
    }

    /**
     * Static Factory Method - 배송지 스냅샷 생성
     *
     * @param receiverName 수령인 이름
     * @param receiverPhone 수령인 연락처
     * @param address 배송 주소
     * @param addressDetail 상세 주소
     * @param zipCode 우편번호
     * @param memo 배송 메모
     * @return ShippingAddressSnapshot 인스턴스
     */
    public static ShippingAddressSnapshot of(
            String receiverName,
            String receiverPhone,
            String address,
            String addressDetail,
            String zipCode,
            String memo) {
        return new ShippingAddressSnapshot(
                receiverName,
                receiverPhone,
                address,
                addressDetail != null ? addressDetail : "",
                zipCode,
                memo);
    }

    /**
     * 전체 주소 반환
     *
     * @return 기본 주소 + 상세 주소
     */
    public String fullAddress() {
        if (addressDetail == null || addressDetail.isBlank()) {
            return address;
        }
        return address + " " + addressDetail;
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidShippingAddressException(fieldName + "은(는) 필수입니다");
        }
    }
}
