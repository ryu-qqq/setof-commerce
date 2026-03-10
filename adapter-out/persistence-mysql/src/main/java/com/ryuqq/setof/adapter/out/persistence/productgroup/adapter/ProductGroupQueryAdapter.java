package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupQueryPort;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * ProductGroupQueryAdapter - 상품 그룹 Query 어댑터.
 *
 * <p>ProductGroupQueryPort를 구현하여 영속성 계층과 연결합니다.
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
public class ProductGroupQueryAdapter implements ProductGroupQueryPort {

    private final ProductGroupQueryDslRepository queryDslRepository;
    private final ProductGroupJpaEntityMapper mapper;

    public ProductGroupQueryAdapter(
            ProductGroupQueryDslRepository queryDslRepository, ProductGroupJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * ID로 상품 그룹을 조회합니다.
     *
     * @param id 상품 그룹 ID
     * @return 상품 그룹 Optional
     */
    @Override
    public Optional<ProductGroup> findById(ProductGroupId id) {
        return queryDslRepository
                .findById(id.value())
                .map(
                        entity -> {
                            List<SellerOptionGroupJpaEntity> groups =
                                    queryDslRepository.findOptionGroupsByProductGroupId(
                                            entity.getId());
                            List<Long> groupIds =
                                    groups.stream().map(SellerOptionGroupJpaEntity::getId).toList();
                            List<SellerOptionValueJpaEntity> values =
                                    queryDslRepository.findOptionValuesByOptionGroupIds(groupIds);
                            return mapper.toDomain(entity, List.of(), groups, values);
                        });
    }

    /**
     * ID 목록으로 상품 그룹 목록을 조회합니다.
     *
     * @param ids 상품 그룹 ID 목록
     * @return 상품 그룹 목록
     */
    @Override
    public List<ProductGroup> findByIds(List<ProductGroupId> ids) {
        List<Long> idValues = ids.stream().map(ProductGroupId::value).toList();
        List<ProductGroupJpaEntity> entities = queryDslRepository.findByIds(idValues);

        if (entities.isEmpty()) {
            return List.of();
        }

        List<Long> entityIds = entities.stream().map(ProductGroupJpaEntity::getId).toList();

        List<SellerOptionGroupJpaEntity> allGroups =
                queryDslRepository.findOptionGroupsByProductGroupIds(entityIds);

        List<Long> allGroupIds = allGroups.stream().map(SellerOptionGroupJpaEntity::getId).toList();

        List<SellerOptionValueJpaEntity> allValues =
                queryDslRepository.findOptionValuesByOptionGroupIds(allGroupIds);

        Map<Long, List<SellerOptionGroupJpaEntity>> groupsByProductGroupId =
                allGroups.stream()
                        .collect(
                                Collectors.groupingBy(
                                        SellerOptionGroupJpaEntity::getProductGroupId));

        Map<Long, List<SellerOptionValueJpaEntity>> valuesByGroupId =
                allValues.stream()
                        .collect(
                                Collectors.groupingBy(
                                        SellerOptionValueJpaEntity::getSellerOptionGroupId));

        return entities.stream()
                .map(
                        entity -> {
                            List<SellerOptionGroupJpaEntity> groups =
                                    groupsByProductGroupId.getOrDefault(entity.getId(), List.of());
                            List<SellerOptionValueJpaEntity> values =
                                    groups.stream()
                                            .flatMap(
                                                    g ->
                                                            valuesByGroupId
                                                                    .getOrDefault(
                                                                            g.getId(), List.of())
                                                                    .stream())
                                            .toList();
                            return mapper.toDomain(entity, List.of(), groups, values);
                        })
                .toList();
    }

    /**
     * ProductGroupImageJpaEntity 목록을 productGroupId 기준으로 그룹화.
     *
     * @param imageEntities 이미지 엔티티 목록
     * @return productGroupId → 이미지 목록 맵
     */
    private Map<Long, List<ProductGroupImageJpaEntity>> groupImagesByProductGroupId(
            List<ProductGroupImageJpaEntity> imageEntities) {
        return imageEntities.stream()
                .collect(Collectors.groupingBy(ProductGroupImageJpaEntity::getProductGroupId));
    }
}
