package com.ryuqq.setof.application.carrier.port.in.query;

import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import java.util.List;

/**
 * Get Carriers UseCase (Query)
 *
 * <p>택배사 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCarriersUseCase {

    /**
     * 활성 상태 택배사 전체 조회
     *
     * @return 활성 택배사 목록
     */
    List<Carrier> getActiveCarriers();

    /**
     * 전체 택배사 조회 (활성/비활성 포함)
     *
     * @return 전체 택배사 목록
     */
    List<Carrier> getAllCarriers();
}
