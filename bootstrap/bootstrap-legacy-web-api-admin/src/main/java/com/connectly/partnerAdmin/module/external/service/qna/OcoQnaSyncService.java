package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.external.annotation.RequiresAccessToken;
import com.connectly.partnerAdmin.module.external.client.OcoClient;
import com.connectly.partnerAdmin.module.external.dto.qna.OcoQnaRequest;
import com.connectly.partnerAdmin.module.external.handler.OcoResponseHandler;
import com.connectly.partnerAdmin.module.external.payload.OcoResponse;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaContents;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OcoQnaSyncService implements ExternalQnaSyncService {

    private final OcoClient ocoClient;
    private final OcoResponseHandler<Object> ocoResponseHandler;

    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }

    @RequiresAccessToken(siteName = SiteName.OCO)
    @Override
    public void doAnswer(long externalIdx, CreateQnaContents qnaContents) {
        OcoQnaRequest ocoQnaRequest = new OcoQnaRequest(externalIdx, qnaContents.getTitle(), qnaContents.getContent());
        ResponseEntity<OcoResponse<Object>> ocoResponseResponseEntity = ocoClient.doAnswer(ocoQnaRequest);
        ocoResponseHandler.handleResponse(ocoResponseResponseEntity);
    }
}
