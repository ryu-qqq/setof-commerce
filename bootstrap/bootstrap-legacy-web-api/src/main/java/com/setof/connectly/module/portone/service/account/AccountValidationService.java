package com.setof.connectly.module.portone.service.account;

import com.setof.connectly.module.user.dto.account.CreateRefundAccount;

public interface AccountValidationService {
    boolean validateAccount(CreateRefundAccount createRefundAccount);
}
