package com.ryuqq.setof.application.shipment.port.in.command;

import com.ryuqq.setof.application.shipment.dto.command.ChangeInvoiceCommand;

/**
 * Change Invoice UseCase (Command)
 *
 * <p>운송장 번호 변경을 담당하는 Inbound Port
 *
 * <p>발송 전(PENDING 상태)에만 변경 가능합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ChangeInvoiceUseCase {

    /**
     * 운송장 번호 변경 실행
     *
     * @param shipmentId 운송장 ID
     * @param command 운송장 번호 변경 명령
     */
    void execute(Long shipmentId, ChangeInvoiceCommand command);
}
