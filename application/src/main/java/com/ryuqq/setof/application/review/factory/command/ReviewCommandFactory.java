package com.ryuqq.setof.application.review.factory.command;

import com.ryuqq.setof.application.review.dto.command.CreateReviewCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.review.vo.ReviewImage;
import com.ryuqq.setof.domain.review.vo.ReviewImages;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

/**
 * Review Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성 로직 캡슐화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewCommandFactory {

    private final ClockHolder clockHolder;

    public ReviewCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 리뷰 생성
     *
     * @param command 리뷰 생성 커맨드
     * @return 생성된 Review (저장 전)
     */
    public Review create(CreateReviewCommand command) {
        Rating rating = Rating.of(command.rating());
        ReviewContent content = createContent(command.content());
        ReviewImages images = createImages(command.imageUrls());

        return Review.create(
                command.memberId(),
                command.orderId(),
                command.productGroupId(),
                rating,
                content,
                images,
                clockHolder.getClock());
    }

    private ReviewContent createContent(String content) {
        if (content == null || content.isBlank()) {
            return ReviewContent.empty();
        }
        return ReviewContent.of(content);
    }

    private ReviewImages createImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return ReviewImages.empty();
        }

        List<ReviewImage> images =
                IntStream.range(0, imageUrls.size())
                        .mapToObj(i -> ReviewImage.photo(imageUrls.get(i), i))
                        .toList();

        return ReviewImages.of(images);
    }
}
