package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.SellerOptionGroupJpaRepository;
import com.ryuqq.setof.application.selleroption.port.out.command.SellerOptionGroupCommandPort;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerOptionGroupCommandAdapter - 셀러 옵션 그룹 Command 어댑터.
 *
 * <p>SellerOptionGroupCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerOptionGroupCommandAdapter implements SellerOptionGroupCommandPort {

    private final SellerOptionGroupJpaRepository jpaRepository;
    private final ProductGroupJpaEntityMapper mapper;

    public SellerOptionGroupCommandAdapter(
            SellerOptionGroupJpaRepository jpaRepository, ProductGroupJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 셀러 옵션 그룹을 저장합니다.
     *
     * @param sellerOptionGroup 저장할 셀러 옵션 그룹 도메인 객체
     * @return 저장된 셀러 옵션 그룹 ID
     */
    @Override
    public Long persist(SellerOptionGroup sellerOptionGroup) {
        SellerOptionGroupJpaEntity entity = mapper.toOptionGroupEntity(sellerOptionGroup);
        SellerOptionGroupJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    /**
     * 셀러 옵션 그룹 목록을 일괄 저장합니다.
     *
     * @param sellerOptionGroups 저장할 셀러 옵션 그룹 도메인 객체 목록
     */
    @Override
    public void persistAll(List<SellerOptionGroup> sellerOptionGroups) {
        List<SellerOptionGroupJpaEntity> entities =
                sellerOptionGroups.stream().map(mapper::toOptionGroupEntity).toList();
        jpaRepository.saveAll(entities);
    }
}
