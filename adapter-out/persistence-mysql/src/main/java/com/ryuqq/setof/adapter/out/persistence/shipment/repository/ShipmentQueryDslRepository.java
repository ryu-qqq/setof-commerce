package com.ryuqq.setof.adapter.out.persistence.shipment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.shipment.entity.QShipmentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shipment.entity.ShipmentJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * ShipmentQueryDslRepository - Shipment QueryDSL Repository
 *
 * <p>복잡한 조회 쿼리를 담당하는 QueryDSL Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ShipmentQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ShipmentQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private static final QShipmentJpaEntity shipment = QShipmentJpaEntity.shipmentJpaEntity;

    /** 활성 배송 상태 (추적 대상) */
    private static final Set<String> ACTIVE_STATUSES =
            Set.of("PENDING", "IN_TRANSIT", "OUT_FOR_DELIVERY");

    /**
     * ID로 Shipment 조회
     *
     * @param id Shipment ID
     * @return ShipmentJpaEntity (Optional)
     */
    public Optional<ShipmentJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(shipment).where(shipment.id.eq(id)).fetchOne());
    }

    /**
     * 결제건 ID로 Shipment 목록 조회
     *
     * @param checkoutId 결제건 ID
     * @return ShipmentJpaEntity 목록
     */
    public List<ShipmentJpaEntity> findByCheckoutId(Long checkoutId) {
        return queryFactory
                .selectFrom(shipment)
                .where(shipment.checkoutId.eq(checkoutId))
                .orderBy(shipment.createdAt.desc())
                .fetch();
    }

    /**
     * 셀러 ID로 Shipment 목록 조회
     *
     * @param sellerId 셀러 ID
     * @return ShipmentJpaEntity 목록
     */
    public List<ShipmentJpaEntity> findBySellerId(Long sellerId) {
        return queryFactory
                .selectFrom(shipment)
                .where(shipment.sellerId.eq(sellerId))
                .orderBy(shipment.createdAt.desc())
                .fetch();
    }

    /**
     * 추적 대상 Shipment 목록 조회
     *
     * <p>DELIVERED, CANCELLED 상태가 아닌 운송장을 반환합니다.
     *
     * @return ShipmentJpaEntity 목록
     */
    public List<ShipmentJpaEntity> findActiveShipments() {
        return queryFactory
                .selectFrom(shipment)
                .where(shipment.status.in(ACTIVE_STATUSES))
                .orderBy(shipment.createdAt.asc())
                .fetch();
    }

    /**
     * 택배사 ID + 운송장 번호로 Shipment 조회
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @return ShipmentJpaEntity (Optional)
     */
    public Optional<ShipmentJpaEntity> findByCarrierIdAndInvoiceNumber(
            Long carrierId, String invoiceNumber) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(shipment)
                        .where(
                                shipment.carrierId.eq(carrierId),
                                shipment.invoiceNumber.eq(invoiceNumber))
                        .fetchOne());
    }

    /**
     * ID 존재 여부 확인
     *
     * @param id Shipment ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        return queryFactory.selectOne().from(shipment).where(shipment.id.eq(id)).fetchFirst()
                != null;
    }

    /**
     * 택배사 ID + 운송장 번호 존재 여부 확인
     *
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @return 존재 여부
     */
    public boolean existsByCarrierIdAndInvoiceNumber(Long carrierId, String invoiceNumber) {
        return queryFactory
                        .selectOne()
                        .from(shipment)
                        .where(
                                shipment.carrierId.eq(carrierId),
                                shipment.invoiceNumber.eq(invoiceNumber))
                        .fetchFirst()
                != null;
    }
}
