package com.ryuqq.setof.application.refundaccount.service.command;

import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.factory.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountCommandManager;
import com.ryuqq.setof.application.refundaccount.port.in.command.RegisterRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.validator.RefundAccountValidator;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.springframework.stereotype.Service;

/**
 * RegisterRefundAccountService - 환불 계좌 등록 Service.
 *
 * <p>Factory로 도메인 객체 생성 → Validator로 계좌 실명 검증 → CommandManager로 persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterRefundAccountService implements RegisterRefundAccountUseCase {

    private final RefundAccountCommandFactory factory;
    private final RefundAccountCommandManager commandManager;
    private final RefundAccountValidator validator;

    public RegisterRefundAccountService(
            RefundAccountCommandFactory factory,
            RefundAccountCommandManager commandManager,
            RefundAccountValidator validator) {
        this.factory = factory;
        this.commandManager = commandManager;
        this.validator = validator;
    }

    @Override
    public Long execute(RegisterRefundAccountCommand command) {
        RefundAccount refundAccount = factory.createNewRefundAccount(command);
        validator.validateAccount(refundAccount);
        return commandManager.persist(refundAccount);
    }
}
