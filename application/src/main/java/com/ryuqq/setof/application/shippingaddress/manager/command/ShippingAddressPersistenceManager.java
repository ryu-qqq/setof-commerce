package com.ryuqq.setof.application.shippingaddress.manager.command;

import com.ryuqq.setof.application.shippingaddress.port.out.command.ShippingAddressPersistencePort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ShippingAddress Persistence Manager
 *
 * <p>ShippingAddress 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingAddressPersistenceManager {

    private final ShippingAddressPersistencePort shippingAddressPersistencePort;

    public ShippingAddressPersistenceManager(
            ShippingAddressPersistencePort shippingAddressPersistencePort) {
        this.shippingAddressPersistencePort = shippingAddressPersistencePort;
    }

    /**
     * ShippingAddress 저장
     *
     * <p>JPA dirty checking으로 수정 시에도 persist 호출로 처리
     *
     * @param shippingAddress 저장할 ShippingAddress
     * @return 저장된 ShippingAddress의 ID
     */
    @Transactional
    public ShippingAddressId persist(ShippingAddress shippingAddress) {
        return shippingAddressPersistencePort.persist(shippingAddress);
    }
}
