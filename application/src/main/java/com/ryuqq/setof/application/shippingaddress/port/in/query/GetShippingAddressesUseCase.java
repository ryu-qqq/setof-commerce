package com.ryuqq.setof.application.shippingaddress.port.in.query;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import java.util.List;
import java.util.UUID;

/**
 * Get ShippingAddresses UseCase (Query)
 *
 * <p>회원의 배송지 목록을 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShippingAddressesUseCase {

    /**
     * 회원의 배송지 목록 조회 실행
     *
     * @param memberId 회원 ID
     * @return 배송지 목록 (최근 등록순)
     */
    List<ShippingAddressResponse> execute(UUID memberId);
}
