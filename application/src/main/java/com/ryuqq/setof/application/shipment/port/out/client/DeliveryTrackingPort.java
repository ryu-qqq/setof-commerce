package com.ryuqq.setof.application.shipment.port.out.client;

import com.ryuqq.setof.application.shipment.dto.client.TrackingApiResult;
import java.util.Optional;

/**
 * Delivery Tracking Port
 *
 * <p>배송 추적을 위한 아웃바운드 포트입니다.
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>DeliveryTrackingAdapter - 스마트택배 API 기반 배송 추적
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeliveryTrackingPort {

    /**
     * 배송 추적 정보를 조회합니다.
     *
     * @param carrierCode 택배사 코드
     * @param invoiceNumber 송장 번호
     * @return 추적 결과 (실패 시 empty)
     */
    Optional<TrackingApiResult> fetchTrackingInfo(String carrierCode, String invoiceNumber);
}
