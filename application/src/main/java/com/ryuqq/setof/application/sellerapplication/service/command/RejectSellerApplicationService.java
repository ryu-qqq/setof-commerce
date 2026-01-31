package com.ryuqq.setof.application.sellerapplication.service.command;

import com.ryuqq.setof.application.common.component.TransactionEventRegistry;
import com.ryuqq.setof.application.sellerapplication.dto.command.RejectSellerApplicationCommand;
import com.ryuqq.setof.application.sellerapplication.factory.SellerApplicationCommandFactory;
import com.ryuqq.setof.application.sellerapplication.manager.SellerApplicationCommandManager;
import com.ryuqq.setof.application.sellerapplication.port.in.command.RejectSellerApplicationUseCase;
import com.ryuqq.setof.application.sellerapplication.validator.SellerApplicationValidator;
import com.ryuqq.setof.domain.sellerapplication.aggregate.SellerApplication;
import com.ryuqq.setof.domain.sellerapplication.id.SellerApplicationId;
import org.springframework.stereotype.Service;

/**
 * RejectSellerApplicationService - 셀러 입점 신청 거절 Service.
 *
 * <p>대기 상태의 입점 신청을 거절합니다.
 *
 * @author ryu-qqq
 */
@Service
public class RejectSellerApplicationService implements RejectSellerApplicationUseCase {

    private final SellerApplicationValidator validator;
    private final SellerApplicationCommandManager commandManager;
    private final SellerApplicationCommandFactory commandFactory;
    private final TransactionEventRegistry eventRegistry;

    public RejectSellerApplicationService(
            SellerApplicationValidator validator,
            SellerApplicationCommandManager commandManager,
            SellerApplicationCommandFactory commandFactory,
            TransactionEventRegistry eventRegistry) {
        this.validator = validator;
        this.commandManager = commandManager;
        this.commandFactory = commandFactory;
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void execute(RejectSellerApplicationCommand command) {
        SellerApplicationId applicationId = SellerApplicationId.of(command.sellerApplicationId());
        SellerApplication application = validator.findExistingOrThrow(applicationId);

        application.reject(command.rejectionReason(), command.processedBy(), commandFactory.now());
        commandManager.persist(application);

        application.pollEvents().forEach(eventRegistry::registerForPublish);
    }
}
