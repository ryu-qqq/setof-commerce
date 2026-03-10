package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionValueJpaRepository;
import com.ryuqq.setof.application.selleroption.port.out.command.SellerOptionValueCommandPort;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerOptionValueCommandAdapter - 셀러 옵션 값 Command 어댑터.
 *
 * <p>SellerOptionValueCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerOptionValueCommandAdapter implements SellerOptionValueCommandPort {

    private final SellerOptionValueJpaRepository jpaRepository;
    private final ProductGroupJpaEntityMapper mapper;

    public SellerOptionValueCommandAdapter(
            SellerOptionValueJpaRepository jpaRepository, ProductGroupJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 셀러 옵션 값을 저장합니다.
     *
     * @param sellerOptionValue 저장할 셀러 옵션 값 도메인 객체
     * @return 저장된 셀러 옵션 값 ID
     */
    @Override
    public Long persist(SellerOptionValue sellerOptionValue) {
        SellerOptionValueJpaEntity entity = mapper.toOptionValueEntity(sellerOptionValue);
        SellerOptionValueJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    /**
     * 셀러 옵션 값 목록을 일괄 저장합니다.
     *
     * @param sellerOptionValues 저장할 셀러 옵션 값 도메인 객체 목록
     * @return 저장된 셀러 옵션 값 ID 목록
     */
    @Override
    public List<Long> persistAll(List<SellerOptionValue> sellerOptionValues) {
        List<SellerOptionValueJpaEntity> entities =
                sellerOptionValues.stream().map(mapper::toOptionValueEntity).toList();
        List<SellerOptionValueJpaEntity> saved = jpaRepository.saveAll(entities);
        return saved.stream().map(SellerOptionValueJpaEntity::getId).toList();
    }

    /**
     * 특정 옵션 그룹에 속한 셀러 옵션 값 목록을 일괄 저장합니다.
     *
     * @param sellerOptionGroupId 셀러 옵션 그룹 ID
     * @param sellerOptionValues 저장할 셀러 옵션 값 도메인 객체 목록
     * @return 저장된 셀러 옵션 값 ID 목록
     */
    @Override
    public List<Long> persistAllForGroup(
            Long sellerOptionGroupId, List<SellerOptionValue> sellerOptionValues) {
        List<SellerOptionValueJpaEntity> entities =
                sellerOptionValues.stream()
                        .map(v -> mapper.toOptionValueEntity(v, sellerOptionGroupId))
                        .toList();
        List<SellerOptionValueJpaEntity> saved = jpaRepository.saveAll(entities);
        return saved.stream().map(SellerOptionValueJpaEntity::getId).toList();
    }
}
