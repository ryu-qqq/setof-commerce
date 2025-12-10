package com.ryuqq.setof.application.refundaccount.factory.command;

import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
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
    public void applyUpdateVerified(RefundAccount refundAccount, UpdateRefundAccountCommand command) {
        refundAccount.updateVerified(
                command.bankId(),
                AccountNumber.of(command.accountNumber()),
                AccountHolderName.of(command.accountHolderName()),
                clockHolder.getClock());
    }
}
