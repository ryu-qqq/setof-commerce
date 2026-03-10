package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.ProductGroupDescriptionJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.mapper.ProductGroupDescriptionJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository.ProductGroupDescriptionJpaRepository;
import com.ryuqq.setof.application.productdescription.port.out.command.ProductGroupDescriptionCommandPort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import org.springframework.stereotype.Component;

/**
 * ProductGroupDescriptionCommandAdapter - 상품그룹 상세설명 Command 어댑터.
 *
 * <p>ProductGroupDescriptionCommandPort를 구현하여 영속성 계층과 연결합니다.
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
 * @since 1.0.0
 */
@Component
public class ProductGroupDescriptionCommandAdapter implements ProductGroupDescriptionCommandPort {

    private final ProductGroupDescriptionJpaRepository jpaRepository;
    private final ProductGroupDescriptionJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 의존.
     *
     * @param jpaRepository JPA 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public ProductGroupDescriptionCommandAdapter(
            ProductGroupDescriptionJpaRepository jpaRepository,
            ProductGroupDescriptionJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 상세설명 저장.
     *
     * @param productGroupDescription ProductGroupDescription 도메인 객체
     * @return 저장된 상세설명 ID
     */
    @Override
    public Long persist(ProductGroupDescription productGroupDescription) {
        ProductGroupDescriptionJpaEntity entity = mapper.toEntity(productGroupDescription);
        ProductGroupDescriptionJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
