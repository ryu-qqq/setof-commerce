package com.ryuqq.setof.domain.category.aggregate;

import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryDisplayName;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import java.time.Instant;

/**
 * Category - 카테고리 Aggregate.
 *
 * <p>상품 카테고리 정보를 표현합니다. 계층 구조를 가지며 parentCategoryId로 트리를 구성합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>categoryName, displayName, path는 필수
 *   <li>categoryDepth는 1 이상
 *   <li>루트 카테고리의 parentCategoryId는 0
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class Category {

    private final CategoryId id;
    private final CategoryName categoryName;
    private final CategoryDepth categoryDepth;
    private final CategoryId parentCategoryId;
    private final CategoryDisplayName displayName;
    private boolean displayed;
    private final TargetGroup targetGroup;
    private final CategoryType categoryType;
    private final CategoryPath path;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private Category(
            CategoryId id,
            CategoryName categoryName,
            CategoryDepth categoryDepth,
            CategoryId parentCategoryId,
            CategoryDisplayName displayName,
            boolean displayed,
            TargetGroup targetGroup,
            CategoryType categoryType,
            CategoryPath path,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.parentCategoryId = parentCategoryId;
        this.displayName = displayName;
        this.displayed = displayed;
        this.targetGroup = targetGroup;
        this.categoryType = categoryType;
        this.path = path;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 카테고리 생성 (루트).
     *
     * @param categoryName 카테고리명 (필수)
     * @param displayName 표시명 (필수)
     * @param path 경로 (필수, 예: "/" - 영속화 후 실제 ID로 갱신)
     * @param targetGroup 타겟 그룹 (필수)
     * @param categoryType 카테고리 타입 (필수)
     * @param now 생성 시각
     * @return 새 Category 인스턴스 (displayed=true, depth=1)
     */
    public static Category forNewRoot(
            CategoryName categoryName,
            CategoryDisplayName displayName,
            CategoryPath path,
            TargetGroup targetGroup,
            CategoryType categoryType,
            Instant now) {
        return new Category(
                CategoryId.forNew(),
                categoryName,
                CategoryDepth.root(),
                CategoryId.of(0L),
                displayName,
                true,
                targetGroup,
                categoryType,
                path,
                DeletionStatus.active(),
                now,
                now);
    }

    /**
     * 새 카테고리 생성 (하위).
     *
     * @param categoryName 카테고리명 (필수)
     * @param displayName 표시명 (필수)
     * @param parentCategoryId 부모 카테고리 ID (필수)
     * @param categoryDepth 카테고리 깊이 (필수)
     * @param path 경로 (필수, 예: "/1/2/3" - 부모경로/신규ID)
     * @param targetGroup 타겟 그룹 (필수)
     * @param categoryType 카테고리 타입 (필수)
     * @param now 생성 시각
     * @return 새 Category 인스턴스 (displayed=true)
     */
    public static Category forNewChild(
            CategoryName categoryName,
            CategoryDisplayName displayName,
            CategoryId parentCategoryId,
            CategoryDepth categoryDepth,
            CategoryPath path,
            TargetGroup targetGroup,
            CategoryType categoryType,
            Instant now) {
        return new Category(
                CategoryId.forNew(),
                categoryName,
                categoryDepth,
                parentCategoryId,
                displayName,
                true,
                targetGroup,
                categoryType,
                path,
                DeletionStatus.active(),
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param categoryName 카테고리명
     * @param categoryDepth 카테고리 깊이
     * @param parentCategoryId 부모 카테고리 ID
     * @param displayName 표시명
     * @param displayed 표시 여부
     * @param targetGroup 타겟 그룹
     * @param categoryType 카테고리 타입
     * @param path 경로
     * @param deletedAt 삭제 시각 (null이면 활성 상태)
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 Category 인스턴스
     */
    public static Category reconstitute(
            CategoryId id,
            CategoryName categoryName,
            CategoryDepth categoryDepth,
            CategoryId parentCategoryId,
            CategoryDisplayName displayName,
            boolean displayed,
            TargetGroup targetGroup,
            CategoryType categoryType,
            CategoryPath path,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        DeletionStatus status =
                deletedAt != null ? DeletionStatus.deletedAt(deletedAt) : DeletionStatus.active();
        return new Category(
                id,
                categoryName,
                categoryDepth,
                parentCategoryId,
                displayName,
                displayed,
                targetGroup,
                categoryType,
                path,
                status,
                createdAt,
                updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /** 루트 카테고리 여부 확인 */
    public boolean isRoot() {
        return parentCategoryId != null && parentCategoryId.value() == 0L;
    }

    /**
     * 카테고리 정보 수정.
     *
     * @param data 수정할 데이터 번들
     * @param now 수정 시각
     */
    public void update(CategoryUpdateData data, Instant now) {
        this.displayed = data.displayed();
        this.updatedAt = now;
    }

    /** 표시 활성화 */
    public void show(Instant now) {
        this.displayed = true;
        this.updatedAt = now;
    }

    /** 표시 비활성화 */
    public void hide(Instant now) {
        this.displayed = false;
        this.updatedAt = now;
    }

    /**
     * 카테고리 삭제 (Soft Delete).
     *
     * @param now 삭제 발생 시각
     */
    public void delete(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.updatedAt = now;
    }

    /**
     * 삭제된 카테고리 복원.
     *
     * @param now 복원 시각
     */
    public void restore(Instant now) {
        this.deletionStatus = DeletionStatus.active();
        this.updatedAt = now;
    }

    // ========== Accessor Methods ==========

    public CategoryId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public CategoryName categoryName() {
        return categoryName;
    }

    public String categoryNameValue() {
        return categoryName.value();
    }

    public CategoryDepth categoryDepth() {
        return categoryDepth;
    }

    public int categoryDepthValue() {
        return categoryDepth.value();
    }

    public CategoryId parentCategoryId() {
        return parentCategoryId;
    }

    public Long parentCategoryIdValue() {
        return parentCategoryId != null ? parentCategoryId.value() : 0L;
    }

    public CategoryDisplayName displayName() {
        return displayName;
    }

    public String displayNameValue() {
        return displayName.value();
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public TargetGroup targetGroup() {
        return targetGroup;
    }

    public CategoryType categoryType() {
        return categoryType;
    }

    public CategoryPath path() {
        return path;
    }

    public String pathValue() {
        return path.value();
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public Instant deletedAt() {
        return deletionStatus.deletedAt();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
