package com.ryuqq.setof.application.shipment.manager.query;

import com.ryuqq.setof.application.shipment.port.out.query.ShipmentQueryPort;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.exception.ShipmentNotFoundException;
import com.ryuqq.setof.domain.shipment.vo.InvoiceNumber;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Shipment Read Manager
 *
 * <p>Shipment Aggregate 조회 담당 Manager
 *
 * <p>역할:
 *
 * <ul>
 *   <li>읽기 전용 Transaction 관리
 *   <li>QueryPort 래핑
 *   <li>도메인 예외 발생
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional(readOnly = true)
public class ShipmentReadManager {

    private final ShipmentQueryPort shipmentQueryPort;

    public ShipmentReadManager(ShipmentQueryPort shipmentQueryPort) {
        this.shipmentQueryPort = shipmentQueryPort;
    }

    /**
     * ID로 Shipment 조회 (필수)
     *
     * @param shipmentId Shipment ID
     * @return 조회된 Shipment
     * @throws ShipmentNotFoundException 운송장이 없는 경우
     */
    public Shipment findById(Long shipmentId) {
        ShipmentId id = ShipmentId.of(shipmentId);
        return shipmentQueryPort
                .findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(shipmentId));
    }

    /**
     * ID로 Shipment 조회 (Optional)
     *
     * @param shipmentId Shipment ID
     * @return 조회된 Shipment (Optional)
     */
    public Optional<Shipment> findByIdOptional(Long shipmentId) {
        ShipmentId id = ShipmentId.of(shipmentId);
        return shipmentQueryPort.findById(id);
    }

    /**
     * 결제건 ID로 Shipment 목록 조회
     *
     * @param checkoutId 결제건 ID
     * @return Shipment 목록
     */
    public List<Shipment> findByCheckoutId(Long checkoutId) {
        return shipmentQueryPort.findByCheckoutId(checkoutId);
    }

    /**
     * 셀러 ID로 Shipment 목록 조회
     *
     * @param sellerId 셀러 ID
     * @return Shipment 목록
     */
    public List<Shipment> findBySellerId(Long sellerId) {
        return shipmentQueryPort.findBySellerId(sellerId);
    }

    /**
     * 추적 대상 Shipment 목록 조회
     *
     * @return 추적 대상 Shipment 목록
     */
    public List<Shipment> findActiveShipments() {
        return shipmentQueryPort.findActiveShipments();
    }

    /**
     * 택배사 ID + 운송장 번호로 Shipment 조회
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @return 조회된 Shipment (Optional)
     */
    public Optional<Shipment> findByCarrierIdAndInvoiceNumber(
            Long carrierId, String invoiceNumber) {
        InvoiceNumber invoice = InvoiceNumber.of(invoiceNumber);
        return shipmentQueryPort.findByCarrierIdAndInvoiceNumber(carrierId, invoice);
    }

    /**
     * Shipment ID 존재 여부 확인
     *
     * @param shipmentId Shipment ID
     * @return 존재 여부
     */
    public boolean existsById(Long shipmentId) {
        ShipmentId id = ShipmentId.of(shipmentId);
        return shipmentQueryPort.existsById(id);
    }

    /**
     * 택배사 ID + 운송장 번호 존재 여부 확인
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @return 존재 여부
     */
    public boolean existsByCarrierIdAndInvoiceNumber(Long carrierId, String invoiceNumber) {
        InvoiceNumber invoice = InvoiceNumber.of(invoiceNumber);
        return shipmentQueryPort.existsByCarrierIdAndInvoiceNumber(carrierId, invoice);
    }
}
