package com.ryuqq.setof.adapter.out.persistence.shipment.adapter;

import com.ryuqq.setof.adapter.out.persistence.shipment.mapper.ShipmentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shipment.repository.ShipmentQueryDslRepository;
import com.ryuqq.setof.application.shipment.port.out.query.ShipmentQueryPort;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.vo.InvoiceNumber;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ShipmentQueryAdapter - Shipment Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Shipment 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>결제건 ID로 목록 조회 (findByCheckoutId)
 *   <li>셀러 ID로 목록 조회 (findBySellerId)
 *   <li>추적 대상 목록 조회 (findActiveShipments)
 *   <li>택배사 ID + 운송장 번호로 조회 (findByCarrierIdAndInvoiceNumber)
 *   <li>존재 여부 확인 (existsById, existsByCarrierIdAndInvoiceNumber)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShipmentQueryAdapter implements ShipmentQueryPort {

    private final ShipmentQueryDslRepository queryDslRepository;
    private final ShipmentJpaEntityMapper shipmentJpaEntityMapper;

    public ShipmentQueryAdapter(
            ShipmentQueryDslRepository queryDslRepository,
            ShipmentJpaEntityMapper shipmentJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.shipmentJpaEntityMapper = shipmentJpaEntityMapper;
    }

    /**
     * ID로 Shipment 단건 조회
     *
     * @param id Shipment ID (Value Object)
     * @return Shipment Domain (Optional)
     */
    @Override
    public Optional<Shipment> findById(ShipmentId id) {
        return queryDslRepository.findById(id.value()).map(shipmentJpaEntityMapper::toDomain);
    }

    /**
     * 결제건 ID로 Shipment 목록 조회
     *
     * @param checkoutId 결제건 ID
     * @return Shipment 목록
     */
    @Override
    public List<Shipment> findByCheckoutId(Long checkoutId) {
        return queryDslRepository.findByCheckoutId(checkoutId).stream()
                .map(shipmentJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 셀러 ID로 Shipment 목록 조회
     *
     * @param sellerId 셀러 ID
     * @return Shipment 목록
     */
    @Override
    public List<Shipment> findBySellerId(Long sellerId) {
        return queryDslRepository.findBySellerId(sellerId).stream()
                .map(shipmentJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 추적 대상 Shipment 목록 조회
     *
     * @return Shipment 목록
     */
    @Override
    public List<Shipment> findActiveShipments() {
        return queryDslRepository.findActiveShipments().stream()
                .map(shipmentJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 택배사 ID + 운송장 번호로 Shipment 조회
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호 (Value Object)
     * @return Shipment Domain (Optional)
     */
    @Override
    public Optional<Shipment> findByCarrierIdAndInvoiceNumber(
            Long carrierId, InvoiceNumber invoiceNumber) {
        return queryDslRepository
                .findByCarrierIdAndInvoiceNumber(carrierId, invoiceNumber.value())
                .map(shipmentJpaEntityMapper::toDomain);
    }

    /**
     * ID로 Shipment 존재 여부 확인
     *
     * @param id Shipment ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(ShipmentId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 택배사 ID + 운송장 번호 존재 여부 확인
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호 (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsByCarrierIdAndInvoiceNumber(Long carrierId, InvoiceNumber invoiceNumber) {
        return queryDslRepository.existsByCarrierIdAndInvoiceNumber(
                carrierId, invoiceNumber.value());
    }
}
