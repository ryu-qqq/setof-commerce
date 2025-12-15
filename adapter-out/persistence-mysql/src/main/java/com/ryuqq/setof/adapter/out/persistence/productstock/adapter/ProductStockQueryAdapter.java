package com.ryuqq.setof.adapter.out.persistence.productstock.adapter;

import com.ryuqq.setof.adapter.out.persistence.productstock.mapper.ProductStockJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productstock.repository.ProductStockQueryDslRepository;
import com.ryuqq.setof.application.productstock.port.out.query.ProductStockQueryPort;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductStockQueryAdapter - ProductStock Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, ProductStock 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductStockQueryAdapter implements ProductStockQueryPort {

    private final ProductStockQueryDslRepository queryDslRepository;
    private final ProductStockJpaEntityMapper mapper;

    public ProductStockQueryAdapter(
            ProductStockQueryDslRepository queryDslRepository, ProductStockJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 상품 ID로 재고 조회
     *
     * @param productId 상품 ID
     * @return 재고 (없으면 empty)
     */
    @Override
    public Optional<ProductStock> findByProductId(Long productId) {
        return queryDslRepository.findByProductId(productId).map(mapper::toDomain);
    }

    /**
     * 여러 상품의 재고 일괄 조회
     *
     * @param productIds 상품 ID 목록
     * @return 재고 목록
     */
    @Override
    public List<ProductStock> findByProductIds(List<Long> productIds) {
        return queryDslRepository.findByProductIds(productIds).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * 재고 ID로 조회
     *
     * @param productStockId 재고 ID
     * @return 재고 (없으면 empty)
     */
    @Override
    public Optional<ProductStock> findById(Long productStockId) {
        return queryDslRepository.findById(productStockId).map(mapper::toDomain);
    }
}
