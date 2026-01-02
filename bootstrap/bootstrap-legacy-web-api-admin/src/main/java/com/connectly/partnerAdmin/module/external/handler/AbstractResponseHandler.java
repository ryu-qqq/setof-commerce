package com.connectly.partnerAdmin.module.external.handler;

import com.connectly.partnerAdmin.module.external.payload.ExternalResponse;
import org.springframework.http.ResponseEntity;

public abstract class AbstractResponseHandler<T extends ResponseEntity<? extends ExternalResponse<R>>, R> implements ResponseHandler<T, R> {

    @Override
    public R handleResponse(T response) {
        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            ExternalResponse<R> body = response.getBody();
            if (body != null) {
                return body.getData();
            } else {
                handleNullBody();
            }
        } else {
            handleError(response);
        }
        return null;
    }

    protected abstract void handleNullBody();
    protected abstract void handleError(T response);
}
