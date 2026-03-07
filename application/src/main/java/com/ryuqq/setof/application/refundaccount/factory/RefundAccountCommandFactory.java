package com.ryuqq.setof.application.refundaccount.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccountUpdateData;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * RefundAccountCommandFactory - 환불 계좌 도메인 객체 생성 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountCommandFactory {

    private final TimeProvider timeProvider;

    public RefundAccountCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public RefundAccount createNewRefundAccount(RegisterRefundAccountCommand command) {
        Instant now = timeProvider.now();
        MemberId memberId = MemberId.of(String.valueOf(command.userId()));
        RefundBankInfo bankInfo =
                RefundBankInfo.of(
                        command.bankName(), command.accountNumber(), command.accountHolderName());
        return RefundAccount.forNew(memberId, bankInfo, now);
    }

    public RefundAccountUpdateData createUpdateData(UpdateRefundAccountCommand command) {
        Instant now = timeProvider.now();
        RefundBankInfo bankInfo =
                RefundBankInfo.of(
                        command.bankName(), command.accountNumber(), command.accountHolderName());
        return RefundAccountUpdateData.of(bankInfo, now);
    }
}
