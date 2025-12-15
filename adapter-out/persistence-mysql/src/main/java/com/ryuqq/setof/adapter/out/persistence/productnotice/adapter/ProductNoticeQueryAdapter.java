package com.ryuqq.setof.adapter.out.persistence.productnotice.adapter;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.mapper.ProductNoticeJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeQueryDslRepository;
import com.ryuqq.setof.application.productnotice.port.out.query.ProductNoticeQueryPort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.exception.ProductNoticeNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeQueryAdapter - ProductNotice Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, ProductNotice 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
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
     * ID로 상품고시 조회
     *
     * @param productNoticeId 상품고시 ID
     * @return 상품고시
     * @throws ProductNoticeNotFoundException 상품고시를 찾을 수 없는 경우
     */
    @Override
    public ProductNotice findById(Long productNoticeId) {
        ProductNoticeJpaEntity entity =
                queryDslRepository
                        .findById(productNoticeId)
                        .orElseThrow(() -> new ProductNoticeNotFoundException(productNoticeId));
        List<ProductNoticeItemJpaEntity> itemEntities =
                queryDslRepository.findItemsByProductNoticeId(productNoticeId);
        return mapper.toDomain(entity, itemEntities);
    }

    /**
     * 상품그룹 ID로 상품고시 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 (없으면 empty)
     */
    @Override
    public Optional<ProductNotice> findByProductGroupId(Long productGroupId) {
        return queryDslRepository
                .findByProductGroupId(productGroupId)
                .map(
                        entity -> {
                            List<ProductNoticeItemJpaEntity> itemEntities =
                                    queryDslRepository.findItemsByProductNoticeId(entity.getId());
                            return mapper.toDomain(entity, itemEntities);
                        });
    }
}
