package com.ryuqq.setof.application.shippingaddress.port.in.query;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import java.util.UUID;

/**
 * Get ShippingAddress UseCase (Query)
 *
 * <p>배송지 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShippingAddressUseCase {

    /**
     * 배송지 단건 조회 실행
     *
     * @param memberId 회원 ID (소유권 검증용)
     * @param shippingAddressId 배송지 ID
     * @return 배송지 정보
     */
    ShippingAddressResponse execute(UUID memberId, Long shippingAddressId);
}
