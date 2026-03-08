package com.ryuqq.setof.application.qna.service.query;

import com.ryuqq.setof.application.qna.assembler.QnaAssembler;
import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;
import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.application.qna.factory.QnaQueryFactory;
import com.ryuqq.setof.application.qna.manager.QnaProductReadManager;
import com.ryuqq.setof.application.qna.port.in.query.GetProductQnasUseCase;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetProductQnasService - 상품 Q&A 목록 조회 Service.
 *
 * <p>QueryFactory로 Criteria 생성 → ReadManager로 조회 → Assembler로 결과 조립.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetProductQnasService implements GetProductQnasUseCase {

    private final QnaProductReadManager readManager;
    private final QnaQueryFactory queryFactory;
    private final QnaAssembler assembler;

    public GetProductQnasService(
            QnaProductReadManager readManager,
            QnaQueryFactory queryFactory,
            QnaAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public QnaPageResult execute(ProductQnaSearchParams params) {
        ProductQnaSearchCriteria criteria = queryFactory.createProductQnaCriteria(params);

        long totalElements = readManager.countProductQnas(criteria);
        if (totalElements == 0) {
            return QnaPageResult.empty(criteria.page(), criteria.size());
        }

        List<QnaWithAnswersResult> qnas = readManager.fetchProductQnas(criteria);

        return assembler.toQnaPageResult(
                qnas, params.viewerUserId(), criteria.page(), criteria.size(), totalElements);
    }
}
