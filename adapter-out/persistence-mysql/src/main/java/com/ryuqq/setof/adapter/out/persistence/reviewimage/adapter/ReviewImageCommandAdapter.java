package com.ryuqq.setof.adapter.out.persistence.reviewimage.adapter;

import com.ryuqq.setof.adapter.out.persistence.reviewimage.entity.ReviewImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.reviewimage.mapper.ReviewImageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.reviewimage.repository.ReviewImageJpaRepository;
import com.ryuqq.setof.application.review.port.out.command.ReviewImageCommandPort;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ReviewImageCommandAdapter - 리뷰 이미지 Command 어댑터.
 *
 * <p>ReviewImageCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewImageCommandAdapter implements ReviewImageCommandPort {

    private final ReviewImageJpaRepository jpaRepository;
    private final ReviewImageJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 의존.
     *
     * @param jpaRepository JPA 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public ReviewImageCommandAdapter(
            ReviewImageJpaRepository jpaRepository, ReviewImageJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 리뷰 이미지 저장.
     *
     * @param reviewImage ReviewImage 도메인 객체
     * @return 저장된 리뷰 이미지 ID
     */
    @Override
    public Long persist(ReviewImage reviewImage) {
        ReviewImageJpaEntity entity = mapper.toEntity(reviewImage);
        ReviewImageJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    /**
     * 리뷰 이미지 일괄 저장.
     *
     * @param reviewImages ReviewImage 도메인 객체 목록
     */
    @Override
    public void persistAll(List<ReviewImage> reviewImages) {
        List<ReviewImageJpaEntity> entities = reviewImages.stream().map(mapper::toEntity).toList();
        jpaRepository.saveAll(entities);
    }

    /**
     * 리뷰 ID 기준으로 이미지 삭제.
     *
     * <p>TODO: QueryDslRepository로 구현 예정 (새 스키마 전환 시)
     *
     * @param reviewId 리뷰 ID
     */
    @Override
    public void deleteByReviewId(long reviewId) {
        throw new UnsupportedOperationException("새 스키마 전환 전까지 레거시 어댑터를 사용합니다");
    }
}
