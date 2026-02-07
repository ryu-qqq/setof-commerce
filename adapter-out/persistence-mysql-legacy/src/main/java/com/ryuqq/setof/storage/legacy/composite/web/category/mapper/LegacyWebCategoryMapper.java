package com.ryuqq.setof.storage.legacy.composite.web.category.mapper;

import com.ryuqq.setof.application.legacy.category.dto.response.LegacyCategoryResult;
import com.ryuqq.setof.application.legacy.category.dto.response.LegacyTreeCategoryResult;
import com.ryuqq.setof.storage.legacy.composite.web.category.dto.LegacyWebCategoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.category.dto.LegacyWebTreeCategoryQueryDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebCategoryMapper - 레거시 Web 카테고리 Mapper.
 *
 * <p>QueryDto → Application Result 변환 및 트리 구성을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebCategoryMapper {

    // ===== QueryDto → Result 변환 =====

    /**
     * LegacyWebCategoryQueryDto → LegacyCategoryResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyCategoryResult
     */
    public LegacyCategoryResult toResult(LegacyWebCategoryQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyCategoryResult.of(
                dto.categoryId(),
                dto.categoryName(),
                dto.displayName(),
                dto.categoryDepth(),
                dto.parentCategoryId(),
                dto.path(),
                dto.targetGroup());
    }

    /**
     * LegacyWebCategoryQueryDto 목록 → LegacyCategoryResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyCategoryResult 목록
     */
    public List<LegacyCategoryResult> toResults(List<LegacyWebCategoryQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }

    // ===== TreeCategoryQueryDto → TreeResult 변환 =====

    /**
     * LegacyWebTreeCategoryQueryDto → LegacyTreeCategoryResult 변환.
     *
     * @param dto TreeCategoryQueryDto
     * @return LegacyTreeCategoryResult
     */
    public LegacyTreeCategoryResult toTreeResult(LegacyWebTreeCategoryQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyTreeCategoryResult.withEmptyChildren(
                dto.categoryId(),
                dto.categoryName(),
                dto.displayName(),
                dto.categoryDepth(),
                dto.parentCategoryId());
    }

    /**
     * LegacyWebTreeCategoryQueryDto 목록 → LegacyTreeCategoryResult 목록 변환.
     *
     * @param dtos TreeCategoryQueryDto 목록
     * @return LegacyTreeCategoryResult 목록
     */
    public List<LegacyTreeCategoryResult> toTreeResults(List<LegacyWebTreeCategoryQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toTreeResult).toList();
    }

    // ===== 트리 구성 =====

    /**
     * flat 리스트를 트리 구조로 변환.
     *
     * <p>depth=1인 카테고리를 루트로 설정하고, 나머지는 부모의 children에 추가.
     *
     * @param dtos flat 카테고리 목록
     * @return 트리 구조의 루트 카테고리 목록
     */
    public List<LegacyTreeCategoryResult> constructTree(List<LegacyWebTreeCategoryQueryDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }

        // 1. categoryId → Result 맵 생성
        Map<Long, LegacyTreeCategoryResult> categoryMap =
                dtos.stream()
                        .map(this::toTreeResult)
                        .collect(
                                Collectors.toMap(
                                        LegacyTreeCategoryResult::categoryId,
                                        Function.identity(),
                                        (existing, replacement) -> existing,
                                        HashMap::new));

        // 2. depth=1이면 루트, 아니면 부모의 children에 추가
        List<LegacyTreeCategoryResult> roots = new ArrayList<>();
        for (LegacyWebTreeCategoryQueryDto dto : dtos) {
            LegacyTreeCategoryResult result = categoryMap.get(dto.categoryId());
            if (dto.categoryDepth() == 1) {
                roots.add(result);
            } else {
                LegacyTreeCategoryResult parent = categoryMap.get(dto.parentCategoryId());
                if (parent != null) {
                    parent.children().add(result);
                }
            }
        }

        return roots;
    }
}
