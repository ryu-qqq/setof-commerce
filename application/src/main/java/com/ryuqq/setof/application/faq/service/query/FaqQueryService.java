package com.ryuqq.setof.application.faq.service.query;

import com.ryuqq.setof.application.faq.assembler.FaqAssembler;
import com.ryuqq.setof.application.faq.dto.query.SearchFaqQuery;
import com.ryuqq.setof.application.faq.dto.response.FaqResponse;
import com.ryuqq.setof.application.faq.factory.query.FaqQueryFactory;
import com.ryuqq.setof.application.faq.manager.query.FaqReadManager;
import com.ryuqq.setof.application.faq.port.in.query.GetFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.query.SearchFaqUseCase;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * FAQ 조회 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class FaqQueryService implements GetFaqUseCase, SearchFaqUseCase {

    private final FaqReadManager faqReadManager;
    private final FaqQueryFactory faqQueryFactory;
    private final FaqAssembler faqAssembler;

    public FaqQueryService(
            FaqReadManager faqReadManager,
            FaqQueryFactory faqQueryFactory,
            FaqAssembler faqAssembler) {
        this.faqReadManager = faqReadManager;
        this.faqQueryFactory = faqQueryFactory;
        this.faqAssembler = faqAssembler;
    }

    @Override
    public FaqResponse execute(FaqId faqId) {
        Faq faq = faqReadManager.findById(faqId);
        return faqAssembler.toResponse(faq);
    }

    @Override
    public List<FaqResponse> execute(SearchFaqQuery query) {
        FaqSearchCriteria criteria = faqQueryFactory.createSearchCriteria(query);
        List<Faq> faqs = faqReadManager.findByCriteria(criteria);
        return faqAssembler.toResponses(faqs);
    }

    @Override
    public long count(SearchFaqQuery query) {
        FaqSearchCriteria criteria = faqQueryFactory.createSearchCriteria(query);
        return faqReadManager.countByCriteria(criteria);
    }
}
