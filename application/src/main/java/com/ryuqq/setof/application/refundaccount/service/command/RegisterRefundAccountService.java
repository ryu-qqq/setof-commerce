package com.ryuqq.setof.application.refundaccount.service.command;

import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.component.RefundAccountValidator;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.factory.command.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.command.RefundAccountPersistenceManager;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.in.command.RegisterRefundAccountUseCase;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountAlreadyExistsException;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import org.springframework.stereotype.Service;

/**
 * Register RefundAccount Service
 *
 * <p>환불계좌 등록 UseCase 구현체
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>회원당 최대 1개까지만 등록 가능
 *   <li>등록 시 외부 계좌 검증 API 통해 검증 필수
 *   <li>검증 실패 시 저장 불가
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterRefundAccountService implements RegisterRefundAccountUseCase {

    private final RefundAccountReadManager refundAccountReadManager;
    private final RefundAccountPersistenceManager refundAccountPersistenceManager;
    private final RefundAccountCommandFactory refundAccountCommandFactory;
    private final RefundAccountAssembler refundAccountAssembler;
    private final BankReadManager bankReadManager;
    private final RefundAccountValidator refundAccountValidator;

    public RegisterRefundAccountService(
            RefundAccountReadManager refundAccountReadManager,
            RefundAccountPersistenceManager refundAccountPersistenceManager,
            RefundAccountCommandFactory refundAccountCommandFactory,
            RefundAccountAssembler refundAccountAssembler,
            BankReadManager bankReadManager,
            RefundAccountValidator refundAccountValidator) {
        this.refundAccountReadManager = refundAccountReadManager;
        this.refundAccountPersistenceManager = refundAccountPersistenceManager;
        this.refundAccountCommandFactory = refundAccountCommandFactory;
        this.refundAccountAssembler = refundAccountAssembler;
        this.bankReadManager = bankReadManager;
        this.refundAccountValidator = refundAccountValidator;
    }

    @Override
    public RefundAccountResponse execute(RegisterRefundAccountCommand command) {
        validateNoExistingAccount(command);

        Bank bank = bankReadManager.findById(command.bankId());

        verifyAccount(bank, command);

        RefundAccount refundAccount = refundAccountCommandFactory.createVerified(command);
        RefundAccountId savedId = refundAccountPersistenceManager.persist(refundAccount);

        RefundAccount savedAccount = refundAccountReadManager.findById(savedId.value());
        return refundAccountAssembler.toResponse(savedAccount, bank);
    }

    private void validateNoExistingAccount(RegisterRefundAccountCommand command) {
        if (refundAccountReadManager.existsByMemberId(command.memberId())) {
            throw new RefundAccountAlreadyExistsException(command.memberId());
        }
    }

    private void verifyAccount(Bank bank, RegisterRefundAccountCommand command) {
        refundAccountValidator.validateAccount(
                bank.getBankCodeValue(), command.accountNumber(), command.accountHolderName());
    }

    /**
     * 환불계좌 등록 실행 (은행 이름 기반)
     *
     * <p>V1 레거시 API 지원을 위한 메서드입니다. bankName으로 은행을 조회하여 등록합니다.
     *
     * @param command 환불계좌 등록 커맨드 (은행 이름 기반)
     * @return 등록된 환불계좌 정보
     */
    @Override
    @Deprecated
    public RefundAccountResponse execute(RegisterRefundAccountByBankNameCommand command) {
        validateNoExistingAccountByBankName(command);

        Bank bank = bankReadManager.findByBankName(command.bankName());

        verifyAccountByBankName(bank, command);

        RefundAccount refundAccount =
                refundAccountCommandFactory.createVerifiedByBankName(command, bank);
        RefundAccountId savedId = refundAccountPersistenceManager.persist(refundAccount);

        RefundAccount savedAccount = refundAccountReadManager.findById(savedId.value());
        return refundAccountAssembler.toResponse(savedAccount, bank);
    }

    private void validateNoExistingAccountByBankName(
            RegisterRefundAccountByBankNameCommand command) {
        if (refundAccountReadManager.existsByMemberId(command.memberId())) {
            throw new RefundAccountAlreadyExistsException(command.memberId());
        }
    }

    private void verifyAccountByBankName(
            Bank bank, RegisterRefundAccountByBankNameCommand command) {
        refundAccountValidator.validateAccount(
                bank.getBankCodeValue(), command.accountNumber(), command.accountHolderName());
    }
}
