package com.ryuqq.setof.application.shipment.port.in.query;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import java.util.List;

/**
 * Get Shipments By Checkout UseCase (Query)
 *
 * <p>결제건 ID로 운송장 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShipmentsByCheckoutUseCase {

    /**
     * 결제건 ID로 운송장 목록 조회
     *
     * @param checkoutId 결제건 ID
     * @return 해당 결제건의 운송장 목록
     */
    List<Shipment> getByCheckoutId(Long checkoutId);
}
