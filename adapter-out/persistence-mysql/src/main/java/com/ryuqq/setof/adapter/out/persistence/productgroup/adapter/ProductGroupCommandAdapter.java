package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.application.productgroup.port.out.command.ProductGroupCommandPort;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import org.springframework.stereotype.Component;

/**
 * ProductGroupCommandAdapter - 상품 그룹 Command 어댑터.
 *
 * <p>ProductGroupCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupCommandAdapter implements ProductGroupCommandPort {

    private final ProductGroupJpaRepository jpaRepository;
    private final ProductGroupJpaEntityMapper mapper;

    public ProductGroupCommandAdapter(
            ProductGroupJpaRepository jpaRepository, ProductGroupJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 상품 그룹을 저장합니다.
     *
     * @param productGroup 저장할 상품 그룹 도메인 객체
     * @return 저장된 상품 그룹 ID
     */
    @Override
    public Long persist(ProductGroup productGroup) {
        ProductGroupJpaEntity entity = mapper.toEntity(productGroup);
        ProductGroupJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
