package com.ryuqq.setof.adapter.out.persistence.productstock.adapter;

import com.ryuqq.setof.adapter.out.persistence.productstock.entity.ProductStockJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productstock.mapper.ProductStockJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productstock.repository.ProductStockJpaRepository;
import com.ryuqq.setof.application.productstock.port.out.command.ProductStockPersistencePort;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import com.ryuqq.setof.domain.productstock.exception.StockOptimisticLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

/**
 * ProductStockPersistenceAdapter - ProductStock Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, ProductStock 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductStockPersistenceAdapter implements ProductStockPersistencePort {

    private final ProductStockJpaRepository jpaRepository;
    private final ProductStockJpaEntityMapper mapper;

    public ProductStockPersistenceAdapter(
            ProductStockJpaRepository jpaRepository, ProductStockJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * ProductStock 저장 (신규 생성)
     *
     * @param productStock 저장할 재고
     * @return 저장된 재고 ID
     */
    @Override
    public Long save(ProductStock productStock) {
        ProductStockJpaEntity entity = mapper.toEntity(productStock);
        ProductStockJpaEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.getId();
    }

    /**
     * ProductStock 업데이트
     *
     * <p>낙관적 락 충돌 발생 시 {@link StockOptimisticLockException}을 던집니다.
     *
     * @param productStock 업데이트할 재고
     * @throws StockOptimisticLockException 동시 수정 충돌 시
     */
    @Override
    public void update(ProductStock productStock) {
        try {
            ProductStockJpaEntity entity = mapper.toEntity(productStock);
            jpaRepository.saveAndFlush(entity);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new StockOptimisticLockException(productStock.getProductIdValue());
        }
    }
}
