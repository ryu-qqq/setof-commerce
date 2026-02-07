package com.ryuqq.setof.storage.legacy.composite.web.category.adapter;

import com.ryuqq.setof.application.legacy.category.dto.response.LegacyCategoryResult;
import com.ryuqq.setof.application.legacy.category.dto.response.LegacyTreeCategoryResult;
import com.ryuqq.setof.domain.legacy.category.dto.query.LegacyCategorySearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.category.dto.LegacyWebCategoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.category.dto.LegacyWebTreeCategoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.category.mapper.LegacyWebCategoryMapper;
import com.ryuqq.setof.storage.legacy.composite.web.category.repository.LegacyWebCategoryCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.composite.web.category.repository.LegacyWebCategoryHierarchyRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * LegacyWebCategoryCompositeQueryAdapter - 레거시 Web 카테고리 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyWebCategoryCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebCategoryCompositeQueryAdapter {

    private final LegacyWebCategoryCompositeQueryDslRepository repository;
    private final LegacyWebCategoryHierarchyRepository hierarchyRepository;
    private final LegacyWebCategoryMapper mapper;

    public LegacyWebCategoryCompositeQueryAdapter(
            LegacyWebCategoryCompositeQueryDslRepository repository,
            LegacyWebCategoryHierarchyRepository hierarchyRepository,
            LegacyWebCategoryMapper mapper) {
        this.repository = repository;
        this.hierarchyRepository = hierarchyRepository;
        this.mapper = mapper;
    }

    // ===== 페이징 조회 (Admin: fetchCategories) =====

    /**
     * 카테고리 페이징 조회.
     *
     * <p>No-Offset 조건이 있으면 No-Offset 방식, 없으면 일반 Offset 방식 사용.
     *
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 카테고리 목록
     */
    public List<LegacyCategoryResult> fetchCategories(
            LegacyCategorySearchCondition condition, Pageable pageable) {
        List<LegacyWebCategoryQueryDto> dtos;
        if (condition.isNoOffsetFetch()) {
            dtos = repository.fetchCategoriesWithNoOffset(condition, pageable);
        } else {
            dtos = repository.fetchCategories(condition, pageable);
        }
        return mapper.toResults(dtos);
    }

    /**
     * 카테고리 개수 조회.
     *
     * @param condition 검색 조건
     * @return 카테고리 개수
     */
    public long countCategories(LegacyCategorySearchCondition condition) {
        return repository.countCategories(condition);
    }

    // ===== 전체 조회 (Admin: fetchAllCategoriesAsTree, Web: getCategories) =====

    /**
     * 전체 카테고리 트리 조회.
     *
     * @return 트리 구조의 루트 카테고리 목록
     */
    public List<LegacyTreeCategoryResult> fetchAllCategoriesAsTree() {
        List<LegacyWebTreeCategoryQueryDto> dtos = repository.fetchAllCategories();
        return mapper.constructTree(dtos);
    }

    /**
     * 지정된 카테고리들의 트리 조회.
     *
     * @param categoryIds 조회할 카테고리 ID 목록
     * @return 트리 구조의 루트 카테고리 목록
     */
    public List<LegacyTreeCategoryResult> fetchCategoriesAsTree(Set<Long> categoryIds) {
        List<LegacyWebTreeCategoryQueryDto> dtos = repository.fetchAllCategories(categoryIds);
        return mapper.constructTree(dtos);
    }

    // ===== ID 기반 조회 (Admin: fetchAllParentCategoriesBulk) =====

    /**
     * 카테고리 ID 목록으로 조회 (flat).
     *
     * @param categoryIds 카테고리 ID 목록
     * @return 카테고리 목록 (트리 구조 아님)
     */
    public List<LegacyTreeCategoryResult> fetchCategoriesByIds(Set<Long> categoryIds) {
        List<LegacyWebTreeCategoryQueryDto> dtos = repository.fetchCategoriesByIds(categoryIds);
        return mapper.toTreeResults(dtos);
    }

    /**
     * 카테고리 ID 목록으로 조회 (List 버전, flat).
     *
     * @param categoryIds 카테고리 ID 목록
     * @return 카테고리 목록 (트리 구조 아님)
     */
    public List<LegacyTreeCategoryResult> fetchCategoriesByIds(List<Long> categoryIds) {
        List<LegacyWebTreeCategoryQueryDto> dtos = repository.fetchCategoriesByIds(categoryIds);
        return mapper.toTreeResults(dtos);
    }

    // ===== 단건 조회 =====

    /**
     * 카테고리 단건 조회 (ID).
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 Optional
     */
    public Optional<LegacyCategoryResult> fetchCategoryById(Long categoryId) {
        return repository.fetchCategoryById(categoryId).map(mapper::toResult);
    }

    // ===== 계층 조회 (Admin: fetchAllChildCategories, fetchAllParentCategories) =====

    /**
     * 하위 카테고리 전체 조회 (Recursive).
     *
     * <p>기준 카테고리부터 모든 하위 카테고리를 재귀적으로 탐색. depth 오름차순 정렬.
     *
     * @param categoryId 기준 카테고리 ID
     * @return 하위 카테고리 목록 (기준 카테고리 포함, flat)
     */
    public List<LegacyTreeCategoryResult> fetchAllChildCategories(Long categoryId) {
        List<LegacyWebTreeCategoryQueryDto> dtos =
                hierarchyRepository.fetchAllChildCategories(categoryId);
        return mapper.toTreeResults(dtos);
    }

    /**
     * 상위 카테고리 전체 조회 (Recursive).
     *
     * <p>기준 카테고리부터 루트까지 모든 상위 카테고리를 재귀적으로 탐색. depth 내림차순 정렬 (기준 → 루트).
     *
     * @param categoryId 기준 카테고리 ID
     * @return 상위 카테고리 목록 (기준 카테고리 포함, flat)
     */
    public List<LegacyTreeCategoryResult> fetchAllParentCategories(Long categoryId) {
        List<LegacyWebTreeCategoryQueryDto> dtos =
                hierarchyRepository.fetchAllParentCategories(categoryId);
        return mapper.toTreeResults(dtos);
    }

    /**
     * Breadcrumb 조회 (루트 → 기준 카테고리).
     *
     * @param categoryId 기준 카테고리 ID
     * @return 상위 카테고리 목록 (루트 → 기준 카테고리, flat)
     */
    public List<LegacyTreeCategoryResult> fetchBreadcrumb(Long categoryId) {
        List<LegacyWebTreeCategoryQueryDto> dtos = hierarchyRepository.fetchBreadcrumb(categoryId);
        return mapper.toTreeResults(dtos);
    }
}
