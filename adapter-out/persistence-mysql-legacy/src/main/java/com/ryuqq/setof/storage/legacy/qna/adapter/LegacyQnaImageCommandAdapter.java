package com.ryuqq.setof.storage.legacy.qna.adapter;

import com.ryuqq.setof.application.qna.port.out.command.QnaImageCommandPort;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaImageEntity;
import com.ryuqq.setof.storage.legacy.qna.mapper.LegacyQnaImageEntityMapper;
import com.ryuqq.setof.storage.legacy.qna.repository.LegacyQnaImageJpaRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyQnaImageCommandAdapter - 레거시 Q&A 이미지 Command Adapter.
 *
 * <p>QnaImageCommandPort 구현체. 레거시 DB의 qna_image 테이블에 이미지를 영속합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyQnaImageCommandAdapter implements QnaImageCommandPort {

    private final LegacyQnaImageJpaRepository jpaRepository;
    private final LegacyQnaImageEntityMapper mapper;

    public LegacyQnaImageCommandAdapter(
            LegacyQnaImageJpaRepository jpaRepository, LegacyQnaImageEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void persistAll(long qnaId, QnaImages images) {
        if (images.isEmpty()) {
            return;
        }

        List<LegacyQnaImageEntity> entities =
                images.toList().stream().map(image -> mapper.toEntity(qnaId, image)).toList();

        jpaRepository.saveAll(entities);
    }

    @Override
    public void deleteAllByQnaId(long qnaId) {
        jpaRepository.softDeleteAllByQnaId(qnaId);
    }
}
