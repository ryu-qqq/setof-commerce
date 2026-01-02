package com.ryuqq.setof.application.shipment.port.in.query;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import java.util.List;

/**
 * Get Active Shipments UseCase (Query)
 *
 * <p>추적 대상 운송장 목록 조회를 담당하는 Inbound Port
 *
 * <p>배송 완료(DELIVERED) 또는 취소(CANCELLED)가 아닌 운송장을 반환합니다. 스마트택배 API 스케줄러에서 사용됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetActiveShipmentsUseCase {

    /**
     * 추적 대상 운송장 전체 조회
     *
     * @return 추적 대상 운송장 목록
     */
    List<Shipment> execute();
}
