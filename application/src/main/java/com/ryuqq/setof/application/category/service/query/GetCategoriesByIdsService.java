package com.ryuqq.setof.application.category.service.query;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.category.manager.CategoryReadManager;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesByIdsUseCase;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 카테고리 ID 목록 조회 Service.
 *
 * <p>요청한 카테고리 ID 목록에 해당하는 카테고리들을 flat list로 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetCategoriesByIdsService implements GetCategoriesByIdsUseCase {

    private final CategoryReadManager readManager;
    private final CategoryAssembler assembler;

    public GetCategoriesByIdsService(CategoryReadManager readManager, CategoryAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public List<TreeCategoryResult> execute(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return List.of();
        }
        List<CategoryId> ids =
                categoryIds.stream().map(CategoryId::of).collect(Collectors.toList());
        List<Category> categories = readManager.getByIds(ids);
        return assembler.toLeafResults(categories);
    }
}
