package com.ryuqq.setof.adapter.out.persistence.shippingaddress.adapter;

import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.mapper.ShippingAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository.ShippingAddressJpaRepository;
import com.ryuqq.setof.application.shippingaddress.port.out.command.ShippingAddressPersistencePort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressPersistenceAdapter - ShippingAddress Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, ShippingAddress Domain을 영속화하는 Adapter입니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Domain -> Entity 변환
 *   <li>JpaRepository.save() 호출
 *   <li>저장된 ID 반환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 *   <li>@Transactional 금지 (Application Layer에서 처리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingAddressPersistenceAdapter implements ShippingAddressPersistencePort {

    private final ShippingAddressJpaRepository jpaRepository;
    private final ShippingAddressJpaEntityMapper mapper;

    public ShippingAddressPersistenceAdapter(
            ShippingAddressJpaRepository jpaRepository, ShippingAddressJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * ShippingAddress 저장 (신규 생성 또는 수정)
     *
     * @param shippingAddress 저장할 ShippingAddress (Domain Aggregate)
     * @return 저장된 ShippingAddress의 ID
     */
    @Override
    public ShippingAddressId persist(ShippingAddress shippingAddress) {
        ShippingAddressJpaEntity entity = mapper.toEntity(shippingAddress);
        ShippingAddressJpaEntity savedEntity = jpaRepository.save(entity);
        return ShippingAddressId.of(savedEntity.getId());
    }
}
