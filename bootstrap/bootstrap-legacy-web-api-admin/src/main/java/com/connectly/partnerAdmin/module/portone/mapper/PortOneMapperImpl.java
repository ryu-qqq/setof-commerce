package com.connectly.partnerAdmin.module.portone.mapper;


import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.connectly.partnerAdmin.module.payment.enums.VBankCode;
import com.siot.IamportRestClient.request.CancelData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PortOneMapperImpl implements PortOneMapper{


    @Override
    public CancelData toCancelData(RefundOrder refundOrder, BigDecimal canceledAmount) {
        CancelData cancelData = new CancelData(refundOrder.getPaymentAgencyId(), true, refundOrder.getRefundAmount().getAmount());
        //cancelData.setChecksum(refundOrder.getRefundAmount());

        if(refundOrder.getRefundAccountInfo()!= null){
            VBankCode vBankCode = VBankCode.ofDisplayName(refundOrder.getRefundAccountInfo().getBankName());
            cancelData.setRefund_holder(refundOrder.getRefundAccountInfo().getAccountHolderName());
            cancelData.setRefund_bank(vBankCode.getKcpCode());
            cancelData.setRefund_account(refundOrder.getRefundAccountInfo().getAccountNumber());
        }

        return cancelData;
    }

}
