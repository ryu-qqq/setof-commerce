package com.connectly.partnerAdmin.module.portone.client;

import com.connectly.partnerAdmin.module.common.exception.UnExpectedException;
import com.connectly.partnerAdmin.module.order.dto.query.RefundOrder;
import com.connectly.partnerAdmin.module.payment.exception.RefundFailException;
import com.connectly.partnerAdmin.module.portone.dto.PortOneVBankHolderDto;
import com.connectly.partnerAdmin.module.portone.dto.PortOneVBankHolderResponse;
import com.connectly.partnerAdmin.module.portone.enums.PortOnePaymentStatus;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "portone.enabled", havingValue = "true", matchIfMissing = true)
public class PortOneClient {

    private final IamportClient iamportClient;
    private final PortOneFeignClient portOneFeignClient;
    private static final String TOKEN_PREFIX = "Bearer ";

    public String fetchTokenPortOne() {
        try {
            IamportResponse<AccessToken> auth = iamportClient.getAuth();
            if(auth.getCode()<0) throw new UnExpectedException(auth.getMessage());
            return TOKEN_PREFIX + auth.getResponse().getToken();
        } catch (IamportResponseException | IOException e) {
            throw new UnExpectedException(e.getMessage());
        }
    }


    public Optional<Payment> fetchPaymentPortOne(String impUid){
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
            if(response.getCode()<0) throw new UnExpectedException(response.getMessage());
            return Optional.of(response.getResponse());
        } catch (IamportResponseException | IOException e) {
            return Optional.empty();
        }
    }


    public PortOneVBankHolderResponse fetchVBankHolder(String token, PortOneVBankHolderDto requestDto){
        try{
            ResponseEntity<IamportResponse<PortOneVBankHolderResponse>> response = portOneFeignClient.fetchVBankHolder(token, requestDto.bankCode(), requestDto.bankNum());
            if(response.getStatusCode().is2xxSuccessful()){
                if(response.getBody() == null) return new PortOneVBankHolderResponse("");
                return response.getBody().getResponse();
            }
        }catch (Exception e){
            return new PortOneVBankHolderResponse("");
        }
        return new PortOneVBankHolderResponse("");
    }



    public void refundOrder(RefundOrder refundOrder, CancelData cancelData){
        try{
            IamportResponse<Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);

            if(response.getCode()<0) {
                throw new RefundFailException(refundOrder.getPaymentId(), response.getMessage());
            }

            Payment result = response.getResponse();

            if(result ==null) {
                throw new RefundFailException(refundOrder.getPaymentId(), response.getMessage());
            }

            if(!result.getStatus().equals(PortOnePaymentStatus.cancelled.toString())) {
                throw new RefundFailException(refundOrder.getPaymentId(), response.getMessage());
            }

        }catch (IamportResponseException | IOException e){
            throw new RefundFailException(refundOrder.getPaymentId(), e.getMessage());
        }
    }

}
