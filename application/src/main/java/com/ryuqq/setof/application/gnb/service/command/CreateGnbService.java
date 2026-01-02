package com.ryuqq.setof.application.gnb.service.command;

import com.ryuqq.setof.application.gnb.dto.command.CreateGnbCommand;
import com.ryuqq.setof.application.gnb.factory.command.GnbCommandFactory;
import com.ryuqq.setof.application.gnb.manager.command.GnbPersistenceManager;
import com.ryuqq.setof.application.gnb.port.in.command.CreateGnbUseCase;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import org.springframework.stereotype.Service;

/**
 * Gnb 생성 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateGnbService implements CreateGnbUseCase {

    private final GnbCommandFactory gnbCommandFactory;
    private final GnbPersistenceManager gnbPersistenceManager;

    public CreateGnbService(
            GnbCommandFactory gnbCommandFactory, GnbPersistenceManager gnbPersistenceManager) {
        this.gnbCommandFactory = gnbCommandFactory;
        this.gnbPersistenceManager = gnbPersistenceManager;
    }

    @Override
    public Long execute(CreateGnbCommand command) {
        Gnb gnb = gnbCommandFactory.createGnb(command);
        return gnbPersistenceManager.persist(gnb).value();
    }
}
