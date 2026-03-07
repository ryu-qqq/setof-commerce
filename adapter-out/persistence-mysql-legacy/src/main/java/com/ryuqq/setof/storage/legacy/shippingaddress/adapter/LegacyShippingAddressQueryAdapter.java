package com.ryuqq.setof.storage.legacy.shippingaddress.adapter;

import com.ryuqq.setof.application.shippingaddress.port.out.ShippingAddressQueryPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.query.ShippingAddressSearchCondition;
import com.ryuqq.setof.storage.legacy.shippingaddress.entity.LegacyShippingAddressEntity;
import com.ryuqq.setof.storage.legacy.shippingaddress.mapper.LegacyShippingAddressEntityMapper;
import com.ryuqq.setof.storage.legacy.shippingaddress.repository.LegacyShippingAddressQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyShippingAddressQueryAdapter - 레거시 배송지 조회 Adapter.
 *
 * <p>Application Layer의 Port를 구현하는 Adapter입니다. Domain 객체만 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyShippingAddressQueryAdapter implements ShippingAddressQueryPort {

    private final LegacyShippingAddressQueryDslRepository repository;
    private final LegacyShippingAddressEntityMapper mapper;

    public LegacyShippingAddressQueryAdapter(
            LegacyShippingAddressQueryDslRepository repository,
            LegacyShippingAddressEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ShippingAddress> findById(Long userId, Long shippingAddressId) {
        ShippingAddressSearchCondition condition =
                ShippingAddressSearchCondition.of(userId, shippingAddressId);
        List<LegacyShippingAddressEntity> entities = repository.findByCondition(condition);
        return entities.stream().findFirst().map(mapper::toDomain);
    }

    @Override
    public List<ShippingAddress> findAllByUserId(Long userId) {
        ShippingAddressSearchCondition condition = ShippingAddressSearchCondition.ofUserId(userId);
        List<LegacyShippingAddressEntity> entities = repository.findByCondition(condition);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public int countByUserId(Long userId) {
        ShippingAddressSearchCondition condition = ShippingAddressSearchCondition.ofUserId(userId);
        return repository.findByCondition(condition).size();
    }
}
