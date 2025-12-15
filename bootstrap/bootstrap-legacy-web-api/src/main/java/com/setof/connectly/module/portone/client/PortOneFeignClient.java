package com.setof.connectly.module.portone.client;

import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderResponse;
import com.siot.IamportRestClient.response.IamportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "portOneClient", url = "${Import.import-url}")
public interface PortOneFeignClient {

    @GetMapping("/vbanks/holder")
    ResponseEntity<IamportResponse<PortOneVBankHolderResponse>> fetchVBankHolder(
            @RequestHeader("Authorization") String token,
            @RequestParam("bank_code") String bankCode,
            @RequestParam("bank_num") String bankNum);
}
