package com.ryuqq.setof.application.qna.manager.query;

import com.ryuqq.setof.application.qna.port.out.query.QnaQueryPort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.exception.QnaNotFoundException;
import com.ryuqq.setof.domain.qna.query.criteria.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * QnA Read Manager
 *
 * <p>QnA 조회를 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaReadManager {

    private final QnaQueryPort qnaQueryPort;

    public QnaReadManager(QnaQueryPort qnaQueryPort) {
        this.qnaQueryPort = qnaQueryPort;
    }

    /**
     * ID로 QnA 조회 (없으면 예외)
     *
     * @param qnaId QnA ID
     * @return QnA
     * @throws QnaNotFoundException QnA가 존재하지 않으면
     */
    public Qna findById(long qnaId) {
        QnaId id = QnaId.of(qnaId);
        return qnaQueryPort
                .findById(id)
                .orElseThrow(() -> new QnaNotFoundException(qnaId));
    }

    /**
     * 검색 조건으로 QnA 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return QnA 목록
     */
    public List<Qna> findByCriteria(QnaSearchCriteria criteria) {
        return qnaQueryPort.findByCriteria(criteria);
    }

    /**
     * 검색 조건에 맞는 QnA 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return QnA 총 개수
     */
    public long countByCriteria(QnaSearchCriteria criteria) {
        return qnaQueryPort.countByCriteria(criteria);
    }

    /**
     * QnA 존재 여부 확인
     *
     * @param qnaId QnA ID
     * @return 존재 여부
     */
    public boolean existsById(long qnaId) {
        QnaId id = QnaId.of(qnaId);
        return qnaQueryPort.existsById(id);
    }
}
