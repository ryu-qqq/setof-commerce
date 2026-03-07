package com.ryuqq.setof.application.refundaccount.service.command;

import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountCommandManager;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.in.command.DeleteRefundAccountUseCase;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeleteRefundAccountService - 환불 계좌 삭제 Service.
 *
 * <p>ReadManager로 조회 → 도메인 delete() → persist로 더티체킹 반영.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class DeleteRefundAccountService implements DeleteRefundAccountUseCase {

    private final RefundAccountReadManager readManager;
    private final RefundAccountCommandManager commandManager;

    public DeleteRefundAccountService(
            RefundAccountReadManager readManager, RefundAccountCommandManager commandManager) {
        this.readManager = readManager;
        this.commandManager = commandManager;
    }

    @Override
    @Transactional
    public void execute(DeleteRefundAccountCommand command) {
        RefundAccount refundAccount =
                readManager.getByUserIdAndId(command.userId(), command.refundAccountId());
        refundAccount.delete(Instant.now());
        commandManager.persist(refundAccount);
    }
}
