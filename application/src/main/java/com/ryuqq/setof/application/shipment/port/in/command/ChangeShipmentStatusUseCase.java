package com.ryuqq.setof.application.shipment.port.in.command;

import com.ryuqq.setof.application.shipment.dto.command.ChangeShipmentStatusCommand;

/**
 * Change Shipment Status UseCase (Command)
 *
 * <p>운송장 상태 변경을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ChangeShipmentStatusUseCase {

    /**
     * 운송장 상태 변경 실행
     *
     * @param shipmentId 운송장 ID
     * @param command 상태 변경 명령
     */
    void execute(Long shipmentId, ChangeShipmentStatusCommand command);
}
