package com.ryuqq.setof.application.refundaccount.factory.command;

import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.AccountHolderName;
import com.ryuqq.setof.domain.refundaccount.vo.AccountNumber;
import org.springframework.stereotype.Component;

/**
 * RefundAccount Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성 로직 캡슐화
 *   <li>Value Object 생성 책임
 *   <li>검증 완료 상태로 생성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountCommandFactory {

    private final ClockHolder clockHolder;

    public RefundAccountCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 신규 환불계좌 생성 (검증 완료 상태)
     *
     * <p>외부 계좌 검증 완료 후 호출되어야 함
     *
     * @param command 환불계좌 등록 커맨드
     * @return 생성된 RefundAccount (저장 전, ID 없음)
     */
    public RefundAccount createVerified(RegisterRefundAccountCommand command) {
        return RefundAccount.forNewVerified(
                command.memberId(),
                command.bankId(),
                AccountNumber.of(command.accountNumber()),
                AccountHolderName.of(command.accountHolderName()),
                clockHolder.getClock());
    }

    /**
     * 기존 환불계좌 수정 적용 (검증 완료)
     *
     * <p>외부 계좌 검증 완료 후 호출되어야 함
     *
     * @param refundAccount 수정할 환불계좌 도메인 객체
     * @param command 수정 커맨드
     */
    public void applyUpdateVerified(
            RefundAccount refundAccount, UpdateRefundAccountCommand command) {
        refundAccount.updateVerified(
                command.bankId(),
                AccountNumber.of(command.accountNumber()),
                AccountHolderName.of(command.accountHolderName()),
                clockHolder.getClock());
    }

    /**
     * 신규 환불계좌 생성 (검증 완료, 은행 이름 기반)
     *
     * <p>V1 레거시 API 지원을 위한 메서드입니다. bankName으로 조회한 Bank 객체에서 bankId를 추출하여 생성합니다.
     *
     * @param command 환불계좌 등록 커맨드 (은행 이름 기반)
     * @param bank 조회된 은행 도메인 객체
     * @return 생성된 RefundAccount (저장 전, ID 없음)
     * @deprecated V2 API에서는 bankId 기반 createVerified 사용 권장
     */
    @Deprecated
    public RefundAccount createVerifiedByBankName(
            RegisterRefundAccountByBankNameCommand command, Bank bank) {
        return RefundAccount.forNewVerified(
                command.memberId(),
                bank.getId().value(),
                AccountNumber.of(command.accountNumber()),
                AccountHolderName.of(command.accountHolderName()),
                clockHolder.getClock());
    }

    /**
     * 기존 환불계좌 수정 적용 (검증 완료, 은행 이름 기반)
     *
     * <p>V1 레거시 API 지원을 위한 메서드입니다. bankName으로 조회한 Bank 객체에서 bankId를 추출하여 수정합니다.
     *
     * @param refundAccount 수정할 환불계좌 도메인 객체
     * @param command 수정 커맨드 (은행 이름 기반)
     * @param bank 조회된 은행 도메인 객체
     * @deprecated V2 API에서는 bankId 기반 applyUpdateVerified 사용 권장
     */
    @Deprecated
    public void applyUpdateVerifiedByBankName(
            RefundAccount refundAccount, UpdateRefundAccountByBankNameCommand command, Bank bank) {
        refundAccount.updateVerified(
                bank.getId().value(),
                AccountNumber.of(command.accountNumber()),
                AccountHolderName.of(command.accountHolderName()),
                clockHolder.getClock());
    }
}
