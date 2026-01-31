package com.ryuqq.setof.application.seller.service.command;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerAddressCommand;
import com.ryuqq.setof.application.seller.factory.SellerCommandFactory;
import com.ryuqq.setof.application.seller.manager.SellerAddressCommandManager;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerAddressUseCase;
import com.ryuqq.setof.application.seller.validator.SellerAddressValidator;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddressUpdateData;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import org.springframework.stereotype.Service;

/**
 * UpdateSellerAddressService - 셀러 주소 수정 Service.
 *
 * <p>APP-TIM-001: TimeProvider 직접 사용 금지 - Factory에서 처리
 *
 * <p>APP-VAL-001: 검증은 Validator에 위임
 *
 * @author ryu-qqq
 */
@Service
public class UpdateSellerAddressService implements UpdateSellerAddressUseCase {

    private final SellerCommandFactory commandFactory;
    private final SellerAddressCommandManager commandManager;
    private final SellerAddressValidator validator;

    public UpdateSellerAddressService(
            SellerCommandFactory commandFactory,
            SellerAddressCommandManager commandManager,
            SellerAddressValidator validator) {
        this.commandFactory = commandFactory;
        this.commandManager = commandManager;
        this.validator = validator;
    }

    @Override
    public void execute(UpdateSellerAddressCommand command) {
        UpdateContext<SellerAddressId, SellerAddressUpdateData> context =
                commandFactory.createUpdateContext(command);

        SellerAddress address = validator.findExistingOrThrow(context.id());
        address.update(context.updateData(), context.changedAt());

        commandManager.persist(address);
    }
}
