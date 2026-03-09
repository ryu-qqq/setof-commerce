package com.ryuqq.setof.storage.legacy.composite.review.adapter;

import com.ryuqq.setof.application.review.port.out.query.ReviewCompositeQueryPort;
import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import com.ryuqq.setof.storage.legacy.composite.review.dto.LegacyWebAvailableReviewQueryDto;
import com.ryuqq.setof.storage.legacy.composite.review.mapper.LegacyWebAvailableReviewMapper;
import com.ryuqq.setof.storage.legacy.composite.review.repository.LegacyWebAvailableReviewCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web 작성 가능한 리뷰 주문 Composite 조회 Adapter.
 *
 * <p>Application Layer의 ReviewCompositeQueryPort를 구현합니다.
 *
 * <p>2단계 쿼리 패턴:
 *
 * <ol>
 *   <li>리뷰 가능 주문 ID 목록 조회 (커서 페이징)
 *   <li>주문 상세 조회 (order + payment + product + product_group + image + brand + seller JOIN)
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebAvailableReviewCompositeQueryAdapter implements ReviewCompositeQueryPort {

    private final LegacyWebAvailableReviewCompositeQueryDslRepository repository;
    private final LegacyWebAvailableReviewMapper mapper;

    public LegacyWebAvailableReviewCompositeQueryAdapter(
            LegacyWebAvailableReviewCompositeQueryDslRepository repository,
            LegacyWebAvailableReviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewableOrder> fetchAvailableReviewOrders(
            AvailableReviewSearchCriteria criteria) {
        Long userId = resolveUserId(criteria);
        List<Long> orderIds = repository.fetchAvailableReviewOrderIds(userId, criteria);

        if (orderIds.isEmpty()) {
            return List.of();
        }

        List<LegacyWebAvailableReviewQueryDto> dtos =
                repository.fetchAvailableReviewsByOrderIds(orderIds);

        return mapper.toDomainList(dtos);
    }

    @Override
    public long countAvailableReviewOrders(AvailableReviewSearchCriteria criteria) {
        Long userId = resolveUserId(criteria);
        return repository.countAvailableReviewOrders(userId);
    }

    private Long resolveUserId(AvailableReviewSearchCriteria criteria) {
        if (criteria.legacyMemberIdValue() != null) {
            return criteria.legacyMemberIdValue();
        }
        throw new IllegalStateException(
                "레거시 어댑터에서는 legacyMemberId가 필수입니다. memberId만으로는 조회할 수 없습니다.");
    }
}
