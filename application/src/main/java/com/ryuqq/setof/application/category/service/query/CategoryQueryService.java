package com.ryuqq.setof.application.category.service.query;

import com.ryuqq.setof.application.category.assembler.CategoryAssembler;
import com.ryuqq.setof.application.category.dto.query.CategorySearchQuery;
import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.application.category.factory.query.CategoryQueryFactory;
import com.ryuqq.setof.application.category.manager.query.CategoryReadManager;
import com.ryuqq.setof.application.category.port.in.query.GetCategoriesUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryPathUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryTreeUseCase;
import com.ryuqq.setof.application.category.port.in.query.GetCategoryUseCase;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 카테고리 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>CategoryReadManager로 카테고리 조회
 *   <li>CategoryAssembler로 Response DTO 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CategoryQueryService
        implements GetCategoryUseCase,
                GetCategoriesUseCase,
                GetCategoryTreeUseCase,
                GetCategoryPathUseCase {

    private final CategoryReadManager categoryReadManager;
    private final CategoryQueryFactory categoryQueryFactory;
    private final CategoryAssembler categoryAssembler;

    public CategoryQueryService(
            CategoryReadManager categoryReadManager,
            CategoryQueryFactory categoryQueryFactory,
            CategoryAssembler categoryAssembler) {
        this.categoryReadManager = categoryReadManager;
        this.categoryQueryFactory = categoryQueryFactory;
        this.categoryAssembler = categoryAssembler;
    }

    @Override
    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryReadManager.findById(categoryId);
        return categoryAssembler.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getCategories(Long parentId) {
        List<Category> categories;
        if (parentId == null) {
            // 최상위 카테고리 조회
            CategorySearchCriteria criteria =
                    categoryQueryFactory.createCriteria(CategorySearchQuery.forRootCategories());
            categories = categoryReadManager.findByCriteria(criteria);
        } else {
            // 하위 카테고리 조회
            categories = categoryReadManager.findByParentId(parentId);
        }
        return categoryAssembler.toCategoryResponses(categories);
    }

    @Override
    public List<CategoryTreeResponse> getCategoryTree() {
        List<Category> allCategories = categoryReadManager.findAllActive();
        return categoryAssembler.toCategoryTreeResponses(allCategories);
    }

    @Override
    public CategoryPathResponse getCategoryPath(Long categoryId) {
        Category category = categoryReadManager.findById(categoryId);

        // 경로에서 ID 추출
        List<Long> pathIds = category.getPath().extractIds();

        // 경로 카테고리들 조회
        List<Category> pathCategories = categoryReadManager.findByIds(pathIds);

        return categoryAssembler.toCategoryPathResponse(categoryId, pathCategories);
    }
}
