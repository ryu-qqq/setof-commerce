package com.ryuqq.setof.application.faq.service.query;

import com.ryuqq.setof.application.faq.assembler.FaqAssembler;
import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.application.faq.factory.FaqQueryFactory;
import com.ryuqq.setof.application.faq.manager.FaqReadManager;
import com.ryuqq.setof.application.faq.port.in.query.GetFaqsByTypeUseCase;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * FAQ 유형별 조회 Service.
 *
 * <p>faqType 기반으로 FAQ 목록을 조회합니다.
 *
 * <p>QueryFactory를 통해 Params → Criteria 변환
 *
 * <p>Assembler를 통해 FaqResult 목록 생성
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetFaqsByTypeService implements GetFaqsByTypeUseCase {

    private final FaqReadManager readManager;
    private final FaqQueryFactory queryFactory;
    private final FaqAssembler assembler;

    public GetFaqsByTypeService(
            FaqReadManager readManager, FaqQueryFactory queryFactory, FaqAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public List<FaqResult> execute(FaqSearchParams params) {
        FaqSearchCriteria criteria = queryFactory.createCriteria(params);
        List<Faq> faqs = readManager.findByCriteria(criteria);
        return assembler.toResults(faqs);
    }
}
