package com.ryuqq.setof.application.shipment.service.query;

import com.ryuqq.setof.application.shipment.manager.query.ShipmentReadManager;
import com.ryuqq.setof.application.shipment.port.in.query.GetActiveShipmentsUseCase;
import com.ryuqq.setof.application.shipment.port.in.query.GetShipmentByInvoiceUseCase;
import com.ryuqq.setof.application.shipment.port.in.query.GetShipmentUseCase;
import com.ryuqq.setof.application.shipment.port.in.query.GetShipmentsByCheckoutUseCase;
import com.ryuqq.setof.application.shipment.port.in.query.GetShipmentsBySellerUseCase;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 운송장 조회 서비스
 *
 * <p>다양한 조건으로 운송장을 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ShipmentQueryService
        implements GetShipmentUseCase,
                GetShipmentsByCheckoutUseCase,
                GetShipmentsBySellerUseCase,
                GetActiveShipmentsUseCase,
                GetShipmentByInvoiceUseCase {

    private final ShipmentReadManager shipmentReadManager;

    public ShipmentQueryService(ShipmentReadManager shipmentReadManager) {
        this.shipmentReadManager = shipmentReadManager;
    }

    @Override
    public Shipment execute(Long shipmentId) {
        return shipmentReadManager.findById(shipmentId);
    }

    @Override
    public List<Shipment> getByCheckoutId(Long checkoutId) {
        return shipmentReadManager.findByCheckoutId(checkoutId);
    }

    @Override
    public List<Shipment> getBySellerId(Long sellerId) {
        return shipmentReadManager.findBySellerId(sellerId);
    }

    @Override
    public List<Shipment> execute() {
        return shipmentReadManager.findActiveShipments();
    }

    @Override
    public Optional<Shipment> execute(Long carrierId, String invoiceNumber) {
        return shipmentReadManager.findByCarrierIdAndInvoiceNumber(carrierId, invoiceNumber);
    }
}
