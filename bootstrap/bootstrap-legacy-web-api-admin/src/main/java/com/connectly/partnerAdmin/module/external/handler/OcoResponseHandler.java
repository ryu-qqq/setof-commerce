package com.connectly.partnerAdmin.module.external.handler;

import com.connectly.partnerAdmin.module.common.exception.UnExpectedException;
import com.connectly.partnerAdmin.module.external.exception.ExMallForbiddenException;
import com.connectly.partnerAdmin.module.external.payload.ExternalResponse;
import com.connectly.partnerAdmin.module.external.payload.OcoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OcoResponseHandler<R> extends AbstractResponseHandler<ResponseEntity<OcoResponse<R>>, R> {

    private static final String SUCCESS_MESSAGE = "success";

    private static final String OCO_AUTH_ERROR_MSG = "token 없음";
    private static final String OCO_DATA_NULL_MSG = "Response body is null";
    private static final String OCO_SERVER_ERROR_MSG = "Failed communication data";

    @Override
    public R handleResponse(ResponseEntity<OcoResponse<R>> response) {
        if(response == null) throw new UnExpectedException(OCO_SERVER_ERROR_MSG);
        if (response.getStatusCode().is2xxSuccessful()) {
            ExternalResponse<R> body = response.getBody();
            if (body != null) {
                String message = body.getMessage();
                if(message.equals(OCO_AUTH_ERROR_MSG)) throw new ExMallForbiddenException();
                if(!message.equals(SUCCESS_MESSAGE)) throw new UnExpectedException(OCO_SERVER_ERROR_MSG);
                return body.getData();
            }
            else {
                handleNullBody();
            }
        }

        handleError(response);
        return null;
    }

    @Override
    protected void handleNullBody() {
        throw new UnExpectedException(OCO_DATA_NULL_MSG);
    }

    @Override
    protected void handleError(ResponseEntity<OcoResponse<R>> response) {
        throw new UnExpectedException(OCO_SERVER_ERROR_MSG + response.getStatusCode());
    }

}
