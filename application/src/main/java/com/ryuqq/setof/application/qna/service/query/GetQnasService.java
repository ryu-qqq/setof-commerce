package com.ryuqq.setof.application.qna.service.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.qna.assembler.QnaAssembler;
import com.ryuqq.setof.application.qna.dto.query.QnaSearchQuery;
import com.ryuqq.setof.application.qna.dto.response.QnaSummaryResponse;
import com.ryuqq.setof.application.qna.factory.query.QnaQueryFactory;
import com.ryuqq.setof.application.qna.manager.query.QnaReadManager;
import com.ryuqq.setof.application.qna.port.in.query.GetQnasUseCase;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.query.criteria.QnaSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 문의 목록 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>QnaQueryFactory로 Criteria 생성
 *   <li>QnaReadManager로 목록 및 개수 조회
 *   <li>QnaAssembler로 Response DTO 목록 변환
 *   <li>PageResponse로 포장하여 반환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetQnasService implements GetQnasUseCase {

    private final QnaReadManager qnaReadManager;
    private final QnaQueryFactory qnaQueryFactory;
    private final QnaAssembler qnaAssembler;

    public GetQnasService(
            QnaReadManager qnaReadManager,
            QnaQueryFactory qnaQueryFactory,
            QnaAssembler qnaAssembler) {
        this.qnaReadManager = qnaReadManager;
        this.qnaQueryFactory = qnaQueryFactory;
        this.qnaAssembler = qnaAssembler;
    }

    @Override
    public PageResponse<QnaSummaryResponse> execute(QnaSearchQuery query) {
        QnaSearchCriteria criteria = qnaQueryFactory.create(query);

        List<Qna> qnas = qnaReadManager.findByCriteria(criteria);
        long totalCount = qnaReadManager.countByCriteria(criteria);

        List<QnaSummaryResponse> content = qnaAssembler.toQnaSummaryResponses(qnas);
        int totalPages = calculateTotalPages(totalCount, criteria.pageSize());
        boolean isFirst = criteria.page() == 0;
        boolean isLast = criteria.page() >= totalPages - 1;

        return PageResponse.of(
                content,
                criteria.page(),
                criteria.pageSize(),
                totalCount,
                totalPages,
                isFirst,
                isLast);
    }

    private int calculateTotalPages(long totalCount, int size) {
        if (totalCount == 0 || size == 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalCount / size);
    }
}
