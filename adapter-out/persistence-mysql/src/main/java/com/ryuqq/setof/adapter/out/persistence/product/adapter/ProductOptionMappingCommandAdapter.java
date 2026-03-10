package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductOptionMappingJpaRepository;
import com.ryuqq.setof.application.product.port.out.command.ProductOptionMappingCommandPort;
import com.ryuqq.setof.domain.product.aggregate.ProductOptionMapping;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductOptionMappingCommandAdapter - 상품 옵션 매핑 Command 어댑터.
 *
 * <p>ProductOptionMappingCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductOptionMappingCommandAdapter implements ProductOptionMappingCommandPort {

    private final ProductOptionMappingJpaRepository jpaRepository;
    private final ProductJpaEntityMapper mapper;

    public ProductOptionMappingCommandAdapter(
            ProductOptionMappingJpaRepository jpaRepository, ProductJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 옵션 매핑 목록 일괄 저장.
     *
     * @param mappings 옵션 매핑 도메인 객체 목록
     */
    @Override
    public void persistAll(List<ProductOptionMapping> mappings) {
        List<ProductOptionMappingJpaEntity> entities =
                mappings.stream().map(mapper::toMappingEntity).toList();
        jpaRepository.saveAll(entities);
    }

    /**
     * 특정 상품의 옵션 매핑 전체 교체 저장.
     *
     * <p>기존 매핑을 삭제하고 새로운 매핑 목록을 저장합니다.
     *
     * @param productId 상품 ID
     * @param mappings 새로운 옵션 매핑 목록
     */
    @Override
    public void persistAllForProduct(Long productId, List<ProductOptionMapping> mappings) {
        jpaRepository.deleteByProductId(productId);
        List<ProductOptionMappingJpaEntity> entities =
                mappings.stream().map(m -> mapper.toMappingEntity(m, productId)).toList();
        jpaRepository.saveAll(entities);
    }
}
