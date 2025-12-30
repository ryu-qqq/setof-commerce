package com.ryuqq.setof.adapter.out.persistence.review.adapter;

import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.review.mapper.ReviewJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.review.repository.ReviewQueryDslRepository;
import com.ryuqq.setof.application.review.port.out.query.ReviewQueryPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.query.criteria.ReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * ReviewQueryAdapter - 리뷰 Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, 리뷰 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>Criteria 기반 목록 조회 (findByCriteria)
 *   <li>Criteria 기반 개수 조회 (countByCriteria)
 *   <li>중복 리뷰 여부 확인 (existsByOrderIdAndProductGroupId)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewQueryAdapter implements ReviewQueryPort {

    private final ReviewQueryDslRepository queryDslRepository;
    private final ReviewJpaEntityMapper reviewJpaEntityMapper;

    public ReviewQueryAdapter(
            ReviewQueryDslRepository queryDslRepository,
            ReviewJpaEntityMapper reviewJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.reviewJpaEntityMapper = reviewJpaEntityMapper;
    }

    /**
     * ID로 리뷰 단건 조회
     *
     * @param id 리뷰 ID (Value Object)
     * @return Review Domain (Optional)
     */
    @Override
    public Optional<Review> findById(ReviewId id) {
        Optional<ReviewJpaEntity> reviewEntity = queryDslRepository.findById(id.getValue());

        if (reviewEntity.isEmpty()) {
            return Optional.empty();
        }

        List<ReviewImageJpaEntity> imageEntities =
                queryDslRepository.findImagesByReviewId(id.getValue());

        return Optional.of(reviewJpaEntityMapper.toDomain(reviewEntity.get(), imageEntities));
    }

    /**
     * 검색 조건으로 리뷰 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Review 목록
     */
    @Override
    public List<Review> findByCriteria(ReviewSearchCriteria criteria) {
        List<ReviewJpaEntity> reviewEntities = queryDslRepository.findByCriteria(criteria);

        if (reviewEntities.isEmpty()) {
            return List.of();
        }

        return toDomainWithImages(reviewEntities);
    }

    /**
     * 검색 조건에 맞는 리뷰 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return 리뷰 총 개수
     */
    @Override
    public long countByCriteria(ReviewSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    /**
     * 주문 ID와 상품 그룹 ID로 리뷰 존재 여부 확인
     *
     * @param orderId 주문 ID
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    @Override
    public boolean existsByOrderIdAndProductGroupId(Long orderId, Long productGroupId) {
        return queryDslRepository.existsByOrderIdAndProductGroupId(orderId, productGroupId);
    }

    /**
     * 리뷰 ID로 존재 여부 확인
     *
     * @param id 리뷰 ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(ReviewId id) {
        return queryDslRepository.existsById(id.getValue());
    }

    /**
     * Review Entity 목록을 Domain으로 변환 (이미지 포함)
     *
     * @param reviewEntities Review Entity 목록
     * @return Review Domain 목록
     */
    private List<Review> toDomainWithImages(List<ReviewJpaEntity> reviewEntities) {
        // 이미지 일괄 조회 (N+1 방지)
        List<Long> reviewIds = reviewEntities.stream().map(ReviewJpaEntity::getId).toList();
        List<ReviewImageJpaEntity> allImages = queryDslRepository.findImagesByReviewIds(reviewIds);

        // 리뷰 ID별 이미지 그룹핑
        Map<Long, List<ReviewImageJpaEntity>> imagesByReviewId =
                allImages.stream()
                        .collect(Collectors.groupingBy(ReviewImageJpaEntity::getReviewId));

        // Domain 변환
        return reviewEntities.stream()
                .map(
                        entity -> {
                            List<ReviewImageJpaEntity> images =
                                    imagesByReviewId.getOrDefault(entity.getId(), List.of());
                            return reviewJpaEntityMapper.toDomain(entity, images);
                        })
                .toList();
    }
}
