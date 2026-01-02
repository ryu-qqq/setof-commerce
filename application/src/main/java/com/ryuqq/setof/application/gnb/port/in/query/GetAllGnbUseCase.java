package com.ryuqq.setof.application.gnb.port.in.query;

import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import java.util.List;

/**
 * Gnb 전체 조회 UseCase (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetAllGnbUseCase {

    /**
     * 활성 GNB 전체 조회 (displayOrder 순 정렬)
     *
     * @return GNB 목록
     */
    List<GnbResponse> execute();
}
