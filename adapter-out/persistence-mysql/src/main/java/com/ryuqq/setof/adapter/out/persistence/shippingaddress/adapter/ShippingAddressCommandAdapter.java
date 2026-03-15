package com.ryuqq.setof.adapter.out.persistence.shippingaddress.adapter;

import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.mapper.ShippingAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository.ShippingAddressJpaRepository;
import com.ryuqq.setof.application.shippingaddress.port.out.command.ShippingAddressCommandPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressCommandAdapter - 배송지 Command 어댑터.
 *
 * <p>ShippingAddressCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>활성화 조건: persistence.shippingaddress.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.shippingaddress.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class ShippingAddressCommandAdapter implements ShippingAddressCommandPort {

    private final ShippingAddressJpaRepository jpaRepository;
    private final ShippingAddressJpaEntityMapper mapper;

    public ShippingAddressCommandAdapter(
            ShippingAddressJpaRepository jpaRepository, ShippingAddressJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 배송지 저장.
     *
     * @param shippingAddress 배송지 도메인 객체
     * @return 저장된 배송지 ID
     */
    @Override
    public Long persist(ShippingAddress shippingAddress) {
        ShippingAddressJpaEntity entity = mapper.toEntity(shippingAddress);
        ShippingAddressJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
