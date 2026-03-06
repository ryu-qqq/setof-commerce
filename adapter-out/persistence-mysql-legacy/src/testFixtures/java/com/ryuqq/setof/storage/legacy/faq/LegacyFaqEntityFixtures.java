package com.ryuqq.setof.storage.legacy.faq;

import com.ryuqq.setof.domain.faq.vo.FaqType;
import com.ryuqq.setof.storage.legacy.faq.entity.LegacyFaqEntity;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * LegacyFaqEntity 테스트 픽스처.
 *
 * <p>Faq 엔티티는 protected 생성자만 제공하므로 리플렉션 기반 빌더를 사용합니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * LegacyFaqEntity entity = LegacyFaqEntityFixtures.builder()
 *     .id(1L)
 *     .title("FAQ 제목")
 *     .build();
 * }</pre>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class LegacyFaqEntityFixtures {

    private LegacyFaqEntityFixtures() {}

    public static final Long DEFAULT_ID = 1L;
    public static final FaqType DEFAULT_FAQ_TYPE = FaqType.MEMBER_LOGIN;
    public static final String DEFAULT_TITLE = "테스트 FAQ 제목";
    public static final String DEFAULT_CONTENTS = "테스트 FAQ 내용입니다.";
    public static final Integer DEFAULT_DISPLAY_ORDER = 1;
    public static final Integer DEFAULT_TOP_DISPLAY_ORDER = 0;
    public static final LocalDateTime DEFAULT_INSERT_DATE = LocalDateTime.of(2026, 1, 1, 0, 0);
    public static final LocalDateTime DEFAULT_UPDATE_DATE = LocalDateTime.of(2026, 1, 1, 0, 0);

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id = DEFAULT_ID;
        private FaqType faqType = DEFAULT_FAQ_TYPE;
        private String title = DEFAULT_TITLE;
        private String contents = DEFAULT_CONTENTS;
        private Integer displayOrder = DEFAULT_DISPLAY_ORDER;
        private Integer topDisplayOrder = DEFAULT_TOP_DISPLAY_ORDER;
        private LocalDateTime insertDate = DEFAULT_INSERT_DATE;
        private LocalDateTime updateDate = DEFAULT_UPDATE_DATE;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder faqType(FaqType faqType) {
            this.faqType = faqType;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public Builder topDisplayOrder(Integer topDisplayOrder) {
            this.topDisplayOrder = topDisplayOrder;
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

        public LegacyFaqEntity build() {
            LegacyFaqEntity entity = newInstance();
            setField(entity, "id", id);
            setField(entity, "faqType", faqType);
            setField(entity, "title", title);
            setField(entity, "contents", contents);
            setField(entity, "displayOrder", displayOrder);
            setField(entity, "topDisplayOrder", topDisplayOrder);
            setField(entity, "insertDate", insertDate);
            setField(entity, "updateDate", updateDate);
            return entity;
        }

        private LegacyFaqEntity newInstance() {
            try {
                var constructor = LegacyFaqEntity.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create LegacyFaqEntity", e);
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
