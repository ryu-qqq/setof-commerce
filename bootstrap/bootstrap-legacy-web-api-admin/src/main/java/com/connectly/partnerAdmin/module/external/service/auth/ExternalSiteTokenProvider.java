package com.connectly.partnerAdmin.module.external.service.auth;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalSiteTokenProvider extends AbstractProvider<SiteName, ExternalSiteTokenService> {

    public ExternalSiteTokenProvider(List<ExternalSiteTokenService> services) {
        for (ExternalSiteTokenService service : services) {
            map.put(service.getSiteName(), service);
        }
    }

}
