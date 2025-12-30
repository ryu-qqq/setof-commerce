package com.ryuqq.setof.application.faqcategory.service.query;

import com.ryuqq.setof.application.faqcategory.assembler.FaqCategoryAssembler;
import com.ryuqq.setof.application.faqcategory.dto.query.SearchFaqCategoryQuery;
import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import com.ryuqq.setof.application.faqcategory.factory.query.FaqCategoryQueryFactory;
import com.ryuqq.setof.application.faqcategory.manager.query.FaqCategoryReadManager;
import com.ryuqq.setof.application.faqcategory.port.in.query.GetFaqCategoryUseCase;
import com.ryuqq.setof.application.faqcategory.port.in.query.SearchFaqCategoryUseCase;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.query.FaqCategorySearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * FAQ 카테고리 조회 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class FaqCategoryQueryService implements GetFaqCategoryUseCase, SearchFaqCategoryUseCase {

    private final FaqCategoryReadManager readManager;
    private final FaqCategoryQueryFactory queryFactory;
    private final FaqCategoryAssembler assembler;

    public FaqCategoryQueryService(
            FaqCategoryReadManager readManager,
            FaqCategoryQueryFactory queryFactory,
            FaqCategoryAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public FaqCategoryResponse execute(FaqCategoryId categoryId) {
        FaqCategory category = readManager.findById(categoryId);
        return assembler.toResponse(category);
    }

    @Override
    public List<FaqCategoryResponse> execute(SearchFaqCategoryQuery query) {
        FaqCategorySearchCriteria criteria = queryFactory.createSearchCriteria(query);
        List<FaqCategory> categories = readManager.findByCriteria(criteria);
        return assembler.toResponseList(categories);
    }

    @Override
    public long count(SearchFaqCategoryQuery query) {
        FaqCategorySearchCriteria criteria = queryFactory.createSearchCriteria(query);
        return readManager.countByCriteria(criteria);
    }
}
