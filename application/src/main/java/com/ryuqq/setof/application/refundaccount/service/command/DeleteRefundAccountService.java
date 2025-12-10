package com.ryuqq.setof.application.refundaccount.service.command;

import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.manager.command.RefundAccountPersistenceManager;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.in.command.DeleteRefundAccountUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delete RefundAccount Service
 *
 * <p>환불계좌 삭제 UseCase 구현체
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>Soft Delete 적용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteRefundAccountService implements DeleteRefundAccountUseCase {

    private final RefundAccountReadManager refundAccountReadManager;
    private final RefundAccountPersistenceManager refundAccountPersistenceManager;
    private final ClockHolder clockHolder;

    public DeleteRefundAccountService(
            RefundAccountReadManager refundAccountReadManager,
            RefundAccountPersistenceManager refundAccountPersistenceManager,
            ClockHolder clockHolder) {
        this.refundAccountReadManager = refundAccountReadManager;
        this.refundAccountPersistenceManager = refundAccountPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public void execute(DeleteRefundAccountCommand command) {
        RefundAccount refundAccount = refundAccountReadManager.findById(command.refundAccountId());

        refundAccount.validateOwnership(command.memberId());

        refundAccount.delete(clockHolder.getClock());
        refundAccountPersistenceManager.persist(refundAccount);
    }
}
