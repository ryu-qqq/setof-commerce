package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalQnaAnswerProvider extends AbstractProvider<SiteName, ExternalQnaSyncService> {

    public ExternalQnaAnswerProvider(List<ExternalQnaSyncService> services) {
        for(ExternalQnaSyncService service : services){
            map.put(service.getSiteName(), service);
        }
    }

}
