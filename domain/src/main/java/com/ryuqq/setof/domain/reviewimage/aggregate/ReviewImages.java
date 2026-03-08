package com.ryuqq.setof.domain.reviewimage.aggregate;

import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.reviewimage.exception.ReviewImageLimitExceededException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 리뷰 이미지 일급 컬렉션.
 *
 * <p>리뷰에 첨부 가능한 이미지는 최대 3장입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReviewImages {

    private static final int MAX_IMAGE_COUNT = 3;

    private final List<ReviewImage> images;

    private ReviewImages(List<ReviewImage> images) {
        if (images != null && images.size() > MAX_IMAGE_COUNT) {
            throw new ReviewImageLimitExceededException();
        }
        this.images = new ArrayList<>(images != null ? images : List.of());
    }

    public static ReviewImages of(List<ReviewImage> images) {
        return new ReviewImages(images);
    }

    public static ReviewImages empty() {
        return new ReviewImages(List.of());
    }

    /**
     * 이미지 추가.
     *
     * @param image 추가할 이미지
     * @throws ReviewImageLimitExceededException 이미지 수가 최대 3장을 초과하는 경우
     */
    public void add(ReviewImage image) {
        if (this.images.size() >= MAX_IMAGE_COUNT) {
            throw new ReviewImageLimitExceededException();
        }
        this.images.add(image);
    }

    /**
     * 모든 이미지에 reviewId를 부여한 새 컬렉션을 반환합니다.
     *
     * @param reviewId 리뷰 ID
     * @return reviewId가 세팅된 새 ReviewImages
     */
    public ReviewImages withReviewId(ReviewId reviewId) {
        List<ReviewImage> bound =
                images.stream()
                        .map(
                                img ->
                                        ReviewImage.create(
                                                reviewId,
                                                img.imageInfo(),
                                                img.displayOrder(),
                                                img.createdAt()))
                        .toList();
        return new ReviewImages(bound);
    }

    /**
     * 모든 이미지를 소프트 삭제합니다.
     *
     * @param occurredAt 삭제 시각
     */
    public void deleteAll(Instant occurredAt) {
        images.forEach(image -> image.delete(occurredAt));
    }

    public List<ReviewImage> toList() {
        return Collections.unmodifiableList(images);
    }

    public int size() {
        return images.size();
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }
}
