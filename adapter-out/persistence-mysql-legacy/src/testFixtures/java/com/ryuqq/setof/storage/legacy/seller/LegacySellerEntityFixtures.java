package com.ryuqq.setof.storage.legacy.seller;

import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * LegacySellerEntity 테스트 픽스처.
 *
 * <p>Seller 엔티티는 protected 생성자만 제공하므로 리플렉션 기반 빌더를 사용합니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * LegacySellerEntity entity = LegacySellerEntityFixtures.builder()
 *     .id(1L)
 *     .sellerName("테스트 셀러")
 *     .build();
 * }</pre>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class LegacySellerEntityFixtures {

    private LegacySellerEntityFixtures() {}

    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_SELLER_NAME = "테스트 셀러";
    public static final String DEFAULT_SELLER_LOGO_URL = "https://example.com/logo.png";
    public static final String DEFAULT_SELLER_DESCRIPTION = "테스트 셀러 설명";
    public static final Double DEFAULT_COMMISSION_RATE = 10.0;
    public static final LocalDateTime DEFAULT_CREATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0);
    public static final LocalDateTime DEFAULT_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 0, 0);

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id = DEFAULT_ID;
        private String sellerName = DEFAULT_SELLER_NAME;
        private String sellerLogoUrl = DEFAULT_SELLER_LOGO_URL;
        private String sellerDescription = DEFAULT_SELLER_DESCRIPTION;
        private Double commissionRate = DEFAULT_COMMISSION_RATE;
        private LocalDateTime createdAt = DEFAULT_CREATED_AT;
        private LocalDateTime updatedAt = DEFAULT_UPDATED_AT;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder sellerName(String sellerName) {
            this.sellerName = sellerName;
            return this;
        }

        public Builder sellerLogoUrl(String sellerLogoUrl) {
            this.sellerLogoUrl = sellerLogoUrl;
            return this;
        }

        public Builder sellerDescription(String sellerDescription) {
            this.sellerDescription = sellerDescription;
            return this;
        }

        public Builder commissionRate(Double commissionRate) {
            this.commissionRate = commissionRate;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public LegacySellerEntity build() {
            LegacySellerEntity entity = newInstance();
            setField(entity, "id", id);
            setField(entity, "sellerName", sellerName);
            setField(entity, "sellerLogoUrl", sellerLogoUrl);
            setField(entity, "sellerDescription", sellerDescription);
            setField(entity, "commissionRate", commissionRate);
            setField(entity, "insertDate", createdAt);
            setField(entity, "updateDate", updatedAt);
            return entity;
        }

        private LegacySellerEntity newInstance() {
            try {
                var constructor = LegacySellerEntity.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create LegacySellerEntity", e);
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
