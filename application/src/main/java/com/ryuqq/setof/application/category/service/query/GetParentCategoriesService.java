package com.ryuqq.setof.application.category.service.query;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.application.category.port.in.query.GetParentCategoriesUseCase;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 부모 카테고리 조회 Service.
 *
 * <p>특정 카테고리의 모든 상위(부모) 카테고리들을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetParentCategoriesService implements GetParentCategoriesUseCase {

    private final CategoryReadManager readManager;
    private final CategoryAssembler assembler;

    public GetParentCategoriesService(
            CategoryReadManager readManager, CategoryAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public List<TreeCategoryResult> execute(Long categoryId) {
        CategoryId childId = CategoryId.of(categoryId);
        List<Category> parents = readManager.findParentsByChildId(childId);
        return assembler.toLeafResults(parents);
    }
}
