package com.connectly.partnerAdmin.module.external.handler;

import com.connectly.partnerAdmin.module.common.exception.UnExpectedException;
import com.connectly.partnerAdmin.module.external.payload.ExternalResponse;
import com.connectly.partnerAdmin.module.external.payload.SeWonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SewonResponseHandler <R> extends AbstractResponseHandler<ResponseEntity<SeWonResponse<R>>, R>{

    private static final String SEWON_SERVER_ERROR_MSG = "Failed to fetch data";
    private static final String SEWON_DATA_NULL_MSG = "Response body is null";
    private static final String SEWON_ERROR_MSG = "error";


    @Override
    public R handleResponse(ResponseEntity<SeWonResponse<R>> response) {
        if(response == null) throw new UnExpectedException(SEWON_SERVER_ERROR_MSG);
        if (response.getStatusCode().is2xxSuccessful()) {
            ExternalResponse<R> body = response.getBody();
            if (body != null) {
                String message = body.getResult();
                if(message.equals(SEWON_ERROR_MSG)) throw new UnExpectedException(body.getMessage());

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
        throw new UnExpectedException(SEWON_DATA_NULL_MSG);
    }

    @Override
    protected void handleError(ResponseEntity<SeWonResponse<R>> response) {
        throw new UnExpectedException(SEWON_SERVER_ERROR_MSG + response.getStatusCode());

    }
}
