package com.ryuqq.setof.storage.legacy.qna.adapter;

import com.ryuqq.setof.application.qna.port.out.command.QnaProductCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaProductEntity;
import com.ryuqq.setof.storage.legacy.qna.mapper.LegacyQnaEntityMapper;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaProductJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaProductCommandAdapter - 레거시 Q&A 상품 매핑 Command Adapter.
 *
 * <p>QnaProductCommandPort 구현체. 레거시 DB의 qna_product 테이블만 영속합니다.
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
public class LegacyQnaProductCommandAdapter implements QnaProductCommandPort {

    private final LegacyQnaProductJpaRepository jpaRepository;
    private final LegacyQnaEntityMapper mapper;

    public LegacyQnaProductCommandAdapter(
            LegacyQnaProductJpaRepository jpaRepository, LegacyQnaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void persist(QnaProduct qnaProduct) {
        LegacyQnaProductEntity entity = mapper.toProductEntity(qnaProduct);
        jpaRepository.save(entity);
    }
}
