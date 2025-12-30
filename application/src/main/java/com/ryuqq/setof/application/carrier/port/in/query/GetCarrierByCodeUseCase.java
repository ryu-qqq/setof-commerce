package com.ryuqq.setof.application.carrier.port.in.query;

import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import java.util.Optional;

/**
 * Get Carrier By Code UseCase (Query)
 *
 * <p>택배사 코드로 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCarrierByCodeUseCase {

    /**
     * 택배사 코드로 조회
     *
     * @param carrierCode 택배사 코드
     * @return 택배사 도메인 (Optional)
     */
    Optional<Carrier> execute(String carrierCode);
}
