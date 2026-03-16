package com.ryuqq.setof.storage.legacy.brand;

import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity.MainDisplayNameType;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * LegacyBrandEntityFixtures - 레거시 브랜드 엔티티 테스트 픽스처.
 *
 * <p>리플렉션 기반 빌더 패턴으로 테스트용 엔티티를 생성합니다.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * LegacyBrandEntity entity = LegacyBrandEntityFixtures.builder()
 *     .id(null)
 *     .brandName("나이키")
 *     .displayKoreanName("나이키 코리아")
 *     .build();
 * }</pre>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class LegacyBrandEntityFixtures {

    public static final String DEFAULT_BRAND_NAME = "테스트 브랜드";
    public static final String DEFAULT_BRAND_ICON_IMAGE_URL = "https://example.com/icon.png";
    public static final String DEFAULT_DISPLAY_KOREAN_NAME = "테스트 브랜드 한국어";
    public static final String DEFAULT_DISPLAY_ENGLISH_NAME = "Test Brand English";
    public static final MainDisplayNameType DEFAULT_MAIN_DISPLAY_TYPE = MainDisplayNameType.KR;
    public static final int DEFAULT_DISPLAY_ORDER = 1;
    public static final Yn DEFAULT_DISPLAY_YN = Yn.Y;

    /**
     * 기본 값으로 초기화된 빌더 생성.
     *
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder 클래스 */
    public static class Builder {
        private Long id = 1L;
        private String brandName = DEFAULT_BRAND_NAME;
        private String brandIconImageUrl = DEFAULT_BRAND_ICON_IMAGE_URL;
        private String displayEnglishName = DEFAULT_DISPLAY_ENGLISH_NAME;
        private String displayKoreanName = DEFAULT_DISPLAY_KOREAN_NAME;
        private MainDisplayNameType mainDisplayType = DEFAULT_MAIN_DISPLAY_TYPE;
        private int displayOrder = DEFAULT_DISPLAY_ORDER;
        private Yn displayYn = DEFAULT_DISPLAY_YN;
        private LocalDateTime insertDate = LocalDateTime.now();
        private LocalDateTime updateDate = LocalDateTime.now();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder brandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public Builder brandIconImageUrl(String brandIconImageUrl) {
            this.brandIconImageUrl = brandIconImageUrl;
            return this;
        }

        public Builder displayEnglishName(String displayEnglishName) {
            this.displayEnglishName = displayEnglishName;
            return this;
        }

        public Builder displayKoreanName(String displayKoreanName) {
            this.displayKoreanName = displayKoreanName;
            return this;
        }

        public Builder mainDisplayType(MainDisplayNameType mainDisplayType) {
            this.mainDisplayType = mainDisplayType;
            return this;
        }

        public Builder displayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public Builder displayYn(String yn) {
            this.displayYn = Yn.valueOf(yn);
            return this;
        }

        public Builder displayYn(Yn yn) {
            this.displayYn = yn;
            return this;
        }

        public Builder insertDate(LocalDateTime insertDate) {
            this.insertDate = insertDate;
            return this;
        }

        public Builder updateDate(LocalDateTime updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        /**
         * deleteYn 메서드 (레거시 호환용).
         *
         * <p>LegacyBrandEntity에는 deleteYn 필드가 없지만, 통합 테스트에서 사용하므로 제공합니다.
         *
         * <p>이 메서드는 아무 동작도 하지 않습니다 (무시됨).
         *
         * @param deleteYn 삭제 여부 (무시됨)
         * @return Builder
         */
        public Builder deleteYn(String deleteYn) {
            // LegacyBrandEntity에는 deleteYn 필드가 없으므로 무시
            return this;
        }

        /**
         * 리플렉션 기반 엔티티 생성.
         *
         * @return LegacyBrandEntity
         */
        public LegacyBrandEntity build() {
            try {
                var constructor = LegacyBrandEntity.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                LegacyBrandEntity entity = constructor.newInstance();

                setField(entity, "id", id);
                setField(entity, "brandName", brandName);
                setField(entity, "brandIconImageUrl", brandIconImageUrl);
                setField(entity, "displayEnglishName", displayEnglishName);
                setField(entity, "displayKoreanName", displayKoreanName);
                setField(entity, "mainDisplayType", mainDisplayType);
                setField(entity, "displayOrder", displayOrder);
                setField(entity, "displayYn", displayYn);

                // LegacyBaseEntity 필드 설정
                setField(entity, "insertDate", insertDate);
                setField(entity, "updateDate", updateDate);

                return entity;
            } catch (Exception e) {
                throw new RuntimeException("LegacyBrandEntity 생성 실패", e);
            }
        }

        private void setField(Object target, String fieldName, Object value) throws Exception {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        }

        private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                if (clazz.getSuperclass() != null) {
                    return findField(clazz.getSuperclass(), fieldName);
                }
                throw e;
            }
        }
    }
}
