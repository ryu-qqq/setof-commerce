package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.DescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.ProductGroupDescriptionJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.mapper.ProductGroupDescriptionJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository.DescriptionImageQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository.ProductGroupDescriptionQueryDslRepository;
import com.ryuqq.setof.application.productdescription.port.out.query.ProductGroupDescriptionQueryPort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductGroupDescriptionQueryAdapter - 상품그룹 상세설명 Query 어댑터.
 *
 * <p>ProductGroupDescriptionQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupDescriptionQueryAdapter implements ProductGroupDescriptionQueryPort {

    private final ProductGroupDescriptionQueryDslRepository queryDslRepository;
    private final DescriptionImageQueryDslRepository imageQueryDslRepository;
    private final ProductGroupDescriptionJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-006: Mapper + QueryDslRepository 의존.
     *
     * @param queryDslRepository 상세설명 QueryDSL 레포지토리
     * @param imageQueryDslRepository 이미지 QueryDSL 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public ProductGroupDescriptionQueryAdapter(
            ProductGroupDescriptionQueryDslRepository queryDslRepository,
            DescriptionImageQueryDslRepository imageQueryDslRepository,
            ProductGroupDescriptionJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.imageQueryDslRepository = imageQueryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 상품그룹 ID로 상세설명 조회.
     *
     * <p>Assemble 패턴: 상세설명 조회 후 이미지를 배치 조회하여 조립합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 상세설명 Optional
     */
    @Override
    public Optional<ProductGroupDescription> findByProductGroupId(ProductGroupId productGroupId) {
        Optional<ProductGroupDescriptionJpaEntity> entityOpt =
                queryDslRepository.findByProductGroupId(productGroupId.value());

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        ProductGroupDescriptionJpaEntity entity = entityOpt.get();
        List<DescriptionImageJpaEntity> imageEntities =
                imageQueryDslRepository.findByProductGroupDescriptionId(entity.getId());

        return Optional.of(mapper.toDomain(entity, imageEntities));
    }
}
