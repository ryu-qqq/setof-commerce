package com.connectly.partnerAdmin.module.coreServer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.connectly.partnerAdmin.module.coreServer.request.PreSignedUrlRequestDto;
import com.connectly.partnerAdmin.module.coreServer.response.PreSignedUrlResponseDto;
import com.connectly.partnerAdmin.module.payload.ApiResponse;

@FeignClient(
    name = "chaliceClient",
    url = "https://dicq8semo9.execute-api.ap-northeast-2.amazonaws.com/api"
)
public interface ChaliceClient {

    @PostMapping("/upload-url")
    ResponseEntity<ApiResponse<PreSignedUrlResponseDto>> getPreSignedUrl(
        @RequestHeader("X-Trace-Id") String traceIdHeader,
        @RequestBody PreSignedUrlRequestDto preSignedUrlRequestDto);

}
