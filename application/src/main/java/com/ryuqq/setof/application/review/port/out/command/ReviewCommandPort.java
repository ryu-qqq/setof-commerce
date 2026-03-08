package com.ryuqq.setof.application.review.port.out.command;

import com.ryuqq.setof.domain.review.aggregate.Review;

/**
 * ReviewCommandPort - 리뷰 명령 출력 포트.
 *
 * <p>Adapter(Persistence Layer)가 구현할 인터페이스입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ReviewCommandPort {

    /**
     * 리뷰 저장 (신규 등록 및 수정).
     *
     * @param review 리뷰 Aggregate
     * @return 저장된 레거시 리뷰 ID
     */
    Long persist(Review review);
}
