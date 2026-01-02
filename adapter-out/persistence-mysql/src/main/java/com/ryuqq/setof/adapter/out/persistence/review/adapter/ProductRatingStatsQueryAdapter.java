package com.ryuqq.setof.adapter.out.persistence.review.adapter;

import com.ryuqq.setof.adapter.out.persistence.review.entity.ProductRatingStatsJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.mapper.ReviewJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.review.repository.ReviewQueryDslRepository;
import com.ryuqq.setof.application.review.port.out.query.ProductRatingStatsQueryPort;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductRatingStatsQueryAdapter - 상품 평점 통계 Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, 상품 평점 통계 조회를 처리합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>상품 그룹 ID로 통계 조회 (findByProductGroupId)
 *   <li>통계 존재 여부 확인 (existsByProductGroupId)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정 금지 (PersistenceAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductRatingStatsQueryAdapter implements ProductRatingStatsQueryPort {

    private final ReviewQueryDslRepository queryDslRepository;
    private final ReviewJpaEntityMapper reviewJpaEntityMapper;

    public ProductRatingStatsQueryAdapter(
            ReviewQueryDslRepository queryDslRepository,
            ReviewJpaEntityMapper reviewJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.reviewJpaEntityMapper = reviewJpaEntityMapper;
    }

    /**
     * 상품 그룹 ID로 평점 통계 조회
     *
     * @param productGroupId 상품 그룹 ID
     * @return ProductRatingStats Domain (Optional)
     */
    @Override
    public Optional<ProductRatingStats> findByProductGroupId(Long productGroupId) {
        Optional<ProductRatingStatsJpaEntity> statsEntity =
                queryDslRepository.findStatsByProductGroupId(productGroupId);
        return statsEntity.map(reviewJpaEntityMapper::toStatsDomain);
    }

    /**
     * 상품 그룹 ID로 평점 통계 존재 여부 확인
     *
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    @Override
    public boolean existsByProductGroupId(Long productGroupId) {
        return queryDslRepository.existsStatsByProductGroupId(productGroupId);
    }
}
