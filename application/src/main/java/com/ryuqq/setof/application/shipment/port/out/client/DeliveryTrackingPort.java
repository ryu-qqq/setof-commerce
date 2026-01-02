package com.ryuqq.setof.application.shipment.port.out.client;

import com.ryuqq.setof.application.shipment.dto.client.TrackingApiResult;
import java.util.Optional;

/**
 * DeliveryTrackingPort - 배송 추적 외부 API 포트
 *
 * <p>스마트택배 API와 같은 외부 배송 추적 서비스와의 통신을 추상화합니다.
 *
 * <p><strong>Port-Out 역할:</strong>
 *
 * <ul>
 *   <li>Application Layer에서 정의, Adapter Layer에서 구현
 *   <li>외부 API 의존성을 추상화하여 테스트 용이성 확보
 *   <li>기술 변경에 유연하게 대응 (스마트택배 → 다른 서비스)
 * </ul>
 *
 * <p><strong>구현 주의사항:</strong>
 *
 * <ul>
 *   <li>실패 시 Optional.empty() 반환 (예외 발생 금지)
 *   <li>재시도 및 서킷브레이커는 Adapter에서 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeliveryTrackingPort {

    /**
     * 운송장 추적 정보 조회
     *
     * <p>택배사 코드와 운송장 번호로 외부 API에서 추적 정보를 조회합니다.
     *
     * @param carrierCode 택배사 코드 (스마트택배 API 기준)
     * @param invoiceNumber 운송장 번호
     * @return 추적 결과, 실패 시 Optional.empty()
     */
    Optional<TrackingApiResult> fetchTrackingInfo(String carrierCode, String invoiceNumber);
}
