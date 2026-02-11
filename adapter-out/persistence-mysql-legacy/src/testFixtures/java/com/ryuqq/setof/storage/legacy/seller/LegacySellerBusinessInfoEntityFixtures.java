package com.ryuqq.setof.storage.legacy.seller;

import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerBusinessInfoEntity;
import java.lang.reflect.Field;

/**
 * LegacySellerBusinessInfoEntity 테스트 픽스처.
 *
 * <p>SellerBusinessInfo 엔티티는 protected 생성자만 제공하므로 리플렉션 기반 빌더를 사용합니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * LegacySellerBusinessInfoEntity entity = LegacySellerBusinessInfoEntityFixtures.builder()
 *     .id(1L)
 *     .registrationNumber("123-45-67890")
 *     .build();
 * }</pre>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class LegacySellerBusinessInfoEntityFixtures {

    private LegacySellerBusinessInfoEntityFixtures() {}

    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_REGISTRATION_NUMBER = "123-45-67890";
    public static final String DEFAULT_COMPANY_NAME = "테스트 회사";
    public static final String DEFAULT_BUSINESS_ADDRESS_LINE1 = "서울특별시 강남구 테헤란로 123";
    public static final String DEFAULT_BUSINESS_ADDRESS_LINE2 = "테스트빌딩 4층";
    public static final String DEFAULT_BUSINESS_ADDRESS_ZIP_CODE = "06234";
    public static final String DEFAULT_BANK_NAME = "신한은행";
    public static final String DEFAULT_ACCOUNT_NUMBER = "110-123-456789";
    public static final String DEFAULT_ACCOUNT_HOLDER_NAME = "테스트회사";
    public static final String DEFAULT_CS_NUMBER = "1234-5678";
    public static final String DEFAULT_CS_PHONE_NUMBER = "02-1234-5678";
    public static final String DEFAULT_CS_EMAIL = "cs@test.com";
    public static final String DEFAULT_SALE_REPORT_NUMBER = "2021-서울강남-1234";
    public static final String DEFAULT_REPRESENTATIVE = "홍길동";

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id = DEFAULT_ID;
        private String registrationNumber = DEFAULT_REGISTRATION_NUMBER;
        private String companyName = DEFAULT_COMPANY_NAME;
        private String businessAddressLine1 = DEFAULT_BUSINESS_ADDRESS_LINE1;
        private String businessAddressLine2 = DEFAULT_BUSINESS_ADDRESS_LINE2;
        private String businessAddressZipCode = DEFAULT_BUSINESS_ADDRESS_ZIP_CODE;
        private String bankName = DEFAULT_BANK_NAME;
        private String accountNumber = DEFAULT_ACCOUNT_NUMBER;
        private String accountHolderName = DEFAULT_ACCOUNT_HOLDER_NAME;
        private String csNumber = DEFAULT_CS_NUMBER;
        private String csPhoneNumber = DEFAULT_CS_PHONE_NUMBER;
        private String csEmail = DEFAULT_CS_EMAIL;
        private String saleReportNumber = DEFAULT_SALE_REPORT_NUMBER;
        private String representative = DEFAULT_REPRESENTATIVE;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder businessAddressLine1(String businessAddressLine1) {
            this.businessAddressLine1 = businessAddressLine1;
            return this;
        }

        public Builder businessAddressLine2(String businessAddressLine2) {
            this.businessAddressLine2 = businessAddressLine2;
            return this;
        }

        public Builder businessAddressZipCode(String businessAddressZipCode) {
            this.businessAddressZipCode = businessAddressZipCode;
            return this;
        }

        public Builder bankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder accountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
            return this;
        }

        public Builder csNumber(String csNumber) {
            this.csNumber = csNumber;
            return this;
        }

        public Builder csPhoneNumber(String csPhoneNumber) {
            this.csPhoneNumber = csPhoneNumber;
            return this;
        }

        public Builder csEmail(String csEmail) {
            this.csEmail = csEmail;
            return this;
        }

        public Builder saleReportNumber(String saleReportNumber) {
            this.saleReportNumber = saleReportNumber;
            return this;
        }

        public Builder representative(String representative) {
            this.representative = representative;
            return this;
        }

        public LegacySellerBusinessInfoEntity build() {
            LegacySellerBusinessInfoEntity entity = newInstance();
            setField(entity, "id", id);
            setField(entity, "registrationNumber", registrationNumber);
            setField(entity, "companyName", companyName);
            setField(entity, "businessAddressLine1", businessAddressLine1);
            setField(entity, "businessAddressLine2", businessAddressLine2);
            setField(entity, "businessAddressZipCode", businessAddressZipCode);
            setField(entity, "bankName", bankName);
            setField(entity, "accountNumber", accountNumber);
            setField(entity, "accountHolderName", accountHolderName);
            setField(entity, "csNumber", csNumber);
            setField(entity, "csPhoneNumber", csPhoneNumber);
            setField(entity, "csEmail", csEmail);
            setField(entity, "saleReportNumber", saleReportNumber);
            setField(entity, "representative", representative);
            return entity;
        }

        private LegacySellerBusinessInfoEntity newInstance() {
            try {
                var constructor = LegacySellerBusinessInfoEntity.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create LegacySellerBusinessInfoEntity", e);
            }
        }

        private void setField(Object target, String fieldName, Object value) {
            try {
                Field field = findField(target.getClass(), fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set field: " + fieldName, e);
            }
        }

        private Field findField(Class<?> clazz, String fieldName) {
            Class<?> current = clazz;
            while (current != null) {
                try {
                    return current.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    current = current.getSuperclass();
                }
            }
            throw new RuntimeException("Field not found: " + fieldName);
        }
    }
}
