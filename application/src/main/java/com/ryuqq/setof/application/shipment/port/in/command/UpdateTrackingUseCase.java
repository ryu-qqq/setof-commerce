package com.ryuqq.setof.application.shipment.port.in.command;

import com.ryuqq.setof.application.shipment.dto.command.UpdateTrackingCommand;

/**
 * Update Tracking UseCase (Command)
 *
 * <p>운송장 추적 정보 업데이트를 담당하는 Inbound Port
 *
 * <p>스마트택배 API로부터 받은 추적 정보를 업데이트합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateTrackingUseCase {

    /**
     * 추적 정보 업데이트 실행
     *
     * @param shipmentId 운송장 ID
     * @param command 추적 정보 업데이트 명령
     */
    void execute(Long shipmentId, UpdateTrackingCommand command);
}
