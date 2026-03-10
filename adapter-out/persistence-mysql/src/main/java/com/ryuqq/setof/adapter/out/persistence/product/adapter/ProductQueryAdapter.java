package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductQueryDslRepository;
import com.ryuqq.setof.application.product.port.out.query.ProductQueryPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * ProductQueryAdapter - 상품 Query 어댑터.
 *
 * <p>ProductQueryPort를 구현하여 영속성 계층과 연결합니다.
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
public class ProductQueryAdapter implements ProductQueryPort {

    private final ProductQueryDslRepository queryDslRepository;
    private final ProductJpaEntityMapper mapper;

    public ProductQueryAdapter(
            ProductQueryDslRepository queryDslRepository, ProductJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * ID로 상품 조회.
     *
     * @param id 상품 ID
     * @return 상품 Optional
     */
    @Override
    public Optional<Product> findById(ProductId id) {
        return queryDslRepository
                .findById(id.value())
                .map(
                        entity -> {
                            List<ProductOptionMappingJpaEntity> mappings =
                                    queryDslRepository.findOptionMappingsByProductId(
                                            entity.getId());
                            return mapper.toDomain(entity, mappings);
                        });
    }

    /**
     * productGroupId로 상품 목록 조회.
     *
     * @param productGroupId 상품 그룹 ID
     * @return 상품 목록
     */
    @Override
    public List<Product> findByProductGroupId(ProductGroupId productGroupId) {
        List<ProductJpaEntity> entities =
                queryDslRepository.findByProductGroupId(productGroupId.value());
        return toDomainsWithBatchMappings(entities);
    }

    /**
     * ID 목록으로 상품 목록 조회.
     *
     * @param ids 상품 ID 목록
     * @return 상품 목록
     */
    @Override
    public List<Product> findByIds(List<ProductId> ids) {
        List<Long> idValues = ids.stream().map(ProductId::value).toList();
        List<ProductJpaEntity> entities = queryDslRepository.findByIdIn(idValues);
        return toDomainsWithBatchMappings(entities);
    }

    /**
     * productGroupId와 ID 목록으로 상품 목록 조회.
     *
     * @param productGroupId 상품 그룹 ID
     * @param ids 상품 ID 목록
     * @return 상품 목록
     */
    public List<Product> findByProductGroupIdAndIdIn(
            ProductGroupId productGroupId, List<ProductId> ids) {
        List<Long> idValues = ids.stream().map(ProductId::value).toList();
        List<ProductJpaEntity> entities =
                queryDslRepository.findByProductGroupIdAndIdIn(productGroupId.value(), idValues);
        return toDomainsWithBatchMappings(entities);
    }

    /**
     * productGroupId 목록으로 상품 목록 조회.
     *
     * @param productGroupIds 상품 그룹 ID 목록
     * @return 상품 목록
     */
    public List<Product> findByProductGroupIdIn(List<ProductGroupId> productGroupIds) {
        List<Long> idValues = productGroupIds.stream().map(ProductGroupId::value).toList();
        List<ProductJpaEntity> entities = queryDslRepository.findByProductGroupIdIn(idValues);
        return toDomainsWithBatchMappings(entities);
    }

    /**
     * 상품 목록을 배치로 옵션 매핑과 함께 도메인으로 변환합니다.
     *
     * <p>N+1 문제 방지: 상품 목록 조회 후 매핑을 한 번에 조회하여 productId 기준으로 그룹핑합니다.
     *
     * @param entities 상품 엔티티 목록
     * @return 상품 도메인 목록
     */
    private List<Product> toDomainsWithBatchMappings(List<ProductJpaEntity> entities) {
        if (entities.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = entities.stream().map(ProductJpaEntity::getId).toList();

        List<ProductOptionMappingJpaEntity> allMappings =
                queryDslRepository.findOptionMappingsByProductIds(productIds);

        Map<Long, List<ProductOptionMappingJpaEntity>> mappingsByProductId =
                allMappings.stream()
                        .collect(
                                Collectors.groupingBy(ProductOptionMappingJpaEntity::getProductId));

        return entities.stream()
                .map(
                        entity ->
                                mapper.toDomain(
                                        entity,
                                        mappingsByProductId.getOrDefault(
                                                entity.getId(), List.of())))
                .toList();
    }
}
