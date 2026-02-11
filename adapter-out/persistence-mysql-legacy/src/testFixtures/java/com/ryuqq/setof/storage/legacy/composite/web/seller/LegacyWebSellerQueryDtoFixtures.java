package com.ryuqq.setof.storage.legacy.composite.web.seller;

import com.ryuqq.setof.storage.legacy.composite.web.seller.dto.LegacyWebSellerQueryDto;
import java.time.LocalDateTime;

/**
 * LegacyWebSellerQueryDto 테스트 Fixtures.
 *
 * <p>레거시 Web 판매자 Composite 조회 결과 DTO의 테스트 데이터를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class LegacyWebSellerQueryDtoFixtures {

    private LegacyWebSellerQueryDtoFixtures() {
        throw new AssertionError("Cannot instantiate LegacyWebSellerQueryDtoFixtures");
    }

    public static final long DEFAULT_SELLER_ID = 1L;
    public static final String DEFAULT_SELLER_NAME = "테스트 셀러";
    public static final String DEFAULT_COMPANY_NAME = "테스트 회사";
    public static final String DEFAULT_LOGO_URL = "https://example.com/logo.png";
    public static final String DEFAULT_SELLER_DESCRIPTION = "테스트 셀러 설명";
    public static final Double DEFAULT_COMMISSION_RATE = 10.0;
    public static final String DEFAULT_ADDRESS_LINE1 = "서울시 강남구";
    public static final String DEFAULT_ADDRESS_LINE2 = "테헤란로 123";
    public static final String DEFAULT_ZIP_CODE = "06234";
    public static final String DEFAULT_CS_NUMBER = "1588-0000";
    public static final String DEFAULT_CS_PHONE = "02-1234-5678";
    public static final String DEFAULT_CS_EMAIL = "cs@test.com";
    public static final String DEFAULT_REGISTRATION_NUMBER = "123-45-67890";
    public static final String DEFAULT_SALE_REPORT_NUMBER = "2024-서울강남-12345";
    public static final String DEFAULT_REPRESENTATIVE = "홍길동";
    public static final String DEFAULT_BANK_NAME = "국민은행";
    public static final String DEFAULT_ACCOUNT_NUMBER = "123-456-789012";
    public static final String DEFAULT_ACCOUNT_HOLDER = "테스트 회사";
    public static final LocalDateTime DEFAULT_CREATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0);
    public static final LocalDateTime DEFAULT_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0);

    /**
     * 기본 LegacyWebSellerQueryDto 생성.
     *
     * @return 모든 필드가 채워진 기본 DTO
     */
    public static LegacyWebSellerQueryDto defaultDto() {
        return new LegacyWebSellerQueryDto(
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                DEFAULT_COMMISSION_RATE,
                DEFAULT_ADDRESS_LINE1,
                DEFAULT_ADDRESS_LINE2,
                DEFAULT_ZIP_CODE,
                DEFAULT_CS_NUMBER,
                DEFAULT_CS_PHONE,
                DEFAULT_CS_EMAIL,
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * sellerId를 지정한 LegacyWebSellerQueryDto 생성.
     *
     * @param sellerId 판매자 ID
     * @return sellerId가 지정된 DTO
     */
    public static LegacyWebSellerQueryDto withSellerId(long sellerId) {
        return new LegacyWebSellerQueryDto(
                sellerId,
                DEFAULT_SELLER_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                DEFAULT_COMMISSION_RATE,
                DEFAULT_ADDRESS_LINE1,
                DEFAULT_ADDRESS_LINE2,
                DEFAULT_ZIP_CODE,
                DEFAULT_CS_NUMBER,
                DEFAULT_CS_PHONE,
                DEFAULT_CS_EMAIL,
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * sellerDescription이 null인 LegacyWebSellerQueryDto 생성.
     *
     * @return description이 null인 DTO
     */
    public static LegacyWebSellerQueryDto withNullDescription() {
        return new LegacyWebSellerQueryDto(
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_LOGO_URL,
                null,
                DEFAULT_COMMISSION_RATE,
                DEFAULT_ADDRESS_LINE1,
                DEFAULT_ADDRESS_LINE2,
                DEFAULT_ZIP_CODE,
                DEFAULT_CS_NUMBER,
                DEFAULT_CS_PHONE,
                DEFAULT_CS_EMAIL,
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * commissionRate가 null인 LegacyWebSellerQueryDto 생성.
     *
     * @return commissionRate가 null인 DTO
     */
    public static LegacyWebSellerQueryDto withNullCommissionRate() {
        return new LegacyWebSellerQueryDto(
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                null,
                DEFAULT_ADDRESS_LINE1,
                DEFAULT_ADDRESS_LINE2,
                DEFAULT_ZIP_CODE,
                DEFAULT_CS_NUMBER,
                DEFAULT_CS_PHONE,
                DEFAULT_CS_EMAIL,
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * csNumber가 null인 LegacyWebSellerQueryDto 생성.
     *
     * <p>csNumber가 null일 때 csPhoneNumber로 폴백하는 로직을 테스트하기 위해 사용합니다.
     *
     * @return csNumber가 null인 DTO
     */
    public static LegacyWebSellerQueryDto withNullCsNumber() {
        return new LegacyWebSellerQueryDto(
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                DEFAULT_COMMISSION_RATE,
                DEFAULT_ADDRESS_LINE1,
                DEFAULT_ADDRESS_LINE2,
                DEFAULT_ZIP_CODE,
                null,
                DEFAULT_CS_PHONE,
                DEFAULT_CS_EMAIL,
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * createdAt과 updatedAt이 null인 LegacyWebSellerQueryDto 생성.
     *
     * @return 날짜 필드가 null인 DTO
     */
    public static LegacyWebSellerQueryDto withNullDates() {
        return new LegacyWebSellerQueryDto(
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                DEFAULT_COMMISSION_RATE,
                DEFAULT_ADDRESS_LINE1,
                DEFAULT_ADDRESS_LINE2,
                DEFAULT_ZIP_CODE,
                DEFAULT_CS_NUMBER,
                DEFAULT_CS_PHONE,
                DEFAULT_CS_EMAIL,
                DEFAULT_REGISTRATION_NUMBER,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER,
                null,
                null);
    }

    /**
     * 지정된 사업자등록번호로 LegacyWebSellerQueryDto 생성.
     *
     * @param registrationNumber 사업자등록번호
     * @return registrationNumber가 지정된 DTO
     */
    public static LegacyWebSellerQueryDto withRegistrationNumber(String registrationNumber) {
        return new LegacyWebSellerQueryDto(
                DEFAULT_SELLER_ID,
                DEFAULT_SELLER_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_LOGO_URL,
                DEFAULT_SELLER_DESCRIPTION,
                DEFAULT_COMMISSION_RATE,
                DEFAULT_ADDRESS_LINE1,
                DEFAULT_ADDRESS_LINE2,
                DEFAULT_ZIP_CODE,
                DEFAULT_CS_NUMBER,
                DEFAULT_CS_PHONE,
                DEFAULT_CS_EMAIL,
                registrationNumber,
                DEFAULT_SALE_REPORT_NUMBER,
                DEFAULT_REPRESENTATIVE,
                DEFAULT_BANK_NAME,
                DEFAULT_ACCOUNT_NUMBER,
                DEFAULT_ACCOUNT_HOLDER,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }
}
