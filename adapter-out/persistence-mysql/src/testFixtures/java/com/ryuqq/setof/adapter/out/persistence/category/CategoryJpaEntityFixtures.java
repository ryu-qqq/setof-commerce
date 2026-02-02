package com.ryuqq.setof.adapter.out.persistence.category;

import com.ryuqq.setof.adapter.out.persistence.category.dto.CategoryTreeDto;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import java.time.Instant;

/**
 * CategoryJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 CategoryJpaEntity 관련 객체들을 생성합니다.
 */
public final class CategoryJpaEntityFixtures {

    private CategoryJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_CATEGORY_NAME = "테스트카테고리";
    public static final String DEFAULT_DISPLAY_NAME = "테스트 카테고리 표시명";
    public static final int DEFAULT_DEPTH = 1;
    public static final Long DEFAULT_PARENT_ID = 0L;
    public static final String DEFAULT_PATH = "/1";

    // ===== Entity Fixtures =====

    /** 활성 상태의 카테고리 Entity 생성. */
    public static CategoryJpaEntity activeEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                DEFAULT_PATH,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 상태 카테고리 Entity 생성. */
    public static CategoryJpaEntity activeEntity(Long id) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                id,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                "/" + id,
                now,
                now,
                null);
    }

    /** 자식 카테고리 Entity 생성. */
    public static CategoryJpaEntity childEntity(Long id, Long parentId, int depth) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                id,
                DEFAULT_CATEGORY_NAME,
                depth,
                parentId,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                "/" + parentId + "/" + id,
                now,
                now,
                null);
    }

    /** 커스텀 카테고리명을 가진 활성 상태 카테고리 Entity 생성. */
    public static CategoryJpaEntity activeEntityWithName(String categoryName, String displayName) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                categoryName,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                displayName,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                DEFAULT_PATH,
                now,
                now,
                null);
    }

    /** 비활성 상태 카테고리 Entity 생성. */
    public static CategoryJpaEntity inactiveEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                2L,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                false,
                TargetGroup.FEMALE,
                CategoryType.BAG,
                "/2",
                now,
                now,
                null);
    }

    /** 삭제된 상태 카테고리 Entity 생성. */
    public static CategoryJpaEntity deletedEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                3L,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                false,
                TargetGroup.KIDS,
                CategoryType.ACC,
                "/3",
                now,
                now,
                now);
    }

    /** 새로 생성될 Entity (ID가 null). */
    public static CategoryJpaEntity newEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                DEFAULT_PATH,
                now,
                now,
                null);
    }

    /** 루트 카테고리 Entity 생성 (depth = 1, parentId = 0). */
    public static CategoryJpaEntity rootEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_CATEGORY_NAME,
                1,
                0L,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                "/" + DEFAULT_ID,
                now,
                now,
                null);
    }

    /** 특정 경로를 가진 Entity 생성. */
    public static CategoryJpaEntity entityWithPath(String path) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                path,
                now,
                now,
                null);
    }

    /** 특정 TargetGroup과 CategoryType을 가진 Entity 생성. */
    public static CategoryJpaEntity entityWithTargetAndType(
            TargetGroup targetGroup, CategoryType categoryType) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                DEFAULT_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                true,
                targetGroup,
                categoryType,
                DEFAULT_PATH,
                now,
                now,
                null);
    }

    // ===== CategoryTreeDto Fixtures =====

    /** 활성 상태의 CategoryTreeDto 생성. */
    public static CategoryTreeDto activeTreeDto() {
        Instant now = Instant.now();
        return new CategoryTreeDto(
                DEFAULT_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                DEFAULT_PATH,
                now,
                now,
                null);
    }

    /** ID를 지정한 CategoryTreeDto 생성. */
    public static CategoryTreeDto treeDto(Long id, Long parentId, int depth) {
        Instant now = Instant.now();
        return new CategoryTreeDto(
                id,
                DEFAULT_CATEGORY_NAME,
                depth,
                parentId,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                "/" + parentId + "/" + id,
                now,
                now,
                null);
    }

    // ===== 새로운 Entity Fixtures (ID가 null로 저장 가능) =====

    /** 비활성 상태의 새 Entity 생성 (ID는 null). */
    public static CategoryJpaEntity newInactiveEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                false,
                TargetGroup.FEMALE,
                CategoryType.BAG,
                DEFAULT_PATH,
                now,
                now,
                null);
    }

    /** 삭제된 상태의 새 Entity 생성 (ID는 null). */
    public static CategoryJpaEntity newDeletedEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                false,
                TargetGroup.KIDS,
                CategoryType.ACC,
                DEFAULT_PATH,
                now,
                now,
                now);
    }

    /** 루트 카테고리 새 Entity 생성 (ID가 null). */
    public static CategoryJpaEntity newRootEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                DEFAULT_CATEGORY_NAME,
                1,
                0L,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                DEFAULT_PATH,
                now,
                now,
                null);
    }

    /** 자식 카테고리 새 Entity 생성 (ID가 null). */
    public static CategoryJpaEntity newChildEntity(Long parentId, int depth) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                DEFAULT_CATEGORY_NAME,
                depth,
                parentId,
                DEFAULT_DISPLAY_NAME,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                "/" + parentId,
                now,
                now,
                null);
    }

    /** 특정 TargetGroup과 CategoryType을 가진 새 Entity 생성 (ID가 null). */
    public static CategoryJpaEntity newEntityWithTargetAndType(
            TargetGroup targetGroup, CategoryType categoryType) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                DEFAULT_DISPLAY_NAME,
                true,
                targetGroup,
                categoryType,
                DEFAULT_PATH,
                now,
                now,
                null);
    }
}
