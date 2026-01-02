package com.ryuqq.setof.application.shipment.factory.command;

import com.ryuqq.setof.application.shipment.dto.command.ChangeInvoiceCommand;
import com.ryuqq.setof.application.shipment.dto.command.ChangeShipmentStatusCommand;
import com.ryuqq.setof.application.shipment.dto.command.RegisterShipmentCommand;
import com.ryuqq.setof.application.shipment.dto.command.UpdateTrackingCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.vo.DeliveryStatus;
import com.ryuqq.setof.domain.shipment.vo.InvoiceNumber;
import com.ryuqq.setof.domain.shipment.vo.Sender;
import com.ryuqq.setof.domain.shipment.vo.ShipmentType;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Shipment Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성/변경 로직 캡슐화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShipmentCommandFactory {

    private final ClockHolder clockHolder;

    public ShipmentCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 운송장 생성
     *
     * @param command 운송장 등록 커맨드
     * @return 생성된 Shipment (저장 전)
     */
    public Shipment create(RegisterShipmentCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        InvoiceNumber invoiceNumber = InvoiceNumber.of(command.invoiceNumber());
        Sender sender =
                Sender.of(command.senderName(), command.senderPhone(), command.senderAddress());
        ShipmentType type = resolveType(command.type());

        return Shipment.forNew(
                command.sellerId(),
                command.checkoutId(),
                command.carrierId(),
                invoiceNumber,
                sender,
                type,
                now);
    }

    /**
     * 배송 상태 변경 적용
     *
     * @param shipment 기존 Shipment
     * @param command 상태 변경 커맨드
     * @return 상태 변경된 Shipment
     */
    public Shipment applyStatusChange(Shipment shipment, ChangeShipmentStatusCommand command) {
        Instant now = Instant.now(clockHolder.getClock());
        DeliveryStatus newStatus = DeliveryStatus.valueOf(command.status());
        return shipment.changeStatus(newStatus, now);
    }

    /**
     * 추적 정보 업데이트 적용
     *
     * @param shipment 기존 Shipment
     * @param command 추적 정보 업데이트 커맨드
     * @return 추적 정보가 업데이트된 Shipment
     */
    public Shipment applyTrackingUpdate(Shipment shipment, UpdateTrackingCommand command) {
        Instant now = Instant.now(clockHolder.getClock());
        Shipment updated =
                shipment.updateTracking(
                        command.location(), command.message(), command.trackedAt(), now);

        // 상태 변경이 함께 요청된 경우
        if (command.newStatus() != null) {
            DeliveryStatus newStatus = DeliveryStatus.valueOf(command.newStatus());
            updated = updated.changeStatus(newStatus, now);
        }

        return updated;
    }

    /**
     * 운송장 번호 변경 적용
     *
     * @param shipment 기존 Shipment
     * @param command 운송장 번호 변경 커맨드
     * @return 운송장 번호가 변경된 Shipment
     */
    public Shipment applyInvoiceChange(Shipment shipment, ChangeInvoiceCommand command) {
        Instant now = Instant.now(clockHolder.getClock());
        InvoiceNumber newInvoiceNumber = InvoiceNumber.of(command.invoiceNumber());
        return shipment.changeInvoice(command.carrierId(), newInvoiceNumber, now);
    }

    /**
     * 배송 완료 처리 적용
     *
     * @param shipment 기존 Shipment
     * @return 배송 완료된 Shipment
     */
    public Shipment applyMarkDelivered(Shipment shipment) {
        Instant now = Instant.now(clockHolder.getClock());
        return shipment.markDelivered(now);
    }

    private ShipmentType resolveType(String type) {
        if (type == null || type.isBlank()) {
            return ShipmentType.defaultType();
        }
        return ShipmentType.valueOf(type);
    }
}
