package com.ryuqq.setof.application.shipment.dto.client;

import java.time.Instant;
import java.util.List;

/**
 * 배송 추적 API 결과 DTO
 *
 * <p>외부 배송 추적 API의 응답을 담는 Application Layer DTO입니다.
 *
 * @param carrierCode 택배사 코드
 * @param invoiceNumber 송장 번호
 * @param status 배송 상태 (PENDING, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED)
 * @param lastLocation 마지막 위치
 * @param lastMessage 마지막 메시지
 * @param lastTrackedAt 마지막 추적 시간
 * @param isDelivered 배송 완료 여부
 * @param deliveredAt 배송 완료 시간
 * @param events 추적 이벤트 목록
 * @author development-team
 * @since 1.0.0
 */
public record TrackingApiResult(
        String carrierCode,
        String invoiceNumber,
        String status,
        String lastLocation,
        String lastMessage,
        Instant lastTrackedAt,
        boolean isDelivered,
        Instant deliveredAt,
        List<TrackingEvent> events) {

    /**
     * 추적 이벤트 DTO
     *
     * @param time 이벤트 시간
     * @param location 이벤트 위치
     * @param code 이벤트 코드
     * @param description 이벤트 설명
     */
    public record TrackingEvent(Instant time, String location, String code, String description) {}
}
