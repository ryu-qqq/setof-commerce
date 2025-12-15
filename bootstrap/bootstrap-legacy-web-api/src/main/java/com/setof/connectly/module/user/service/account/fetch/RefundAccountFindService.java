package com.setof.connectly.module.user.service.account.fetch;

import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.entity.RefundAccount;

public interface RefundAccountFindService {

    RefundAccountInfo fetchRefundAccountInfo();

    RefundAccount fetchRefundAccountEntity(long refundAccountId);

    RefundAccountInfo fetchRefundAccount(long paymentId);
}
