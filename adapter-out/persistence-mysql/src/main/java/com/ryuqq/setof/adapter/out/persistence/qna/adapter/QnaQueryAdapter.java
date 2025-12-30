package com.ryuqq.setof.adapter.out.persistence.qna.adapter;

import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.mapper.QnaJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.qna.repository.QnaQueryDslRepository;
import com.ryuqq.setof.application.qna.port.out.query.QnaQueryPort;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.query.criteria.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * QnaQueryAdapter - QnA Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, QnA 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>Criteria 기반 목록 조회 (findByCriteria)
 *   <li>Criteria 기반 개수 조회 (countByCriteria)
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
public class QnaQueryAdapter implements QnaQueryPort {

    private final QnaQueryDslRepository queryDslRepository;
    private final QnaJpaEntityMapper qnaJpaEntityMapper;

    public QnaQueryAdapter(
            QnaQueryDslRepository queryDslRepository, QnaJpaEntityMapper qnaJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.qnaJpaEntityMapper = qnaJpaEntityMapper;
    }

    /**
     * ID로 QnA 단건 조회
     *
     * @param id QnA ID (Value Object)
     * @return Qna Domain (Optional)
     */
    @Override
    public Optional<Qna> findById(QnaId id) {
        Optional<QnaJpaEntity> qnaEntity = queryDslRepository.findById(id.getValue());

        if (qnaEntity.isEmpty()) {
            return Optional.empty();
        }

        List<QnaImageJpaEntity> imageEntities =
                queryDslRepository.findImagesByQnaId(id.getValue());

        return Optional.of(qnaJpaEntityMapper.toDomain(qnaEntity.get(), imageEntities));
    }

    /**
     * 검색 조건으로 QnA 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return QnA 목록
     */
    @Override
    public List<Qna> findByCriteria(QnaSearchCriteria criteria) {
        List<QnaJpaEntity> qnaEntities = queryDslRepository.findByCriteria(criteria);

        if (qnaEntities.isEmpty()) {
            return List.of();
        }

        return toDomainWithImages(qnaEntities);
    }

    /**
     * 검색 조건에 맞는 QnA 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return QnA 총 개수
     */
    @Override
    public long countByCriteria(QnaSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    /**
     * ID로 QnA 존재 여부 확인
     *
     * @param id QnA ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(QnaId id) {
        return queryDslRepository.existsById(id.getValue());
    }

    /**
     * QnA Entity 목록을 Domain으로 변환 (이미지 포함)
     *
     * @param qnaEntities QnA Entity 목록
     * @return Qna Domain 목록
     */
    private List<Qna> toDomainWithImages(List<QnaJpaEntity> qnaEntities) {
        // 이미지 일괄 조회 (N+1 방지)
        List<Long> qnaIds = qnaEntities.stream().map(QnaJpaEntity::getId).toList();
        List<QnaImageJpaEntity> allImages = queryDslRepository.findImagesByQnaIds(qnaIds);

        // QnA ID별 이미지 그룹핑
        Map<Long, List<QnaImageJpaEntity>> imagesByQnaId =
                allImages.stream().collect(Collectors.groupingBy(QnaImageJpaEntity::getQnaId));

        // Domain 변환
        return qnaEntities.stream()
                .map(
                        entity -> {
                            List<QnaImageJpaEntity> images =
                                    imagesByQnaId.getOrDefault(entity.getId(), List.of());
                            return qnaJpaEntityMapper.toDomain(entity, images);
                        })
                .toList();
    }
}
