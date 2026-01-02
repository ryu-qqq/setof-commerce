package com.ryuqq.setof.adapter.out.persistence.review.adapter;

import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.mapper.ReviewJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.review.repository.ReviewImageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.review.repository.ReviewJpaRepository;
import com.ryuqq.setof.application.review.port.out.command.ReviewPersistencePort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ReviewPersistenceAdapter - 리뷰 Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, 리뷰 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>리뷰 저장 (persist)
 *   <li>ReviewJpaEntity + ReviewImageJpaEntity 함께 저장
 *   <li>수정 시 기존 이미지 삭제 후 재생성
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewPersistenceAdapter implements ReviewPersistencePort {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewImageJpaRepository reviewImageJpaRepository;
    private final ReviewJpaEntityMapper reviewJpaEntityMapper;

    public ReviewPersistenceAdapter(
            ReviewJpaRepository reviewJpaRepository,
            ReviewImageJpaRepository reviewImageJpaRepository,
            ReviewJpaEntityMapper reviewJpaEntityMapper) {
        this.reviewJpaRepository = reviewJpaRepository;
        this.reviewImageJpaRepository = reviewImageJpaRepository;
        this.reviewJpaEntityMapper = reviewJpaEntityMapper;
    }

    /**
     * 리뷰 저장 (생성/수정)
     *
     * <p>리뷰와 이미지를 함께 저장합니다. 수정 시 기존 이미지를 삭제하고 새로 생성합니다.
     *
     * @param review 리뷰 도메인
     * @return 저장된 ReviewId
     */
    @Override
    public ReviewId persist(Review review) {
        // 기존 리뷰인 경우 이미지 삭제
        if (review.getId() != null) {
            reviewImageJpaRepository.deleteAllByReviewId(review.getId().getValue());
        }

        // 리뷰 Entity 저장
        ReviewJpaEntity reviewEntity = reviewJpaEntityMapper.toEntity(review);
        ReviewJpaEntity savedReviewEntity = reviewJpaRepository.save(reviewEntity);

        // 이미지 Entity 저장
        List<ReviewImageJpaEntity> imageEntities =
                reviewJpaEntityMapper.toImageEntities(review, savedReviewEntity.getId());
        if (!imageEntities.isEmpty()) {
            reviewImageJpaRepository.saveAll(imageEntities);
        }

        return ReviewId.of(savedReviewEntity.getId());
    }
}
