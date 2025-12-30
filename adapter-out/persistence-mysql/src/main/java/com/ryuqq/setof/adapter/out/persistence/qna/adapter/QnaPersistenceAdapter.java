package com.ryuqq.setof.adapter.out.persistence.qna.adapter;

import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.mapper.QnaJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.qna.repository.QnaImageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.qna.repository.QnaJpaRepository;
import com.ryuqq.setof.application.qna.port.out.command.QnaPersistencePort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * QnaPersistenceAdapter - QnA Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, QnA 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>QnA 저장 (persist)
 *   <li>QnaJpaEntity + QnaImageJpaEntity 함께 저장
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class QnaPersistenceAdapter implements QnaPersistencePort {

    private final QnaJpaRepository qnaJpaRepository;
    private final QnaImageJpaRepository qnaImageJpaRepository;
    private final QnaJpaEntityMapper qnaJpaEntityMapper;

    public QnaPersistenceAdapter(
            QnaJpaRepository qnaJpaRepository,
            QnaImageJpaRepository qnaImageJpaRepository,
            QnaJpaEntityMapper qnaJpaEntityMapper) {
        this.qnaJpaRepository = qnaJpaRepository;
        this.qnaImageJpaRepository = qnaImageJpaRepository;
        this.qnaJpaEntityMapper = qnaJpaEntityMapper;
    }

    /**
     * QnA 저장 (생성/수정)
     *
     * <p>QnA와 이미지를 함께 저장합니다.
     *
     * @param qna QnA 도메인
     * @return 저장된 QnaId
     */
    @Override
    public QnaId persist(Qna qna) {
        // QnA Entity 저장
        QnaJpaEntity qnaEntity = qnaJpaEntityMapper.toEntity(qna);
        QnaJpaEntity savedQnaEntity = qnaJpaRepository.save(qnaEntity);

        // 이미지 Entity 저장 (ORDER QnA만 이미지 보유)
        List<QnaImageJpaEntity> imageEntities =
                qnaJpaEntityMapper.toImageEntities(qna, savedQnaEntity.getId());
        if (!imageEntities.isEmpty()) {
            qnaImageJpaRepository.saveAll(imageEntities);
        }

        return QnaId.of(savedQnaEntity.getId());
    }
}
