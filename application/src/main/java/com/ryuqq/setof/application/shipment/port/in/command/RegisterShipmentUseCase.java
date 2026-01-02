package com.ryuqq.setof.application.shipment.port.in.command;

import com.ryuqq.setof.application.shipment.dto.command.RegisterShipmentCommand;

/**
 * Register Shipment UseCase (Command)
 *
 * <p>운송장 등록을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterShipmentUseCase {

    /**
     * 운송장 등록 실행
     *
     * @param command 운송장 등록 명령
     * @return 등록된 운송장 ID
     */
    Long execute(RegisterShipmentCommand command);
}
