package com.ryuqq.setof.application.qna.manager;

import com.ryuqq.setof.application.qna.port.out.query.QnaQueryPort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.exception.QnaNotFoundException;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * QnaReadManager - Q&A 범용 조회 Manager.
 *
 * <p>상품/주문 타입 구분 없이 qna 테이블에서 단건 조회하여 Qna 도메인 객체를 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaReadManager {

    private final QnaQueryPort queryPort;

    public QnaReadManager(QnaQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public Qna getById(long qnaId) {
        return queryPort
                .findById(qnaId)
                .orElseThrow(() -> new QnaNotFoundException(LegacyQnaId.of(qnaId)));
    }
}
