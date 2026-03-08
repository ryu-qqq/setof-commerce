package com.ryuqq.setof.storage.legacy.qna.adapter;

import com.ryuqq.setof.application.qna.port.out.query.QnaQueryPort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaAnswerEntity;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity;
import com.ryuqq.setof.storage.legacy.qna.mapper.LegacyQnaEntityMapper;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaAnswerQueryDslRepository;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaQueryDslRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaQueryAdapter - Q&A 범용 단건 조회 Adapter.
 *
 * <p>QnaQueryPort 구현체입니다. qna 테이블에서 ID 기반 단건 조회 후 Qna 도메인 객체로 변환합니다. 답변이 있는 경우 함께 로드합니다.
 *
 * <p>PER-ADP-001: QueryAdapter는 QueryDslRepository + Mapper에만 의존.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaQueryAdapter implements QnaQueryPort {

    private final LegacyQnaQueryDslRepository qnaRepository;
    private final LegacyQnaAnswerQueryDslRepository answerRepository;
    private final LegacyQnaEntityMapper mapper;

    public LegacyQnaQueryAdapter(
            LegacyQnaQueryDslRepository qnaRepository,
            LegacyQnaAnswerQueryDslRepository answerRepository,
            LegacyQnaEntityMapper mapper) {
        this.qnaRepository = qnaRepository;
        this.answerRepository = answerRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Qna> findById(long qnaId) {
        Optional<LegacyQnaEntity> qnaEntity = qnaRepository.findById(qnaId);
        if (qnaEntity.isEmpty()) {
            return Optional.empty();
        }

        LegacyQnaAnswerEntity answerEntity = answerRepository.findByQnaId(qnaId).orElse(null);

        return Optional.of(mapper.toDomain(qnaEntity.get(), answerEntity));
    }
}
