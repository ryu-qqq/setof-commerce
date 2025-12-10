package com.ryuqq.setof.application.shippingaddress.port.out.command;

import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;

/**
 * ShippingAddress Persistence Port (Command)
 *
 * <p>ShippingAddress Aggregate를 저장하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShippingAddressPersistencePort {

    /**
     * 배송지 저장 (신규 등록 또는 수정)
     *
     * <p>ID가 없으면 신규 등록, 있으면 수정
     *
     * @param shippingAddress 저장할 배송지 도메인 객체
     * @return 저장된 배송지 ID
     */
    ShippingAddressId persist(ShippingAddress shippingAddress);
}
