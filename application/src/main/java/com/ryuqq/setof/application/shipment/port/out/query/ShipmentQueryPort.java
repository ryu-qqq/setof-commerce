package com.ryuqq.setof.application.shipment.port.out.query;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.vo.InvoiceNumber;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;
import java.util.List;
import java.util.Optional;

/**
 * Shipment Query Port (Query)
 *
 * <p>Shipment Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShipmentQueryPort {

    /**
     * ID로 Shipment 단건 조회
     *
     * @param id Shipment ID (Value Object)
     * @return Shipment Domain (Optional)
     */
    Optional<Shipment> findById(ShipmentId id);

    /**
     * 결제건 ID로 Shipment 목록 조회
     *
     * @param checkoutId 결제건 ID
     * @return Shipment 목록
     */
    List<Shipment> findByCheckoutId(Long checkoutId);

    /**
     * 셀러 ID로 Shipment 목록 조회
     *
     * @param sellerId 셀러 ID
     * @return Shipment 목록
     */
    List<Shipment> findBySellerId(Long sellerId);

    /**
     * 추적 대상 Shipment 목록 조회
     *
     * <p>DELIVERED, CANCELLED 상태가 아닌 운송장을 반환합니다.
     *
     * @return 추적 대상 Shipment 목록
     */
    List<Shipment> findActiveShipments();

    /**
     * 택배사 ID + 운송장 번호로 Shipment 조회
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호 (Value Object)
     * @return Shipment Domain (Optional)
     */
    Optional<Shipment> findByCarrierIdAndInvoiceNumber(Long carrierId, InvoiceNumber invoiceNumber);

    /**
     * Shipment ID 존재 여부 확인
     *
     * @param id Shipment ID
     * @return 존재 여부
     */
    boolean existsById(ShipmentId id);

    /**
     * 택배사 ID + 운송장 번호 존재 여부 확인
     *
     * <p>운송장 번호 중복 검증에 사용됩니다.
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호 (Value Object)
     * @return 존재 여부
     */
    boolean existsByCarrierIdAndInvoiceNumber(Long carrierId, InvoiceNumber invoiceNumber);
}
