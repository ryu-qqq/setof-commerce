package com.ryuqq.setof.application.category.service.query;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.application.category.port.in.query.GetAllCategoriesAsTreeUseCase;
import com.ryuqq.setof.domain.category.aggregate.Category;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 전체 카테고리 트리 조회 Service.
 *
 * <p>모든 노출 카테고리를 계층 구조(트리)로 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetAllCategoriesAsTreeService implements GetAllCategoriesAsTreeUseCase {

    private final CategoryReadManager readManager;
    private final CategoryAssembler assembler;

    public GetAllCategoriesAsTreeService(
            CategoryReadManager readManager, CategoryAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public List<TreeCategoryResult> execute() {
        List<Category> categories = readManager.findAllDisplayed();
        return assembler.toTreeResults(categories);
    }
}
