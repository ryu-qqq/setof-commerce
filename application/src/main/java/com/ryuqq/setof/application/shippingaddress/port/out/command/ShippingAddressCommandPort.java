package com.ryuqq.setof.application.shippingaddress.port.out.command;

import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;

/**
 * ShippingAddressCommandPort - 배송지 명령 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ShippingAddressCommandPort {

    Long persist(ShippingAddress shippingAddress);
}
