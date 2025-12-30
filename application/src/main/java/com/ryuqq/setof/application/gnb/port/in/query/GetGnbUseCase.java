package com.ryuqq.setof.application.gnb.port.in.query;

import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import com.ryuqq.setof.domain.cms.vo.GnbId;

/**
 * Gnb 단건 조회 UseCase (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetGnbUseCase {

    /**
     * GNB 단건 조회
     *
     * @param gnbId GNB ID
     * @return GNB 응답
     */
    GnbResponse execute(GnbId gnbId);
}
