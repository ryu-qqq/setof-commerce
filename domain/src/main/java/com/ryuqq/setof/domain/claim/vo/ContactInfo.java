package com.ryuqq.setof.domain.claim.vo;

import com.ryuqq.setof.domain.common.vo.PhoneNumber;

/**
 * 수거지 연락처 Value Object.
 *
 * <p>클레임 수거에 필요한 발송인 연락처 정보를 나타냅니다.
 *
 * @param name 이름
 * @param phone 전화번호
 * @param address 주소
 * @param addressDetail 상세 주소 (nullable)
 * @param zipCode 우편번호 (nullable)
 */
public record ContactInfo(
        String name, PhoneNumber phone, String address, String addressDetail, String zipCode) {

    public ContactInfo {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다");
        }
        if (phone == null) {
            throw new IllegalArgumentException("전화번호는 필수입니다");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("주소는 필수입니다");
        }
    }

    public static ContactInfo of(
            String name, PhoneNumber phone, String address, String addressDetail, String zipCode) {
        return new ContactInfo(name, phone, address, addressDetail, zipCode);
    }

    /**
     * 전화번호 문자열 값을 반환합니다.
     *
     * @return 전화번호 문자열
     */
    public String phoneValue() {
        return phone.value();
    }
}
