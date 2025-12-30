package com.ryuqq.setof.application.shipment.port.in.query;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import java.util.List;

/**
 * Get Shipments By Seller UseCase (Query)
 *
 * <p>셀러 ID로 운송장 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShipmentsBySellerUseCase {

    /**
     * 셀러 ID로 운송장 목록 조회
     *
     * @param sellerId 셀러 ID
     * @return 해당 셀러의 운송장 목록
     */
    List<Shipment> getBySellerId(Long sellerId);
}
