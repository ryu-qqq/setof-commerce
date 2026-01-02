package com.connectly.partnerAdmin.module.external.service.auth;

import com.connectly.partnerAdmin.module.order.enums.SiteName;

public interface ExternalSiteTokenService {

    SiteName getSiteName();

    String getToken();

}
