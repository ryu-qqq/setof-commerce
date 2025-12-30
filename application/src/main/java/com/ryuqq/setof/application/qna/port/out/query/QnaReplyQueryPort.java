package com.ryuqq.setof.application.qna.port.out.query;

import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import java.util.List;
import java.util.Optional;

/**
 * QnA Reply Query Port (Query)
 *
 * <p>QnA Reply를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface QnaReplyQueryPort {

    /**
     * ID로 Reply 단건 조회
     *
     * @param id Reply ID (Value Object)
     * @return Reply Domain (Optional)
     */
    Optional<QnaReply> findById(QnaReplyId id);

    /**
     * QnA ID로 Reply 목록 조회 (Materialized Path 순서로 정렬)
     *
     * @param qnaId QnA ID
     * @return Reply 목록
     */
    List<QnaReply> findByQnaId(long qnaId);

    /**
     * Reply ID 존재 여부 확인
     *
     * @param id Reply ID
     * @return 존재 여부
     */
    boolean existsById(QnaReplyId id);
}
