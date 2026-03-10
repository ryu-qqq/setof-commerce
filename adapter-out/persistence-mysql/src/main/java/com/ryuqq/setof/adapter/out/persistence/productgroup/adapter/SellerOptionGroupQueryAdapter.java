package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionGroupQueryDslRepository;
import com.ryuqq.setof.application.selleroption.port.out.query.SellerOptionGroupQueryPort;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * SellerOptionGroupQueryAdapter - 셀러 옵션 그룹 Query 어댑터.
 *
 * <p>SellerOptionGroupQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerOptionGroupQueryAdapter implements SellerOptionGroupQueryPort {

    private final SellerOptionGroupQueryDslRepository queryDslRepository;
    private final ProductGroupJpaEntityMapper mapper;

    public SellerOptionGroupQueryAdapter(
            SellerOptionGroupQueryDslRepository queryDslRepository,
            ProductGroupJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 상품 그룹 ID로 셀러 옵션 그룹 목록을 조회합니다.
     *
     * @param productGroupId 상품 그룹 ID
     * @return 셀러 옵션 그룹 목록
     */
    @Override
    public List<SellerOptionGroup> findByProductGroupId(ProductGroupId productGroupId) {
        List<SellerOptionGroupJpaEntity> groups =
                queryDslRepository.findByProductGroupId(productGroupId.value());

        if (groups.isEmpty()) {
            return List.of();
        }

        List<Long> groupIds = groups.stream().map(SellerOptionGroupJpaEntity::getId).toList();

        List<SellerOptionValueJpaEntity> allValues =
                queryDslRepository.findValuesByGroupIds(groupIds);

        Map<Long, List<SellerOptionValueJpaEntity>> valuesByGroupId =
                allValues.stream()
                        .collect(
                                Collectors.groupingBy(
                                        SellerOptionValueJpaEntity::getSellerOptionGroupId));

        return groups.stream()
                .map(
                        g -> {
                            List<SellerOptionValue> values =
                                    valuesByGroupId.getOrDefault(g.getId(), List.of()).stream()
                                            .map(mapper::toOptionValueDomain)
                                            .toList();
                            return mapper.toOptionGroupDomain(g, values);
                        })
                .toList();
    }
}
