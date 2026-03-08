package com.ryuqq.setof.storage.legacy.qna.adapter;

import com.ryuqq.setof.application.qna.port.out.command.QnaCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaAnswerEntity;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity;
import com.ryuqq.setof.storage.legacy.qna.mapper.LegacyQnaAnswerEntityMapper;
import com.ryuqq.setof.storage.legacy.qna.mapper.LegacyQnaEntityMapper;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaAnswerJpaRepository;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaCommandAdapter - 레거시 Q&A 명령 Adapter.
 *
 * <p>QnaCommandPort 구현체. Qna Aggregate 단위로 qna + qna_answer 테이블을 함께 영속합니다.
 *
 * <p>답변이 존재하면 legacyId 유무로 INSERT/UPDATE를 결정합니다 (JPA merge).
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository + Mapper에만 의존.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaCommandAdapter implements QnaCommandPort {

    private final LegacyQnaJpaRepository qnaJpaRepository;
    private final LegacyQnaAnswerJpaRepository answerJpaRepository;
    private final LegacyQnaEntityMapper qnaMapper;
    private final LegacyQnaAnswerEntityMapper answerMapper;

    public LegacyQnaCommandAdapter(
            LegacyQnaJpaRepository qnaJpaRepository,
            LegacyQnaAnswerJpaRepository answerJpaRepository,
            LegacyQnaEntityMapper qnaMapper,
            LegacyQnaAnswerEntityMapper answerMapper) {
        this.qnaJpaRepository = qnaJpaRepository;
        this.answerJpaRepository = answerJpaRepository;
        this.qnaMapper = qnaMapper;
        this.answerMapper = answerMapper;
    }

    @Override
    public Long persist(Qna qna) {
        LegacyQnaEntity entity = qnaMapper.toEntity(qna);
        LegacyQnaEntity saved = qnaJpaRepository.save(entity);
        long qnaId = saved.getId();

        if (qna.answer() != null) {
            LegacyQnaAnswerEntity answerEntity = answerMapper.toEntity(qnaId, qna);
            answerJpaRepository.save(answerEntity);
        }

        return qnaId;
    }
}
