package com.ryuqq.setof.storage.legacy.category;

import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * LegacyCategoryEntityFixtures - 레거시 카테고리 엔티티 테스트 픽스처.
 *
 * <p>리플렉션 기반 빌더 패턴으로 테스트용 LegacyCategoryEntity를 생성합니다.
 *
 * <p>PER-TST-001: testFixtures는 빌더 패턴 사용.
 *
 * <p>PER-TST-002: 리플렉션으로 protected 필드 설정.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class LegacyCategoryEntityFixtures {

    private LegacyCategoryEntityFixtures() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id = 1L;
        private String categoryName = "테스트 카테고리";
        private int categoryDepth = 1;
        private long parentCategoryId = 0L;
        private String displayName = "테스트 카테고리 표시명";
        private Yn displayYn = Yn.Y;
        private TargetGroup targetGroup = TargetGroup.MALE;
        private CategoryType categoryType = CategoryType.CLOTHING;
        private String path = "/1";
        private LocalDateTime insertDate = LocalDateTime.now();
        private LocalDateTime updateDate = LocalDateTime.now();
        private Yn deleteYn = Yn.N;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder categoryDepth(int categoryDepth) {
            this.categoryDepth = categoryDepth;
            return this;
        }

        public Builder parentCategoryId(long parentCategoryId) {
            this.parentCategoryId = parentCategoryId;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder displayYn(Yn displayYn) {
            this.displayYn = displayYn;
            return this;
        }

        public Builder displayYn(String yn) {
            this.displayYn = Yn.valueOf(yn);
            return this;
        }

        public Builder targetGroup(TargetGroup targetGroup) {
            this.targetGroup = targetGroup;
            return this;
        }

        public Builder categoryType(CategoryType categoryType) {
            this.categoryType = categoryType;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
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

        public Builder deleteYn(Yn deleteYn) {
            this.deleteYn = deleteYn;
            return this;
        }

        public Builder deleteYn(String yn) {
            this.deleteYn = Yn.valueOf(yn);
            return this;
        }

        public LegacyCategoryEntity build() {
            LegacyCategoryEntity entity = createEntityInstance();
            setField(entity, "id", id);
            setField(entity, "categoryName", categoryName);
            setField(entity, "categoryDepth", categoryDepth);
            setField(entity, "parentCategoryId", parentCategoryId);
            setField(entity, "displayName", displayName);
            setField(entity, "displayYn", displayYn);
            setField(entity, "targetGroup", targetGroup);
            setField(entity, "categoryType", categoryType);
            setField(entity, "path", path);
            setField(entity, "insertDate", insertDate);
            setField(entity, "updateDate", updateDate);
            setFieldIfExists(entity, "deleteYn", deleteYn);
            return entity;
        }

        private LegacyCategoryEntity createEntityInstance() {
            try {
                var constructor = LegacyCategoryEntity.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("엔티티 인스턴스 생성 실패", e);
            }
        }

        private void setField(Object target, String fieldName, Object value) {
            try {
                Field field = findField(target.getClass(), fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception e) {
                throw new IllegalStateException("필드 설정 실패: " + fieldName, e);
            }
        }

        private void setFieldIfExists(Object target, String fieldName, Object value) {
            try {
                Field field = findField(target.getClass(), fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (NoSuchFieldException e) {
                // 필드가 없으면 무시
            } catch (Exception e) {
                throw new IllegalStateException("필드 설정 실패: " + fieldName, e);
            }
        }

        private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
            Class<?> current = clazz;
            while (current != null) {
                try {
                    return current.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    current = current.getSuperclass();
                }
            }
            throw new NoSuchFieldException(fieldName);
        }
    }
}
