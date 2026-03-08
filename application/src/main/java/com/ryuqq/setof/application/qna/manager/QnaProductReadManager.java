package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.application.qna.port.out.query.QnaProductQueryPort;
import com.ryuqq.setof.domain.qna.exception.QnaNotFoundException;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * QnaProductReadManager - 상품 Q&A 조회 Manager.
 *
 * <p>상품에 걸린 Q&A 조회를 담당합니다. QnaProductQueryPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaProductReadManager {

    private final QnaProductQueryPort queryPort;

    public QnaProductReadManager(QnaProductQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public List<QnaWithAnswersResult> fetchProductQnas(ProductQnaSearchCriteria criteria) {
        return queryPort.fetchProductQnas(criteria);
    }

    @Transactional(readOnly = true)
    public long countProductQnas(ProductQnaSearchCriteria criteria) {
        return queryPort.countProductQnas(criteria);
    }

    @Transactional(readOnly = true)
    public QnaWithAnswersResult getProductQnaById(long qnaId) {
        return queryPort
                .findProductQnaById(qnaId)
                .orElseThrow(() -> new QnaNotFoundException(LegacyQnaId.of(qnaId)));
    }
}
