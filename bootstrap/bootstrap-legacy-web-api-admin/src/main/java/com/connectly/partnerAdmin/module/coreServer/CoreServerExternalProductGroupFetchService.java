package com.connectly.partnerAdmin.module.coreServer;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroupContext;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CoreServerExternalProductGroupFetchService implements ExternalProductFetchInterface {

    private final CoreServerClient coreServerClient;

    public CoreServerExternalProductGroupFetchService(CoreServerClient coreServerClient) {
        this.coreServerClient = coreServerClient;
    }

    @Override
    public DefaultExternalProductGroupContext fetchBySiteIdAndExternalProductGroupId(long siteId, String externalProductGroupId) {
        String traceIdHeader = MDC.get("traceId");

        ResponseEntity<ApiResponse<Object>> apiResponseResponseEntity = coreServerClient.fetchBySiteIdAndExternalProductGroupId(
            traceIdHeader, siteId, externalProductGroupId);

        if(apiResponseResponseEntity.getStatusCode().is2xxSuccessful()){
            Object data = apiResponseResponseEntity.getBody().getData();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(data, DefaultExternalProductGroupContext.class);
        }

        throw new RuntimeException("Failed to fetch external product group context");
    }
}
