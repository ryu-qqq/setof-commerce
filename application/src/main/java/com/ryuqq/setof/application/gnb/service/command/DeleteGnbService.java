package com.ryuqq.setof.application.gnb.service.command;

import com.ryuqq.setof.application.gnb.dto.command.DeleteGnbCommand;
import com.ryuqq.setof.application.gnb.manager.command.GnbPersistenceManager;
import com.ryuqq.setof.application.gnb.manager.query.GnbReadManager;
import com.ryuqq.setof.application.gnb.port.in.command.DeleteGnbUseCase;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import org.springframework.stereotype.Service;

/**
 * Gnb 삭제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteGnbService implements DeleteGnbUseCase {

    private final GnbReadManager gnbReadManager;
    private final GnbPersistenceManager gnbPersistenceManager;

    public DeleteGnbService(
            GnbReadManager gnbReadManager, GnbPersistenceManager gnbPersistenceManager) {
        this.gnbReadManager = gnbReadManager;
        this.gnbPersistenceManager = gnbPersistenceManager;
    }

    @Override
    public void execute(DeleteGnbCommand command) {
        Gnb gnb = gnbReadManager.findById(command.gnbId());
        gnb.delete();
        gnbPersistenceManager.persist(gnb);
    }
}
