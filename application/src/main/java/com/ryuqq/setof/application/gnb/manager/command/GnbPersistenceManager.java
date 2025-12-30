package com.ryuqq.setof.application.gnb.manager.command;

import com.ryuqq.setof.application.gnb.port.out.command.GnbPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gnb 영속성 Manager (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional
public class GnbPersistenceManager {

    private final GnbPersistencePort gnbPersistencePort;

    public GnbPersistenceManager(GnbPersistencePort gnbPersistencePort) {
        this.gnbPersistencePort = gnbPersistencePort;
    }

    /**
     * GNB 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param gnb 저장/수정할 GNB
     * @return 저장된 GNB ID
     */
    public GnbId persist(Gnb gnb) {
        return gnbPersistencePort.persist(gnb);
    }
}
