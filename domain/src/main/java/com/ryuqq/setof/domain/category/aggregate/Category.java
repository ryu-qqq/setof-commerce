package com.ryuqq.setof.domain.category.aggregate;

import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import com.ryuqq.setof.domain.category.vo.CategoryStatus;
import java.time.Instant;

/**
 * Category Aggregate Root
 *
 * <p>카테고리 정보를 나타내는 도메인 엔티티입니다. MarketPlace에서 배치로 동기화되며, 이 프로젝트에서는 조회만 수행합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 *
 * <p>카테고리 계층구조:
 *
 * <ul>
 *   <li>parentId: 부모 카테고리 ID (null이면 최상위)
 *   <li>depth: 깊이 (0=최상위, 1=중분류, 2=소분류...)
 *   <li>path: Path Enumeration (예: "/1/5/23/")
 *   <li>isLeaf: 리프 노드 여부 (하위 카테고리 없음)
 * </ul>
 */
public class Category {

    private final CategoryId id;
    private final CategoryCode code;
    private final CategoryName nameKo;
    private final Long parentId;
    private final CategoryDepth depth;
    private final CategoryPath path;
    private final int sortOrder;
    private final boolean isLeaf;
    private final CategoryStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Category(
            CategoryId id,
            CategoryCode code,
            CategoryName nameKo,
            Long parentId,
            CategoryDepth depth,
            CategoryPath path,
            int sortOrder,
            boolean isLeaf,
            CategoryStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.nameKo = nameKo;
        this.parentId = parentId;
        this.depth = depth;
        this.path = path;
        this.sortOrder = sortOrder;
        this.isLeaf = isLeaf;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param parentId 부모 카테고리 ID (nullable - null이면 최상위)
     * @param depth 깊이
     * @param path 경로 (Path Enumeration)
     * @param sortOrder 정렬 순서
     * @param isLeaf 리프 노드 여부
     * @param status 카테고리 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return Category 인스턴스
     */
    public static Category reconstitute(
            CategoryId id,
            CategoryCode code,
            CategoryName nameKo,
            Long parentId,
            CategoryDepth depth,
            CategoryPath path,
            int sortOrder,
            boolean isLeaf,
            CategoryStatus status,
            Instant createdAt,
            Instant updatedAt) {
        return new Category(
                id, code, nameKo, parentId, depth, path, sortOrder, isLeaf, status, createdAt,
                updatedAt);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 카테고리 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 카테고리 ID Long 값
     */
    public Long getIdValue() {
        return id.value();
    }

    /**
     * 카테고리 코드 값 반환 (Law of Demeter 준수)
     *
     * @return 카테고리 코드 문자열
     */
    public String getCodeValue() {
        return code.value();
    }

    /**
     * 카테고리명 값 반환 (Law of Demeter 준수)
     *
     * @return 카테고리명 문자열
     */
    public String getNameKoValue() {
        return nameKo.value();
    }

    /**
     * 깊이 값 반환 (Law of Demeter 준수)
     *
     * @return 깊이 정수 값
     */
    public Integer getDepthValue() {
        return depth.value();
    }

    /**
     * 경로 값 반환 (Law of Demeter 준수)
     *
     * @return 경로 문자열
     */
    public String getPathValue() {
        return path.value();
    }

    /**
     * 카테고리 상태 이름 반환 (Law of Demeter 준수)
     *
     * @return 카테고리 상태 문자열
     */
    public String getStatusValue() {
        return status.name();
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 활성 상태 여부 확인 (Tell, Don't Ask)
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return status.isActive();
    }

    /**
     * 최상위 카테고리 여부 확인
     *
     * @return 최상위 카테고리이면 true (parentId가 null)
     */
    public boolean isRoot() {
        return parentId == null;
    }

    /**
     * 하위 카테고리 존재 여부 확인
     *
     * @return 하위 카테고리가 없으면 true (리프 노드)
     */
    public boolean isLeafNode() {
        return isLeaf;
    }

    /**
     * 부모 카테고리 존재 여부 확인
     *
     * @return 부모 카테고리가 있으면 true
     */
    public boolean hasParent() {
        return parentId != null;
    }

    /**
     * 특정 카테고리의 하위인지 확인
     *
     * @param parentCategoryId 상위 카테고리 ID
     * @return 하위 카테고리이면 true
     */
    public boolean isDescendantOf(Long parentCategoryId) {
        return path.contains(parentCategoryId);
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public CategoryId getId() {
        return id;
    }

    public CategoryCode getCode() {
        return code;
    }

    public CategoryName getNameKo() {
        return nameKo;
    }

    public Long getParentId() {
        return parentId;
    }

    public CategoryDepth getDepth() {
        return depth;
    }

    public CategoryPath getPath() {
        return path;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
