package com.setof.connectly.module.user.service.account;

import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;

public interface RefundAccountQueryService {

    RefundAccountInfo saveRefundAccount(CreateRefundAccount createRefundAccount);

    RefundAccountInfo updateRefundAccount(
            CreateRefundAccount createRefundAccount, long refundAccountId);

    RefundAccountInfo deleteRefundAccount(long refundAccountId);
}
