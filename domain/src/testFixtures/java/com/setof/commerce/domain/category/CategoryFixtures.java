package com.ryuqq.setof.domain.category;

import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.aggregate.CategoryUpdateData;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryDisplayName;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import java.time.Instant;

/**
 * Category 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Category 관련 객체들을 생성합니다.
 */
public final class CategoryFixtures {

    private CategoryFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_CATEGORY_NAME = "테스트카테고리";
    public static final String DEFAULT_DISPLAY_NAME = "테스트 카테고리 표시명";
    public static final String DEFAULT_PATH = "/1";

    // ===== Category Aggregate Fixtures =====
    public static Category newRootCategory() {
        return Category.forNewRoot(
                defaultCategoryName(),
                defaultCategoryDisplayName(),
                CategoryPath.of("/"),
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CommonVoFixtures.now());
    }

    public static Category newRootCategory(String categoryName, String displayName) {
        return Category.forNewRoot(
                CategoryName.of(categoryName),
                CategoryDisplayName.of(displayName),
                CategoryPath.of("/"),
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CommonVoFixtures.now());
    }

    public static Category newChildCategory(CategoryId parentId, CategoryDepth depth, String path) {
        return Category.forNewChild(
                defaultCategoryName(),
                defaultCategoryDisplayName(),
                parentId,
                depth,
                CategoryPath.of(path),
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CommonVoFixtures.now());
    }

    public static Category activeCategory() {
        return Category.reconstitute(
                CategoryId.of(1L),
                defaultCategoryName(),
                CategoryDepth.root(),
                CategoryId.of(0L),
                defaultCategoryDisplayName(),
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CategoryPath.of("/1"),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Category activeCategory(Long id) {
        return Category.reconstitute(
                CategoryId.of(id),
                defaultCategoryName(),
                CategoryDepth.root(),
                CategoryId.of(0L),
                defaultCategoryDisplayName(),
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CategoryPath.of("/" + id),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Category activeChildCategory(Long id, Long parentId) {
        return Category.reconstitute(
                CategoryId.of(id),
                defaultCategoryName(),
                CategoryDepth.of(2),
                CategoryId.of(parentId),
                defaultCategoryDisplayName(),
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CategoryPath.of("/" + parentId + "/" + id),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Category inactiveCategory() {
        return Category.reconstitute(
                CategoryId.of(2L),
                defaultCategoryName(),
                CategoryDepth.root(),
                CategoryId.of(0L),
                defaultCategoryDisplayName(),
                false,
                TargetGroup.FEMALE,
                CategoryType.BAG,
                CategoryPath.of("/2"),
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Category deletedCategory() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return Category.reconstitute(
                CategoryId.of(3L),
                defaultCategoryName(),
                CategoryDepth.root(),
                CategoryId.of(0L),
                defaultCategoryDisplayName(),
                false,
                TargetGroup.KIDS,
                CategoryType.ACC,
                CategoryPath.of("/3"),
                deletedAt,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== VO Fixtures =====
    public static CategoryName defaultCategoryName() {
        return CategoryName.of(DEFAULT_CATEGORY_NAME);
    }

    public static CategoryDisplayName defaultCategoryDisplayName() {
        return CategoryDisplayName.of(DEFAULT_DISPLAY_NAME);
    }

    public static CategoryPath defaultCategoryPath() {
        return CategoryPath.of(DEFAULT_PATH);
    }

    // ===== CategoryUpdateData Fixtures =====
    public static CategoryUpdateData categoryUpdateData() {
        return CategoryUpdateData.of(true);
    }

    public static CategoryUpdateData categoryUpdateData(boolean displayed) {
        return CategoryUpdateData.of(displayed);
    }
}
