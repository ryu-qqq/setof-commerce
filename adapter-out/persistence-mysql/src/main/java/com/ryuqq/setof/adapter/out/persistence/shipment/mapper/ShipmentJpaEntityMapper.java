package com.ryuqq.setof.adapter.out.persistence.shipment.mapper;

import com.ryuqq.setof.adapter.out.persistence.shipment.entity.ShipmentJpaEntity;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.vo.DeliveryStatus;
import com.ryuqq.setof.domain.shipment.vo.InvoiceNumber;
import com.ryuqq.setof.domain.shipment.vo.Sender;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;
import com.ryuqq.setof.domain.shipment.vo.ShipmentType;
import com.ryuqq.setof.domain.shipment.vo.TrackingInfo;
import org.springframework.stereotype.Component;

/**
 * ShipmentJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Shipment 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Shipment -> ShipmentJpaEntity (저장용)
 *   <li>ShipmentJpaEntity -> Shipment (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShipmentJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * @param domain Shipment 도메인
     * @return ShipmentJpaEntity
     */
    public ShipmentJpaEntity toEntity(Shipment domain) {
        TrackingInfo trackingInfo = domain.getTrackingInfo();

        return ShipmentJpaEntity.of(
                domain.getIdValue(),
                domain.getSellerId(),
                domain.getCheckoutId(),
                domain.getCarrierId(),
                domain.getInvoiceNumberValue(),
                domain.getSenderName(),
                domain.getSenderPhone(),
                domain.getSender().address(),
                domain.getTypeValue(),
                domain.getStatusValue(),
                trackingInfo.lastLocation(),
                trackingInfo.lastMessage(),
                trackingInfo.lastTrackedAt(),
                trackingInfo.deliveredAt(),
                domain.getShippedAt(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity ShipmentJpaEntity
     * @return Shipment 도메인
     */
    public Shipment toDomain(ShipmentJpaEntity entity) {
        InvoiceNumber invoiceNumber = InvoiceNumber.of(entity.getInvoiceNumber());
        Sender sender =
                Sender.of(
                        entity.getSenderName(), entity.getSenderPhone(), entity.getSenderAddress());
        ShipmentType type = ShipmentType.valueOf(entity.getType());
        DeliveryStatus status = DeliveryStatus.valueOf(entity.getStatus());
        TrackingInfo trackingInfo = buildTrackingInfo(entity);

        return Shipment.reconstitute(
                ShipmentId.of(entity.getId()),
                entity.getSellerId(),
                entity.getCheckoutId(),
                entity.getCarrierId(),
                invoiceNumber,
                sender,
                type,
                status,
                trackingInfo,
                entity.getShippedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * Entity에서 TrackingInfo 재구성
     *
     * @param entity ShipmentJpaEntity
     * @return TrackingInfo
     */
    private TrackingInfo buildTrackingInfo(ShipmentJpaEntity entity) {
        if (entity.getDeliveredAt() != null) {
            return TrackingInfo.delivered(
                    entity.getLastLocation(),
                    entity.getLastMessage(),
                    entity.getLastTrackedAt(),
                    entity.getDeliveredAt());
        }

        if (entity.getLastLocation() != null || entity.getLastMessage() != null) {
            return TrackingInfo.of(
                    entity.getLastLocation(), entity.getLastMessage(), entity.getLastTrackedAt());
        }

        return TrackingInfo.empty();
    }
}
