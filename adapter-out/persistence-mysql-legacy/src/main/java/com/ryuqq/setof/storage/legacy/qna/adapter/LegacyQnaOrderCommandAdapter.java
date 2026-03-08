package com.ryuqq.setof.storage.legacy.qna.adapter;

import com.ryuqq.setof.application.qna.port.out.command.QnaOrderCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaOrderEntity;
import com.ryuqq.setof.storage.legacy.qna.mapper.LegacyQnaEntityMapper;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaOrderJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaOrderCommandAdapter - 레거시 Q&A 주문 매핑 Command Adapter.
 *
 * <p>QnaOrderCommandPort 구현체. 레거시 DB의 qna_order 테이블만 영속합니다.
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
public class LegacyQnaOrderCommandAdapter implements QnaOrderCommandPort {

    private final LegacyQnaOrderJpaRepository jpaRepository;
    private final LegacyQnaEntityMapper mapper;

    public LegacyQnaOrderCommandAdapter(
            LegacyQnaOrderJpaRepository jpaRepository, LegacyQnaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void persist(QnaOrder qnaOrder) {
        LegacyQnaOrderEntity entity = mapper.toOrderEntity(qnaOrder);
        jpaRepository.save(entity);
    }
}
