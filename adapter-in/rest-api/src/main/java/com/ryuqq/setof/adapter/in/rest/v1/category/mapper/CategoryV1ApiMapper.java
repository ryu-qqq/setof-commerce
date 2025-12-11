package com.ryuqq.setof.adapter.in.rest.v1.category.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Category V2 → V1 API 응답 변환 매퍼
 *
 * <p>V2 응답(CategoryTreeResponse)을 V1 응답(CategoryV1ApiResponse)으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CategoryV1ApiMapper {

    /**
     * V2 CategoryTreeResponse를 V1 응답으로 변환
     *
     * <p>재귀적으로 하위 카테고리를 포함하여 변환합니다.
     *
     * @param response V2 카테고리 트리 응답
     * @return V1 카테고리 응답
     */
    public CategoryV1ApiResponse toV1Response(CategoryTreeResponse response) {
        List<CategoryV1ApiResponse> children =
                response.children().stream().map(this::toV1Response).toList();

        return new CategoryV1ApiResponse(
                response.id(), response.nameKo(), response.depth(), null, children);
    }

    /**
     * V2 CategoryTreeResponse를 V1 응답으로 변환 (부모 ID 포함)
     *
     * @param response V2 카테고리 트리 응답
     * @param parentId 부모 카테고리 ID
     * @return V1 카테고리 응답
     */
    public CategoryV1ApiResponse toV1Response(CategoryTreeResponse response, Long parentId) {
        List<CategoryV1ApiResponse> children =
                response.children().stream()
                        .map(child -> toV1Response(child, response.id()))
                        .toList();

        return new CategoryV1ApiResponse(
                response.id(), response.nameKo(), response.depth(), parentId, children);
    }

    /**
     * V2 CategoryTreeResponse 목록을 V1 응답 목록으로 변환
     *
     * @param responses V2 카테고리 트리 응답 목록
     * @return V1 카테고리 응답 목록
     */
    public List<CategoryV1ApiResponse> toV1ResponseList(List<CategoryTreeResponse> responses) {
        return responses.stream().map(response -> toV1Response(response, null)).toList();
    }
}
