package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidCustomerServiceInfoException;

/**
 * 고객 서비스 정보 Value Object (Embedded VO)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>이메일, 휴대폰, 유선전화 중 최소 1개 필수
 * </ul>
 *
 * @param email CS 이메일 (선택)
 * @param mobilePhone CS 휴대폰 번호 (선택)
 * @param landlinePhone CS 유선전화 번호 (선택)
 */
public record CustomerServiceInfo(
        CsEmail email, CsMobilePhone mobilePhone, CsLandlinePhone landlinePhone) {

    /** Compact Constructor - 최소 1개 연락처 검증 */
    public CustomerServiceInfo {
        validateAtLeastOneContact(email, mobilePhone, landlinePhone);
    }

    /**
     * Static Factory Method
     *
     * @param email CS 이메일 (선택)
     * @param mobilePhone CS 휴대폰 번호 (선택)
     * @param landlinePhone CS 유선전화 번호 (선택)
     * @return CustomerServiceInfo 인스턴스
     * @throws InvalidCustomerServiceInfoException 연락처가 하나도 없는 경우
     */
    public static CustomerServiceInfo of(
            CsEmail email, CsMobilePhone mobilePhone, CsLandlinePhone landlinePhone) {
        return new CustomerServiceInfo(email, mobilePhone, landlinePhone);
    }

    /**
     * 이메일 존재 여부 확인
     *
     * @return 이메일이 있으면 true
     */
    public boolean hasEmail() {
        return email != null && email.hasValue();
    }

    /**
     * 휴대폰 번호 존재 여부 확인
     *
     * @return 휴대폰 번호가 있으면 true
     */
    public boolean hasMobilePhone() {
        return mobilePhone != null && mobilePhone.hasValue();
    }

    /**
     * 유선전화 번호 존재 여부 확인
     *
     * @return 유선전화 번호가 있으면 true
     */
    public boolean hasLandlinePhone() {
        return landlinePhone != null && landlinePhone.hasValue();
    }

    // ========== Law of Demeter Helper Methods ==========

    public String getEmailValue() {
        return email != null ? email.value() : null;
    }

    public String getMobilePhoneValue() {
        return mobilePhone != null ? mobilePhone.value() : null;
    }

    public String getLandlinePhoneValue() {
        return landlinePhone != null ? landlinePhone.value() : null;
    }

    private static void validateAtLeastOneContact(
            CsEmail email, CsMobilePhone mobilePhone, CsLandlinePhone landlinePhone) {

        boolean hasEmail = email != null && email.hasValue();
        boolean hasMobile = mobilePhone != null && mobilePhone.hasValue();
        boolean hasLandline = landlinePhone != null && landlinePhone.hasValue();

        if (!hasEmail && !hasMobile && !hasLandline) {
            throw new InvalidCustomerServiceInfoException();
        }
    }
}
