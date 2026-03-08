package com.ryuqq.setof.application.review.validator;

import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.domain.review.exception.ReviewAlreadyWrittenException;
import org.springframework.stereotype.Component;

/**
 * ReviewRegistrationValidator - 리뷰 등록 검증.
 *
 * <p>동일 주문+회원에 활성 리뷰가 존재하면 등록을 차단합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewRegistrationValidator {

    private final ReviewReadManager readManager;

    public ReviewRegistrationValidator(ReviewReadManager readManager) {
        this.readManager = readManager;
    }

    public void validateNoDuplicate(long orderId, long userId) {
        boolean exists = readManager.existsActiveReviewByOrderAndUser(orderId, userId);

        if (exists) {
            throw new ReviewAlreadyWrittenException(orderId);
        }
    }
}
