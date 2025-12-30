package com.ryuqq.setof.application.review.assembler;

import com.ryuqq.setof.application.review.dto.response.ProductRatingStatsResponse;
import com.ryuqq.setof.application.review.dto.response.ReviewImageResponse;
import com.ryuqq.setof.application.review.dto.response.ReviewResponse;
import com.ryuqq.setof.application.review.dto.response.ReviewSummaryResponse;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import com.ryuqq.setof.domain.review.aggregate.Review;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Review Assembler
 *
 * <p>Domain 객체 → Response DTO 변환을 담당
 *
 * <p>주의: toDomain은 CommandFactory 책임입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewAssembler {

    /**
     * Review 도메인을 ReviewResponse로 변환
     *
     * @param review Review 도메인 객체
     * @return ReviewResponse
     */
    public ReviewResponse toReviewResponse(Review review) {
        List<ReviewImageResponse> images =
                review.getImageList().stream()
                        .map(
                                img ->
                                        ReviewImageResponse.of(
                                                img.getImageUrl(), img.getDisplayOrder()))
                        .toList();

        return ReviewResponse.of(
                review.getId().getValue(),
                review.getMemberId(),
                review.getOrderId(),
                review.getProductGroupId(),
                review.getRatingValue(),
                review.getContent().getValue(),
                images,
                review.getCreatedAt(),
                review.getUpdatedAt());
    }

    /**
     * Review 도메인을 ReviewSummaryResponse로 변환
     *
     * @param review Review 도메인 객체
     * @return ReviewSummaryResponse
     */
    public ReviewSummaryResponse toReviewSummaryResponse(Review review) {
        return ReviewSummaryResponse.of(
                review.getId().getValue(),
                review.getMemberId(),
                review.getRatingValue(),
                review.getContent().getValue(),
                review.hasImages(),
                review.getImageList().size(),
                review.getCreatedAt());
    }

    /**
     * Review 도메인 목록을 ReviewSummaryResponse 목록으로 변환
     *
     * @param reviews Review 도메인 목록
     * @return ReviewSummaryResponse 목록
     */
    public List<ReviewSummaryResponse> toReviewSummaryResponses(List<Review> reviews) {
        return reviews.stream().map(this::toReviewSummaryResponse).toList();
    }

    /**
     * ProductRatingStats 도메인을 ProductRatingStatsResponse로 변환
     *
     * @param stats ProductRatingStats 도메인 객체
     * @return ProductRatingStatsResponse
     */
    public ProductRatingStatsResponse toProductRatingStatsResponse(ProductRatingStats stats) {
        return ProductRatingStatsResponse.of(
                stats.getProductGroupId(), stats.getAverageRating(), (int) stats.getReviewCount());
    }
}
