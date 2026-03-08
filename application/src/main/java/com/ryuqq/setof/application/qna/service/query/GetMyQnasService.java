package com.ryuqq.setof.application.qna.service.query;

import com.ryuqq.setof.application.qna.assembler.QnaAssembler;
import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;
import com.ryuqq.setof.application.qna.factory.QnaQueryFactory;
import com.ryuqq.setof.application.qna.internal.MyQnaReadStrategy;
import com.ryuqq.setof.application.qna.internal.MyQnaReadStrategyProvider;
import com.ryuqq.setof.application.qna.port.in.query.GetMyQnasUseCase;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetMyQnasService - 내 Q&A 목록 조회 Service.
 *
 * <p>QueryFactory로 Criteria 생성 → StrategyProvider로 타입별 조회 전략 선택 → Assembler로 결과 조립.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetMyQnasService implements GetMyQnasUseCase {

    private final MyQnaReadStrategyProvider strategyProvider;
    private final QnaQueryFactory queryFactory;
    private final QnaAssembler assembler;

    public GetMyQnasService(
            MyQnaReadStrategyProvider strategyProvider,
            QnaQueryFactory queryFactory,
            QnaAssembler assembler) {
        this.strategyProvider = strategyProvider;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public MyQnaSliceResult execute(MyQnaSearchParams params) {
        QnaSearchCriteria criteria = queryFactory.createMyQnaCriteria(params);

        MyQnaReadStrategy strategy = strategyProvider.getStrategy(criteria.qnaType());
        List<MyQnaResult> qnas = strategy.fetchMyQnas(criteria);

        return assembler.toMyQnaSliceResult(qnas, criteria.size());
    }
}
