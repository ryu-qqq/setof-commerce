package com.ryuqq.setof.adapter.in.rest.v1.category.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryDisplayV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CategoryV1ApiMapper - V1 Public 카테고리 API 매퍼.
 *
 * <p>Application Layer의 결과를 V1 Public API 응답으로 변환합니다.
 *
 * <p>레거시 CategoryDisplayDto와 동일한 구조로 변환합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 정의.
 *
 * <p>API-MAP-002: 순수 변환 로직만 포함 (비즈니스 로직 금지).
 *
 * <p>API-MAP-003: null-safe 변환 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryV1ApiMapper {

    /**
     * CategoryDisplayResult 목록을 V1 카테고리 응답 목록으로 변환.
     *
     * <p>레거시 CategoryDisplayDto와 동일한 필드로 변환합니다.
     *
     * @param results UseCase 실행 결과
     * @return V1 호환 카테고리 목록
     */
    public List<CategoryDisplayV1ApiResponse> toListResponse(List<CategoryDisplayResult> results) {
        return results.stream().map(this::toResponse).toList();
    }

    /**
     * CategoryDisplayResult를 V1 카테고리 응답으로 재귀 변환.
     *
     * <p>레거시 CategoryDisplayDto와 동일한 필드로 변환합니다.
     *
     * @param result 카테고리 조회 결과
     * @return V1 호환 카테고리 응답
     */
    private CategoryDisplayV1ApiResponse toResponse(CategoryDisplayResult result) {
        List<CategoryDisplayV1ApiResponse> children =
                result.children().stream().map(this::toResponse).toList();

        return new CategoryDisplayV1ApiResponse(
                result.categoryId(),
                result.categoryName(),
                result.depth(),
                result.parentCategoryId(),
                children);
    }
}
