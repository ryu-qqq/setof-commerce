package com.ryuqq.setof.application.shipment.dto.client;

import java.time.Instant;
import java.util.List;

/**
 * TrackingApiResult - 배송 추적 API 결과 DTO
 *
 * <p>외부 배송 추적 API (스마트택배 등)의 응답을 표현합니다.
 *
 * <p><strong>Application Layer DTO 규칙:</strong>
 *
 * <ul>
 *   <li>Java Record 사용 (불변성)
 *   <li>Lombok 금지
 *   <li>Domain 객체 직접 참조 금지
 * </ul>
 *
 * @param carrierCode 택배사 코드
 * @param invoiceNumber 운송장 번호
 * @param status 배송 상태 (API 원본 값)
 * @param lastLocation 마지막 위치
 * @param lastMessage 마지막 메시지
 * @param lastTrackedAt 마지막 추적 시각
 * @param isDelivered 배송 완료 여부
 * @param deliveredAt 배송 완료 시각 (nullable)
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
     * TrackingEvent - 개별 추적 이벤트
     *
     * @param time 이벤트 시각
     * @param location 위치
     * @param status 상태
     * @param message 메시지
     */
    public record TrackingEvent(Instant time, String location, String status, String message) {}

    /**
     * 빈 결과 생성
     *
     * @param carrierCode 택배사 코드
     * @param invoiceNumber 운송장 번호
     * @return 빈 TrackingApiResult
     */
    public static TrackingApiResult empty(String carrierCode, String invoiceNumber) {
        return new TrackingApiResult(
                carrierCode, invoiceNumber, null, null, null, null, false, null, List.of());
    }

    /**
     * 추적 정보 존재 여부 확인
     *
     * @return 추적 정보가 있으면 true
     */
    public boolean hasTrackingData() {
        return lastLocation != null || lastMessage != null;
    }
}
