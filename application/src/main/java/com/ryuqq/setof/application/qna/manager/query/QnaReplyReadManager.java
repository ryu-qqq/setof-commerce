package com.ryuqq.setof.application.qna.manager.query;

import com.ryuqq.setof.application.qna.port.out.query.QnaReplyQueryPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.exception.QnaReplyNotFoundException;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * QnA Reply Read Manager
 *
 * <p>QnA Reply 조회를 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaReplyReadManager {

    private final QnaReplyQueryPort qnaReplyQueryPort;

    public QnaReplyReadManager(QnaReplyQueryPort qnaReplyQueryPort) {
        this.qnaReplyQueryPort = qnaReplyQueryPort;
    }

    /**
     * ID로 Reply 조회 (없으면 예외)
     *
     * @param replyId Reply ID
     * @return QnaReply
     * @throws QnaReplyNotFoundException Reply가 존재하지 않으면
     */
    public QnaReply findById(long replyId) {
        QnaReplyId id = QnaReplyId.of(replyId);
        return qnaReplyQueryPort
                .findById(id)
                .orElseThrow(() -> new QnaReplyNotFoundException(replyId));
    }

    /**
     * QnA ID로 Reply 목록 조회 (Materialized Path 순서로 정렬)
     *
     * @param qnaId QnA ID
     * @return Reply 목록
     */
    public List<QnaReply> findByQnaId(long qnaId) {
        return qnaReplyQueryPort.findByQnaId(qnaId);
    }

    /**
     * Reply 존재 여부 확인
     *
     * @param replyId Reply ID
     * @return 존재 여부
     */
    public boolean existsById(long replyId) {
        QnaReplyId id = QnaReplyId.of(replyId);
        return qnaReplyQueryPort.existsById(id);
    }
}
