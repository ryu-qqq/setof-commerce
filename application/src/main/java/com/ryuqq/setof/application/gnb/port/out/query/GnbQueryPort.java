package com.ryuqq.setof.application.gnb.port.out.query;

import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import java.util.List;
import java.util.Optional;

/**
 * Gnb 조회 Outbound Port (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GnbQueryPort {

    /**
     * GNB ID로 조회
     *
     * @param gnbId GNB ID
     * @return GNB Optional
     */
    Optional<Gnb> findById(GnbId gnbId);

    /**
     * 활성 GNB 전체 조회 (displayOrder 순 정렬)
     *
     * @return GNB 목록
     */
    List<Gnb> findAllActive();

    /**
     * GNB 존재 여부 확인
     *
     * @param gnbId GNB ID
     * @return 존재 여부
     */
    boolean existsById(GnbId gnbId);
}
