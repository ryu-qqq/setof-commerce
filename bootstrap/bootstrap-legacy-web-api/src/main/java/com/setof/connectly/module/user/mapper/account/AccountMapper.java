package com.setof.connectly.module.user.mapper.account;

import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderDto;
import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.entity.RefundAccount;

public interface AccountMapper {
    RefundAccount toEntity(CreateRefundAccount createRefundAccount);

    RefundAccountInfo toResponse(RefundAccount refundAccount);

    PortOneVBankHolderDto toPortOneVBankDto(CreateRefundAccount createRefundAccount);
}
