package com.ryuqq.setof.application.review.port.out.command;

import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import java.util.List;

/**
 * ReviewImageCommandPort - 리뷰 이미지 명령 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ReviewImageCommandPort {

    /**
     * 리뷰 이미지를 저장합니다.
     *
     * @param reviewImage 저장할 리뷰 이미지 도메인 객체
     * @return 리뷰 이미지 ID
     */
    Long persist(ReviewImage reviewImage);

    /**
     * 리뷰 이미지를 일괄 저장합니다.
     *
     * @param reviewImages 저장할 리뷰 이미지 도메인 객체 목록
     */
    void persistAll(List<ReviewImage> reviewImages);

    /**
     * 리뷰 ID 기준으로 이미지를 삭제합니다.
     *
     * @param reviewId 리뷰 ID
     */
    void deleteByReviewId(long reviewId);
}
