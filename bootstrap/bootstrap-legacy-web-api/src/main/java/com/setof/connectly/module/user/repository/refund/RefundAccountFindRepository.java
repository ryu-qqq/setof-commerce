package com.setof.connectly.module.user.repository.refund;

import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.entity.RefundAccount;
import java.util.Optional;

public interface RefundAccountFindRepository {
    Optional<RefundAccountInfo> fetchRefundAccount(long userId);

    Optional<RefundAccountInfo> fetchRefundAccountByPaymentId(long paymentId);

    Optional<RefundAccount> fetchRefundAccountEntity(long refundAccountId, long userId);
}
