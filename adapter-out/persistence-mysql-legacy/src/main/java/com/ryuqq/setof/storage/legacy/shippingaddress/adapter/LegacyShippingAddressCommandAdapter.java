package com.ryuqq.setof.storage.legacy.shippingaddress.adapter;

import com.ryuqq.setof.application.shippingaddress.port.out.command.ShippingAddressCommandPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.storage.legacy.shippingaddress.entity.LegacyShippingAddressEntity;
import com.ryuqq.setof.storage.legacy.shippingaddress.mapper.LegacyShippingAddressEntityMapper;
import com.ryuqq.setof.storage.legacy.shippingaddress.repository.LegacyShippingAddressJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyShippingAddressCommandAdapter - 레거시 배송지 명령 Adapter.
 *
 * <p>Application Layer의 CommandPort를 구현하는 Adapter입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyShippingAddressCommandAdapter implements ShippingAddressCommandPort {

    private final LegacyShippingAddressJpaRepository repository;
    private final LegacyShippingAddressEntityMapper mapper;

    public LegacyShippingAddressCommandAdapter(
            LegacyShippingAddressJpaRepository repository,
            LegacyShippingAddressEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(ShippingAddress shippingAddress) {
        LegacyShippingAddressEntity entity = mapper.toEntity(shippingAddress);
        LegacyShippingAddressEntity saved = repository.save(entity);
        return saved.getId();
    }
}
