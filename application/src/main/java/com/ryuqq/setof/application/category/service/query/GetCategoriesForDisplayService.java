package com.ryuqq.setof.application.category.service.query;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesForDisplayUseCase;
import com.ryuqq.setof.domain.category.aggregate.Category;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 노출용 카테고리 트리 조회 Service.
 *
 * <p>Public API용 간단한 카테고리 목록 조회입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetCategoriesForDisplayService implements GetCategoriesForDisplayUseCase {

    private final CategoryReadManager readManager;
    private final CategoryAssembler assembler;

    public GetCategoriesForDisplayService(
            CategoryReadManager readManager, CategoryAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public List<CategoryDisplayResult> execute() {
        List<Category> categories = readManager.findAllDisplayed();
        return assembler.toDisplayResults(categories);
    }
}
