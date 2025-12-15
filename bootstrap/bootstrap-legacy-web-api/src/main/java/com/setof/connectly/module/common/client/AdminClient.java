package com.setof.connectly.module.common.client;

import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderDto;
import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "adminClient", url = "${admin.url}")
public interface AdminClient {

    @GetMapping("/vbank/holder")
    ResponseEntity<ApiResponse<PortOneVBankHolderResponse>> fetchVBankHolder(
            @RequestHeader("apikey") String apiKey,
            @RequestBody PortOneVBankHolderDto portOneVBankHolderDto);
}
