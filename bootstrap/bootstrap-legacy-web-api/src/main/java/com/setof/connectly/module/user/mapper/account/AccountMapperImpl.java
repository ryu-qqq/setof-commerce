package com.setof.connectly.module.user.mapper.account;

import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderDto;
import com.setof.connectly.module.payment.enums.account.VBankCode;
import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.entity.RefundAccount;
import com.setof.connectly.module.utils.SecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements AccountMapper {

    public RefundAccount toEntity(CreateRefundAccount createRefundAccount) {
        return RefundAccount.builder()
                .accountNumber(createRefundAccount.getAccountNumber())
                .accountHolderName(createRefundAccount.getAccountHolderName())
                .bankName(createRefundAccount.getBankName())
                .userId(SecurityUtils.currentUserId())
                .build();
    }

    @Override
    public RefundAccountInfo toResponse(RefundAccount refundAccount) {
        return RefundAccountInfo.builder()
                .refundAccountId(refundAccount.getId())
                .accountNumber(refundAccount.getAccountNumber())
                .bankName(refundAccount.getBankName())
                .accountHolderName(refundAccount.getAccountHolderName())
                .build();
    }

    @Override
    public PortOneVBankHolderDto toPortOneVBankDto(CreateRefundAccount createRefundAccount) {
        return PortOneVBankHolderDto.builder()
                .bankCode(VBankCode.ofDisplayName(createRefundAccount.getBankName()).getCode())
                .bankNum(createRefundAccount.getAccountNumber())
                .build();
    }
}
