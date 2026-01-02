package com.ryuqq.setof.application.review.port.out.command;

import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;

/**
 * ProductRatingStats Persistence Port (Command)
 *
 * <p>ProductRatingStats Aggregate를 영속화하는 쓰기 전용 Port
 */
public interface ProductRatingStatsPersistencePort {

    /**
     * ProductRatingStats 저장 (신규 생성 또는 수정)
     *
     * @param productRatingStats 저장할 ProductRatingStats (Domain Aggregate)
     */
    void persist(ProductRatingStats productRatingStats);
}
