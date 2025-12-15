package com.ryuqq.setof.application.refundaccount.service.command;

import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.factory.command.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.command.RefundAccountPersistenceManager;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.in.command.UpdateRefundAccountUseCase;
import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Update RefundAccount Service
 *
 * <p>환불계좌 수정 UseCase 구현체
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>수정 시 외부 계좌 검증 API 통해 재검증 필수
 *   <li>검증 실패 시 수정 불가
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateRefundAccountService implements UpdateRefundAccountUseCase {

    private final RefundAccountReadManager refundAccountReadManager;
    private final RefundAccountPersistenceManager refundAccountPersistenceManager;
    private final RefundAccountCommandFactory refundAccountCommandFactory;
    private final RefundAccountAssembler refundAccountAssembler;
    private final BankReadManager bankReadManager;
    private final AccountVerificationPort accountVerificationPort;

    public UpdateRefundAccountService(
            RefundAccountReadManager refundAccountReadManager,
            RefundAccountPersistenceManager refundAccountPersistenceManager,
            RefundAccountCommandFactory refundAccountCommandFactory,
            RefundAccountAssembler refundAccountAssembler,
            BankReadManager bankReadManager,
            AccountVerificationPort accountVerificationPort) {
        this.refundAccountReadManager = refundAccountReadManager;
        this.refundAccountPersistenceManager = refundAccountPersistenceManager;
        this.refundAccountCommandFactory = refundAccountCommandFactory;
        this.refundAccountAssembler = refundAccountAssembler;
        this.bankReadManager = bankReadManager;
        this.accountVerificationPort = accountVerificationPort;
    }

    @Override
    @Transactional
    public RefundAccountResponse execute(UpdateRefundAccountCommand command) {
        RefundAccount refundAccount = refundAccountReadManager.findById(command.refundAccountId());

        refundAccount.validateOwnership(command.memberId());

        Bank bank = bankReadManager.findById(command.bankId());

        verifyAccount(bank, command);

        refundAccountCommandFactory.applyUpdateVerified(refundAccount, command);

        refundAccountPersistenceManager.persist(refundAccount);

        return refundAccountAssembler.toResponse(refundAccount, bank);
    }

    private void verifyAccount(Bank bank, UpdateRefundAccountCommand command) {
        boolean verified =
                accountVerificationPort.verifyAccount(
                        bank.getBankCodeValue(),
                        command.accountNumber(),
                        command.accountHolderName());

        if (!verified) {
            throw new AccountVerificationFailedException(
                    bank.getBankCodeValue(), command.accountNumber());
        }
    }

    /**
     * 환불계좌 수정 실행 (은행 이름 기반)
     *
     * <p>V1 레거시 API 지원을 위한 메서드입니다. bankName으로 은행을 조회하여 수정합니다.
     *
     * @param command 환불계좌 수정 커맨드 (은행 이름 기반)
     * @return 수정된 환불계좌 정보
     */
    @Override
    @Deprecated
    @Transactional
    public RefundAccountResponse execute(UpdateRefundAccountByBankNameCommand command) {
        RefundAccount refundAccount = refundAccountReadManager.findById(command.refundAccountId());

        refundAccount.validateOwnership(command.memberId());

        Bank bank = bankReadManager.findByBankName(command.bankName());

        verifyAccountByBankName(bank, command);

        refundAccountCommandFactory.applyUpdateVerifiedByBankName(refundAccount, command, bank);

        refundAccountPersistenceManager.persist(refundAccount);

        return refundAccountAssembler.toResponse(refundAccount, bank);
    }

    private void verifyAccountByBankName(Bank bank, UpdateRefundAccountByBankNameCommand command) {
        boolean verified =
                accountVerificationPort.verifyAccount(
                        bank.getBankCodeValue(),
                        command.accountNumber(),
                        command.accountHolderName());

        if (!verified) {
            throw new AccountVerificationFailedException(
                    bank.getBankCodeValue(), command.accountNumber());
        }
    }
}
