package com.connectly.partnerAdmin.module.external.service.auth;


import com.connectly.partnerAdmin.module.external.client.OcoClient;
import com.connectly.partnerAdmin.module.external.dto.auth.OcoTokenRequest;
import com.connectly.partnerAdmin.module.external.dto.auth.OcoTokenResponse;
import com.connectly.partnerAdmin.module.external.payload.OcoResponse;
import com.connectly.partnerAdmin.module.external.handler.OcoResponseHandler;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OcoAuthServiceImpl extends OcoResponseHandler<OcoTokenResponse> implements OcoAuthService{

    @Value("${oco.id}")
    private String username;
    @Value("${oco.password}")
    private String password;
    private final OcoClient ocoClient;

    @Override
    public String getToken() {
        ResponseEntity<OcoResponse<OcoTokenResponse>> accessToken = ocoClient.getAccessToken(new OcoTokenRequest(username, password));
        OcoTokenResponse ocoTokenResponse = handleResponse(accessToken);
        return ocoTokenResponse.getToken();
    }



    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }

}
