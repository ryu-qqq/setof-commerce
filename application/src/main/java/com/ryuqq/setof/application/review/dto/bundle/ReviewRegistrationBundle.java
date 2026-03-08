package com.ryuqq.setof.application.review.dto.bundle;

import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;

/**
 * ReviewRegistrationBundle - 리뷰 등록 번들.
 *
 * <p>Review + ReviewImages를 하나의 단위로 묶어 Facade에 전달합니다.
 *
 * @param review 리뷰 Aggregate
 * @param reviewImages 리뷰 이미지 일급 컬렉션
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewRegistrationBundle(Review review, ReviewImages reviewImages) {

    public ReviewRegistrationBundle {
        if (review == null) {
            throw new IllegalArgumentException("ReviewRegistrationBundle의 review는 null일 수 없습니다");
        }
        if (reviewImages == null) {
            throw new IllegalArgumentException(
                    "ReviewRegistrationBundle의 reviewImages는 null일 수 없습니다");
        }
    }
}
