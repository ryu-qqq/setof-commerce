package com.connectly.partnerAdmin.module.portone.service;


import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.connectly.partnerAdmin.module.payment.exception.PaymentNotFoundException;
import com.connectly.partnerAdmin.module.portone.client.PortOneClient;
import com.connectly.partnerAdmin.module.portone.dto.PortOneVBankHolderDto;
import com.connectly.partnerAdmin.module.portone.dto.PortOneVBankHolderResponse;
import com.connectly.partnerAdmin.module.portone.mapper.PortOneMapper;
import com.connectly.partnerAdmin.module.seller.controller.request.CreateSellerSettlementAccount;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PortOnePaymentService implements PgPaymentService {
    private final PortOneClient portOneClient;
    private final PortOneMapper portOneMapper;


    private Payment fetchPayment(String impUid){
        return portOneClient.fetchPaymentPortOne(impUid)
                .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    public void refundOrder(RefundOrder refundOrder) {
        Payment payment = fetchPayment(refundOrder.getPaymentAgencyId());
        CancelData cancelData = portOneMapper.toCancelData(refundOrder, payment.getAmount());
        portOneClient.refundOrder(refundOrder, cancelData);
    }

    @Override
    public String validateAccount(CreateSellerSettlementAccount createRefundAccount) {
        String token = portOneClient.fetchTokenPortOne();
        PortOneVBankHolderDto requestDto = new PortOneVBankHolderDto(createRefundAccount.getBankName(), createRefundAccount.getAccountNumber());
        PortOneVBankHolderResponse response = portOneClient.fetchVBankHolder(token, requestDto);
        return response.bankHolder();
    }


}
