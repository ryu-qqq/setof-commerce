package com.ryuqq.setof.domain.shipment.vo;

import java.time.Instant;

/**
 * 배송 추적 정보 Value Object
 *
 * <p>스마트택배 API에서 받아온 배송 추적 정보를 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param lastLocation 마지막 위치 (예: "서울 강남 집배센터")
 * @param lastMessage 마지막 메시지 (예: "배송출발")
 * @param lastTrackedAt 마지막 추적 시각
 * @param deliveredAt 배송 완료 시각 (nullable)
 */
public record TrackingInfo(
        String lastLocation, String lastMessage, Instant lastTrackedAt, Instant deliveredAt) {

    /**
     * Static Factory Method - 빈 추적 정보 생성
     *
     * @return 빈 TrackingInfo 인스턴스
     */
    public static TrackingInfo empty() {
        return new TrackingInfo(null, null, null, null);
    }

    /**
     * Static Factory Method - 추적 정보 생성
     *
     * @param lastLocation 마지막 위치
     * @param lastMessage 마지막 메시지
     * @param lastTrackedAt 마지막 추적 시각
     * @return TrackingInfo 인스턴스
     */
    public static TrackingInfo of(String lastLocation, String lastMessage, Instant lastTrackedAt) {
        return new TrackingInfo(lastLocation, lastMessage, lastTrackedAt, null);
    }

    /**
     * Static Factory Method - 배송 완료 정보 생성
     *
     * @param lastLocation 마지막 위치
     * @param lastMessage 마지막 메시지
     * @param lastTrackedAt 마지막 추적 시각
     * @param deliveredAt 배송 완료 시각
     * @return TrackingInfo 인스턴스
     */
    public static TrackingInfo delivered(
            String lastLocation, String lastMessage, Instant lastTrackedAt, Instant deliveredAt) {
        return new TrackingInfo(lastLocation, lastMessage, lastTrackedAt, deliveredAt);
    }

    /**
     * 추적 정보 존재 여부 확인
     *
     * @return 추적 정보가 있으면 true
     */
    public boolean hasTrackingData() {
        return lastLocation != null || lastMessage != null;
    }

    /**
     * 배송 완료 여부 확인
     *
     * @return 배송 완료 시각이 있으면 true
     */
    public boolean isDelivered() {
        return deliveredAt != null;
    }

    /**
     * 추적 정보 업데이트
     *
     * @param newLocation 새 위치
     * @param newMessage 새 메시지
     * @param trackedAt 추적 시각
     * @return 업데이트된 TrackingInfo 인스턴스
     */
    public TrackingInfo updateTracking(String newLocation, String newMessage, Instant trackedAt) {
        return new TrackingInfo(newLocation, newMessage, trackedAt, this.deliveredAt);
    }

    /**
     * 배송 완료 처리
     *
     * @param completedAt 배송 완료 시각
     * @return 배송 완료된 TrackingInfo 인스턴스
     */
    public TrackingInfo markDelivered(Instant completedAt) {
        return new TrackingInfo(
                this.lastLocation, this.lastMessage, this.lastTrackedAt, completedAt);
    }
}
