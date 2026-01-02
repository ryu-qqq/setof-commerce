package com.ryuqq.setof.application.gnb.manager.query;

import com.ryuqq.setof.application.gnb.port.out.query.GnbQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.exception.GnbNotFoundException;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gnb 조회 Manager (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional(readOnly = true)
public class GnbReadManager {

    private final GnbQueryPort gnbQueryPort;

    public GnbReadManager(GnbQueryPort gnbQueryPort) {
        this.gnbQueryPort = gnbQueryPort;
    }

    /**
     * GNB ID로 조회
     *
     * @param gnbId GNB ID
     * @return GNB
     * @throws GnbNotFoundException GNB가 존재하지 않는 경우
     */
    public Gnb findById(GnbId gnbId) {
        return gnbQueryPort.findById(gnbId).orElseThrow(() -> new GnbNotFoundException(gnbId));
    }

    /**
     * 활성 GNB 전체 조회
     *
     * @return GNB 목록
     */
    public List<Gnb> findAllActive() {
        return gnbQueryPort.findAllActive();
    }
}
