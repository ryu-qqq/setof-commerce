package com.ryuqq.setof.application.category.service.query;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.application.category.port.in.query.GetChildCategoriesUseCase;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 자식 카테고리 조회 Service.
 *
 * <p>특정 카테고리의 직계 자식 카테고리들을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetChildCategoriesService implements GetChildCategoriesUseCase {

    private final CategoryReadManager readManager;
    private final CategoryAssembler assembler;

    public GetChildCategoriesService(CategoryReadManager readManager, CategoryAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public List<TreeCategoryResult> execute(Long categoryId) {
        CategoryId parentId = CategoryId.of(categoryId);
        List<Category> children = readManager.findChildrenByParentId(parentId);
        return assembler.toTreeResults(children);
    }
}
