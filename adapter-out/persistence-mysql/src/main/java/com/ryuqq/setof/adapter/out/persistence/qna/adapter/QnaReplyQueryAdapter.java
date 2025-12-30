package com.ryuqq.setof.adapter.out.persistence.qna.adapter;

import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaReplyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.mapper.QnaReplyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.qna.repository.QnaReplyQueryDslRepository;
import com.ryuqq.setof.application.qna.port.out.query.QnaReplyQueryPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaReply;
import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * QnaReplyQueryAdapter - QnA Reply Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, QnA Reply 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여
 * 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>QnA ID로 목록 조회 (findByQnaId) - Materialized Path 순서
 *   <li>존재 여부 확인 (existsById)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaReplyQueryAdapter implements QnaReplyQueryPort {

    private final QnaReplyQueryDslRepository queryDslRepository;
    private final QnaReplyJpaEntityMapper qnaReplyJpaEntityMapper;

    public QnaReplyQueryAdapter(
            QnaReplyQueryDslRepository queryDslRepository,
            QnaReplyJpaEntityMapper qnaReplyJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.qnaReplyJpaEntityMapper = qnaReplyJpaEntityMapper;
    }

    /**
     * ID로 Reply 단건 조회
     *
     * @param id Reply ID (Value Object)
     * @return Reply Domain (Optional)
     */
    @Override
    public Optional<QnaReply> findById(QnaReplyId id) {
        return queryDslRepository
                .findById(id.getValue())
                .map(qnaReplyJpaEntityMapper::toDomain);
    }

    /**
     * QnA ID로 Reply 목록 조회 (Materialized Path 순서)
     *
     * @param qnaId QnA ID
     * @return Reply 목록 (Path 순서로 정렬)
     */
    @Override
    public List<QnaReply> findByQnaId(long qnaId) {
        List<QnaReplyJpaEntity> entities = queryDslRepository.findByQnaId(qnaId);
        return entities.stream().map(qnaReplyJpaEntityMapper::toDomain).toList();
    }

    /**
     * Reply ID 존재 여부 확인
     *
     * @param id Reply ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(QnaReplyId id) {
        return queryDslRepository.existsById(id.getValue());
    }
}
