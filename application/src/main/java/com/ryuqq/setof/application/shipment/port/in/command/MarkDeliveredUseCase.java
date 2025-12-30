package com.ryuqq.setof.application.shipment.port.in.command;

/**
 * Mark Delivered UseCase (Command)
 *
 * <p>운송장 배송 완료 처리를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface MarkDeliveredUseCase {

    /**
     * 배송 완료 처리 실행
     *
     * @param shipmentId 운송장 ID
     */
    void execute(Long shipmentId);
}
