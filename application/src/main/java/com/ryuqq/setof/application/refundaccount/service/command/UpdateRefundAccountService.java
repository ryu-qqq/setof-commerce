package com.ryuqq.setof.application.refundaccount.service.command;

import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.factory.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountCommandManager;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.in.command.UpdateRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.validator.RefundAccountValidator;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccountUpdateData;
import org.springframework.stereotype.Service;

/**
 * UpdateRefundAccountService - 환불 계좌 수정 Service.
 *
 * <p>ReadManager로 조회 → UpdateData 생성 → domain.update() → Validator로 검증 → persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateRefundAccountService implements UpdateRefundAccountUseCase {

    private final RefundAccountReadManager readManager;
    private final RefundAccountCommandFactory factory;
    private final RefundAccountCommandManager commandManager;
    private final RefundAccountValidator validator;

    public UpdateRefundAccountService(
            RefundAccountReadManager readManager,
            RefundAccountCommandFactory factory,
            RefundAccountCommandManager commandManager,
            RefundAccountValidator validator) {
        this.readManager = readManager;
        this.factory = factory;
        this.commandManager = commandManager;
        this.validator = validator;
    }

    @Override
    public void execute(UpdateRefundAccountCommand command) {
        RefundAccount refundAccount =
                readManager.getByUserIdAndId(command.userId(), command.refundAccountId());
        RefundAccountUpdateData updateData = factory.createUpdateData(command);
        refundAccount.update(updateData);
        validator.validateAccount(refundAccount);
        commandManager.persist(refundAccount);
    }
}
