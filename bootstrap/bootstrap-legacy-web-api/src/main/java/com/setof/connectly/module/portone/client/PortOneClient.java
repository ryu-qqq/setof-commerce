package com.setof.connectly.module.portone.client;

import com.setof.connectly.module.exception.communication.ExternalCommunicationException;
import com.setof.connectly.module.exception.payment.RefundFailException;
import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderDto;
import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderResponse;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortOneClient {

    private final IamportClient iamportClient;
    private final PortOneFeignClient portOneFeignClient;
    private static final String TOKEN_PREFIX = "Bearer ";

    public String fetchTokenPortOne() {
        try {
            IamportResponse<AccessToken> auth = iamportClient.getAuth();
            if (auth.getCode() < 0) throw new ExternalCommunicationException(auth.getMessage());
            return TOKEN_PREFIX + auth.getResponse().getToken();
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PortOneVBankHolderResponse fetchVBankHolder(
            String token, PortOneVBankHolderDto requestDto) {
        try {
            ResponseEntity<IamportResponse<PortOneVBankHolderResponse>> response =
                    portOneFeignClient.fetchVBankHolder(
                            token, requestDto.getBankCode(), requestDto.getBankNum());
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().getResponse();
            }
        } catch (Exception e) {
            return new PortOneVBankHolderResponse("");
        }
        return new PortOneVBankHolderResponse("");
    }

    public Optional<Payment> fetchPaymentPortOne(String impUid) {
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
            if (response.getCode() < 0)
                throw new ExternalCommunicationException(response.getMessage());
            return Optional.of(response.getResponse());
        } catch (IamportResponseException | IOException e) {
            return Optional.empty();
        }
    }

    public void refundOrder(long paymentId, CancelData cancelData) {
        try {
            IamportResponse<Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);
            if (response.getCode() < 0) {
                throw new RefundFailException(paymentId);
            }

        } catch (IamportResponseException | IOException e) {
            throw new RefundFailException(paymentId);
        }
    }
}
