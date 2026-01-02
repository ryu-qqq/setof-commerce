package com.ryuqq.setof.application.gnb.service.command;

import com.ryuqq.setof.application.gnb.dto.command.UpdateGnbCommand;
import com.ryuqq.setof.application.gnb.factory.command.GnbCommandFactory;
import com.ryuqq.setof.application.gnb.manager.command.GnbPersistenceManager;
import com.ryuqq.setof.application.gnb.manager.query.GnbReadManager;
import com.ryuqq.setof.application.gnb.port.in.command.UpdateGnbUseCase;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import org.springframework.stereotype.Service;

/**
 * Gnb 수정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateGnbService implements UpdateGnbUseCase {

    private final GnbReadManager gnbReadManager;
    private final GnbCommandFactory gnbCommandFactory;
    private final GnbPersistenceManager gnbPersistenceManager;

    public UpdateGnbService(
            GnbReadManager gnbReadManager,
            GnbCommandFactory gnbCommandFactory,
            GnbPersistenceManager gnbPersistenceManager) {
        this.gnbReadManager = gnbReadManager;
        this.gnbCommandFactory = gnbCommandFactory;
        this.gnbPersistenceManager = gnbPersistenceManager;
    }

    @Override
    public void execute(UpdateGnbCommand command) {
        Gnb gnb = gnbReadManager.findById(command.gnbId());
        gnbCommandFactory.applyUpdateGnb(gnb, command);
        gnbPersistenceManager.persist(gnb);
    }
}
