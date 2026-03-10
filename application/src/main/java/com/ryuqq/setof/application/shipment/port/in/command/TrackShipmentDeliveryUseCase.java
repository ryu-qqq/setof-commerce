package com.ryuqq.setof.application.shipment.port.in.command;

/**
 * 배송 추적 유스케이스.
 *
 * <p>스케줄러에서 호출합니다. 배송중(DELIVERY_PROCESSING) 상태의 주문을 조회하여 배송 추적 API를 호출하고, 배송 완료 시 주문/배송 상태를
 * 업데이트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface TrackShipmentDeliveryUseCase {

    /**
     * 배송중 주문을 배치 크기만큼 조회하여 배송 추적 처리.
     *
     * @param batchSize 배치 크기
     * @return 처리된 건수
     */
    int execute(int batchSize);
}
