package com.ryuqq.setof.application.review.manager;

import com.ryuqq.setof.application.review.port.out.command.ReviewImageCommandPort;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ReviewImageCommandManager - 리뷰 이미지 명령 Manager.
 *
 * <p>ReviewImageCommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewImageCommandManager {

    private final ReviewImageCommandPort commandPort;

    public ReviewImageCommandManager(ReviewImageCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(ReviewImage reviewImage) {
        return commandPort.persist(reviewImage);
    }

    public void persistAll(List<ReviewImage> reviewImages) {
        commandPort.persistAll(reviewImages);
    }

    public void deleteByReviewId(long reviewId) {
        commandPort.deleteByReviewId(reviewId);
    }
}
