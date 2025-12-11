package com.ryuqq.setof.domain.category;

import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryDepth;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryName;
import com.ryuqq.setof.domain.category.vo.CategoryPath;
import com.ryuqq.setof.domain.category.vo.CategoryStatus;
import java.time.Instant;
import java.util.List;

/**
 * Category TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Category 인스턴스 생성을 위한 팩토리 클래스
 *
 * <p>Category.reconstitute 시그니처: (CategoryId, CategoryCode, CategoryName, Long parentId,
 * CategoryDepth, CategoryPath, int sortOrder, boolean isLeaf, CategoryStatus, Instant createdAt,
 * Instant updatedAt)
 */
public final class CategoryFixture {

    private static final Instant DEFAULT_CREATED_AT = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant DEFAULT_UPDATED_AT = Instant.parse("2024-01-01T00:00:00Z");

    private CategoryFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 최상위 카테고리 생성 (패션)
     *
     * @return Category 인스턴스
     */
    public static Category createRoot() {
        return Category.reconstitute(
                CategoryId.of(1L),
                CategoryCode.of("FASHION"),
                CategoryName.of("패션"),
                null,
                CategoryDepth.of(0),
                CategoryPath.of("/1/"),
                1,
                false,
                CategoryStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 중분류 카테고리 생성 (의류)
     *
     * @return Category 인스턴스
     */
    public static Category createMiddle() {
        return Category.reconstitute(
                CategoryId.of(5L),
                CategoryCode.of("CLOTHING"),
                CategoryName.of("의류"),
                1L,
                CategoryDepth.of(1),
                CategoryPath.of("/1/5/"),
                1,
                false,
                CategoryStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 소분류 카테고리 생성 (상의) - leaf 노드
     *
     * @return Category 인스턴스
     */
    public static Category createSmall() {
        return Category.reconstitute(
                CategoryId.of(23L),
                CategoryCode.of("TOPS"),
                CategoryName.of("상의"),
                5L,
                CategoryDepth.of(2),
                CategoryPath.of("/1/5/23/"),
                1,
                true,
                CategoryStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * ID 지정하여 카테고리 생성
     *
     * @param id 카테고리 ID
     * @return Category 인스턴스
     */
    public static Category createWithId(Long id) {
        return Category.reconstitute(
                CategoryId.of(id),
                CategoryCode.of("CAT" + id),
                CategoryName.of("카테고리" + id),
                null,
                CategoryDepth.of(0),
                CategoryPath.of("/" + id + "/"),
                1,
                false,
                CategoryStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 비활성 카테고리 생성
     *
     * @return Category 인스턴스 (비활성)
     */
    public static Category createInactive() {
        return Category.reconstitute(
                CategoryId.of(99L),
                CategoryCode.of("INACTIVE"),
                CategoryName.of("비활성카테고리"),
                null,
                CategoryDepth.of(0),
                CategoryPath.of("/99/"),
                99,
                true,
                CategoryStatus.INACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 여러 카테고리 목록 생성 (최상위 카테고리 목록)
     *
     * @return Category 목록
     */
    public static List<Category> createList() {
        return List.of(
                Category.reconstitute(
                        CategoryId.of(1L),
                        CategoryCode.of("FASHION"),
                        CategoryName.of("패션"),
                        null,
                        CategoryDepth.of(0),
                        CategoryPath.of("/1/"),
                        1,
                        false,
                        CategoryStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT),
                Category.reconstitute(
                        CategoryId.of(2L),
                        CategoryCode.of("ELECTRONICS"),
                        CategoryName.of("전자제품"),
                        null,
                        CategoryDepth.of(0),
                        CategoryPath.of("/2/"),
                        2,
                        false,
                        CategoryStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT),
                Category.reconstitute(
                        CategoryId.of(3L),
                        CategoryCode.of("HOME"),
                        CategoryName.of("가구/인테리어"),
                        null,
                        CategoryDepth.of(0),
                        CategoryPath.of("/3/"),
                        3,
                        false,
                        CategoryStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT));
    }

    /**
     * 계층 구조를 가진 카테고리 목록 생성 (패션 > 의류 > 상의)
     *
     * @return Category 목록 (계층 구조)
     */
    public static List<Category> createHierarchy() {
        return List.of(
                Category.reconstitute(
                        CategoryId.of(1L),
                        CategoryCode.of("FASHION"),
                        CategoryName.of("패션"),
                        null,
                        CategoryDepth.of(0),
                        CategoryPath.of("/1/"),
                        1,
                        false,
                        CategoryStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT),
                Category.reconstitute(
                        CategoryId.of(5L),
                        CategoryCode.of("CLOTHING"),
                        CategoryName.of("의류"),
                        1L,
                        CategoryDepth.of(1),
                        CategoryPath.of("/1/5/"),
                        1,
                        false,
                        CategoryStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT),
                Category.reconstitute(
                        CategoryId.of(23L),
                        CategoryCode.of("TOPS"),
                        CategoryName.of("상의"),
                        5L,
                        CategoryDepth.of(2),
                        CategoryPath.of("/1/5/23/"),
                        1,
                        true,
                        CategoryStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT));
    }

    /**
     * 커스텀 카테고리 생성
     *
     * @param id 카테고리 ID
     * @param parentId 부모 카테고리 ID (null 가능)
     * @param code 카테고리 코드
     * @param name 카테고리명
     * @param path 경로 (Path Enumeration)
     * @param depth 깊이
     * @param sortOrder 정렬 순서
     * @param isLeaf 리프 노드 여부
     * @param active 활성 상태
     * @return Category 인스턴스
     */
    public static Category createCustom(
            Long id,
            Long parentId,
            String code,
            String name,
            String path,
            int depth,
            int sortOrder,
            boolean isLeaf,
            boolean active) {
        return Category.reconstitute(
                CategoryId.of(id),
                CategoryCode.of(code),
                CategoryName.of(name),
                parentId,
                CategoryDepth.of(depth),
                CategoryPath.of(path),
                sortOrder,
                isLeaf,
                active ? CategoryStatus.ACTIVE : CategoryStatus.INACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }
}
