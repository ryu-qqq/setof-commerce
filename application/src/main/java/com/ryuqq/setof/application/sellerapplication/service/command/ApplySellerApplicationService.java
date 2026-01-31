package com.ryuqq.setof.application.sellerapplication.service.command;

import com.ryuqq.setof.application.common.component.TransactionEventRegistry;
import com.ryuqq.setof.application.sellerapplication.dto.command.ApplySellerApplicationCommand;
import com.ryuqq.setof.application.sellerapplication.factory.SellerApplicationCommandFactory;
import com.ryuqq.setof.application.sellerapplication.manager.SellerApplicationCommandManager;
import com.ryuqq.setof.application.sellerapplication.port.in.command.ApplySellerApplicationUseCase;
import com.ryuqq.setof.application.sellerapplication.validator.SellerApplicationValidator;
import com.ryuqq.setof.domain.sellerapplication.aggregate.SellerApplication;
import org.springframework.stereotype.Service;

/**
 * ApplySellerApplicationService - 셀러 입점 신청 Service.
 *
 * <p>새로운 입점 신청을 생성합니다.
 *
 * @author ryu-qqq
 */
@Service
public class ApplySellerApplicationService implements ApplySellerApplicationUseCase {

    private final SellerApplicationCommandFactory commandFactory;
    private final SellerApplicationCommandManager commandManager;
    private final SellerApplicationValidator validator;
    private final TransactionEventRegistry eventRegistry;

    public ApplySellerApplicationService(
            SellerApplicationCommandFactory commandFactory,
            SellerApplicationCommandManager commandManager,
            SellerApplicationValidator validator,
            TransactionEventRegistry eventRegistry) {
        this.commandFactory = commandFactory;
        this.commandManager = commandManager;
        this.validator = validator;
        this.eventRegistry = eventRegistry;
    }

    @Override
    public Long execute(ApplySellerApplicationCommand command) {
        validator.validateNoPendingApplication(command.businessInfo().registrationNumber());

        SellerApplication application = commandFactory.create(command);
        Long applicationId = commandManager.persist(application);

        application.pollEvents().forEach(eventRegistry::registerForPublish);

        return applicationId;
    }
}
