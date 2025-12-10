package com.ryuqq.setof.application.refundaccount.service.query;

import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.in.query.GetRefundAccountUseCase;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Get RefundAccount Service
 *
 * <p>환불계좌 조회 UseCase 구현체
 *
 * <p>회원당 최대 1개이므로 memberId로 단건 조회
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetRefundAccountService implements GetRefundAccountUseCase {

    private final RefundAccountReadManager refundAccountReadManager;
    private final RefundAccountAssembler refundAccountAssembler;
    private final BankReadManager bankReadManager;

    public GetRefundAccountService(
            RefundAccountReadManager refundAccountReadManager,
            RefundAccountAssembler refundAccountAssembler,
            BankReadManager bankReadManager) {
        this.refundAccountReadManager = refundAccountReadManager;
        this.refundAccountAssembler = refundAccountAssembler;
        this.bankReadManager = bankReadManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefundAccountResponse> execute(UUID memberId) {
        Optional<RefundAccount> refundAccountOpt =
                refundAccountReadManager.findByMemberId(memberId);

        if (refundAccountOpt.isEmpty()) {
            return Optional.empty();
        }

        RefundAccount refundAccount = refundAccountOpt.get();
        Bank bank = bankReadManager.findById(refundAccount.getBankId());

        return Optional.of(refundAccountAssembler.toResponse(refundAccount, bank));
    }
}
