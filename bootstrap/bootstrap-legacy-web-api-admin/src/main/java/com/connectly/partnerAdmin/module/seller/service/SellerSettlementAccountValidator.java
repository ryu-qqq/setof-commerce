package com.connectly.partnerAdmin.module.seller.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.portone.service.PgPaymentService;
import com.connectly.partnerAdmin.module.seller.controller.request.CreateSellerSettlementAccount;

@Component
@RequiredArgsConstructor
public class SellerSettlementAccountValidator {

    private final PgPaymentService pgPaymentService;

    public boolean checkAccount(CreateSellerSettlementAccount createSellerSettlementAccount){
        String findName = pgPaymentService.validateAccount(createSellerSettlementAccount);

        if(!findName.equals(createSellerSettlementAccount.getAccountHolderName())){
            throw new IllegalArgumentException("예금주명이 일치하지 않습니다.  =>" +  findName);
        }

        return true;
    }

}
