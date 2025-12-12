package com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryContextV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Category V2 → Admin V1 API 응답 변환 매퍼
 *
 * <p>V2 응답(CategoryTreeResponse)을 Admin V1 응답(TreeCategoryContextV1ApiResponse)으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CategoryAdminV1ApiMapper {

    /**
     * V2 CategoryTreeResponse를 Admin V1 응답으로 변환
     *
     * <p>재귀적으로 하위 카테고리를 포함하여 변환합니다.
     *
     * @param response V2 카테고리 트리 응답
     * @return Admin V1 카테고리 응답
     */
    public TreeCategoryContextV1ApiResponse toV1Response(CategoryTreeResponse response) {
        return toV1Response(response, null);
    }

    /**
     * V2 CategoryTreeResponse를 Admin V1 응답으로 변환 (부모 ID 포함)
     *
     * @param response V2 카테고리 트리 응답
     * @param parentId 부모 카테고리 ID
     * @return Admin V1 카테고리 응답
     */
    public TreeCategoryContextV1ApiResponse toV1Response(
            CategoryTreeResponse response, Long parentId) {
        List<TreeCategoryContextV1ApiResponse> children =
                response.children().stream()
                        .map(child -> toV1Response(child, response.id()))
                        .toList();

        String displayName = buildDisplayName(response.nameKo(), response.depth());

        return new TreeCategoryContextV1ApiResponse(
                response.id(),
                response.nameKo(),
                displayName,
                response.depth(),
                parentId,
                children);
    }

    /**
     * V2 CategoryTreeResponse 목록을 Admin V1 응답 목록으로 변환
     *
     * @param responses V2 카테고리 트리 응답 목록
     * @return Admin V1 카테고리 응답 목록
     */
    public List<TreeCategoryContextV1ApiResponse> toV1ResponseList(
            List<CategoryTreeResponse> responses) {
        return responses.stream().map(response -> toV1Response(response, null)).toList();
    }

    private String buildDisplayName(String name, int depth) {
        return name;
    }
}
