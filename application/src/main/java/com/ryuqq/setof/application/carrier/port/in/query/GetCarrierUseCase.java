package com.ryuqq.setof.application.carrier.port.in.query;

import com.ryuqq.setof.domain.carrier.aggregate.Carrier;

/**
 * Get Carrier UseCase (Query)
 *
 * <p>단일 택배사 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCarrierUseCase {

    /**
     * 택배사 ID로 조회
     *
     * @param carrierId 택배사 ID
     * @return 택배사 도메인
     */
    Carrier execute(Long carrierId);
}
