package com.ryuqq.setof.application.shipment.port.in.query;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import java.util.Optional;

/**
 * Get Shipment By Invoice UseCase (Query)
 *
 * <p>택배사 코드 + 운송장 번호로 운송장 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShipmentByInvoiceUseCase {

    /**
     * 택배사 ID + 운송장 번호로 운송장 조회
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @return 조회된 Shipment (Optional)
     */
    Optional<Shipment> execute(Long carrierId, String invoiceNumber);
}
