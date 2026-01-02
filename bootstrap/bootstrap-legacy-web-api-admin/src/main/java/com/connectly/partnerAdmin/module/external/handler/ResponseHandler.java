package com.connectly.partnerAdmin.module.external.handler;

import com.connectly.partnerAdmin.module.external.payload.ExternalResponse;
import org.springframework.http.ResponseEntity;

public interface ResponseHandler<T extends ResponseEntity<? extends ExternalResponse<R>>, R> {

    R handleResponse(T response);
}