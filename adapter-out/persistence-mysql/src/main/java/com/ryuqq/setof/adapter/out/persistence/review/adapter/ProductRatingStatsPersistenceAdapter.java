package com.ryuqq.setof.adapter.out.persistence.review.adapter;

import com.ryuqq.setof.adapter.out.persistence.review.entity.ProductRatingStatsJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.mapper.ReviewJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.review.repository.ProductRatingStatsJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.review.repository.ReviewQueryDslRepository;
import com.ryuqq.setof.application.review.port.out.command.ProductRatingStatsPersistencePort;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductRatingStatsPersistenceAdapter - 상품 평점 통계 Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, 상품 평점 통계 저장을 처리합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>통계 저장 (persist)
 *   <li>기존 통계가 있으면 업데이트, 없으면 신규 생성
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
public class ProductRatingStatsPersistenceAdapter implements ProductRatingStatsPersistencePort {

    private final ProductRatingStatsJpaRepository statsJpaRepository;
    private final ReviewQueryDslRepository queryDslRepository;
    private final ReviewJpaEntityMapper reviewJpaEntityMapper;

    public ProductRatingStatsPersistenceAdapter(
            ProductRatingStatsJpaRepository statsJpaRepository,
            ReviewQueryDslRepository queryDslRepository,
            ReviewJpaEntityMapper reviewJpaEntityMapper) {
        this.statsJpaRepository = statsJpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.reviewJpaEntityMapper = reviewJpaEntityMapper;
    }

    /**
     * 상품 평점 통계 저장 (생성/수정)
     *
     * <p>기존 통계가 있으면 업데이트하고, 없으면 신규 생성합니다.
     *
     * @param stats 상품 평점 통계 도메인
     */
    @Override
    public void persist(ProductRatingStats stats) {
        Optional<ProductRatingStatsJpaEntity> existingEntity =
                queryDslRepository.findStatsByProductGroupId(stats.getProductGroupId());

        ProductRatingStatsJpaEntity entityToSave;
        if (existingEntity.isPresent()) {
            ProductRatingStatsJpaEntity existing = existingEntity.get();
            entityToSave =
                    reviewJpaEntityMapper.toStatsEntity(
                            stats, existing.getId(), existing.getCreatedAt());
        } else {
            entityToSave = reviewJpaEntityMapper.toStatsEntity(stats);
        }

        statsJpaRepository.save(entityToSave);
    }
}
