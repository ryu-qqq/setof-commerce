package com.ryuqq.setof.adapter.out.persistence.productnotice.adapter;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.mapper.ProductNoticeJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeQueryDslRepository;
import com.ryuqq.setof.application.productnotice.port.out.query.ProductNoticeQueryPort;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeQueryAdapter - 상품고시 Query 어댑터.
 *
 * <p>ProductNoticeQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductNoticeQueryAdapter implements ProductNoticeQueryPort {

    private final ProductNoticeQueryDslRepository queryDslRepository;
    private final ProductNoticeJpaEntityMapper mapper;

    public ProductNoticeQueryAdapter(
            ProductNoticeQueryDslRepository queryDslRepository,
            ProductNoticeJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 상품그룹 ID로 상품고시 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 Optional
     */
    @Override
    public Optional<ProductNotice> findByProductGroupId(ProductGroupId productGroupId) {
        Optional<ProductNoticeJpaEntity> entityOpt =
                queryDslRepository.findByProductGroupId(productGroupId.value());
        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }
        ProductNoticeJpaEntity entity = entityOpt.get();
        List<ProductNoticeEntryJpaEntity> entryEntities =
                queryDslRepository.findEntriesByProductNoticeId(entity.getId());
        return Optional.of(mapper.toDomain(entity, entryEntities));
    }
}
