package com.ryuqq.setof.application.review.manager;

import com.ryuqq.setof.application.review.port.out.command.ReviewCommandPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import org.springframework.stereotype.Component;

/**
 * ReviewCommandManager - 리뷰 명령 Manager.
 *
 * <p>ReviewCommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewCommandManager {

    private final ReviewCommandPort commandPort;

    public ReviewCommandManager(ReviewCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(Review review) {
        return commandPort.persist(review);
    }
}
