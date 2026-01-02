package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalQnaRegistrationProvider<T extends ExMallQna> extends AbstractProvider<SiteName, ExternalQnaService<T>> {

    public ExternalQnaRegistrationProvider(List<ExternalQnaService<T>> services) {
        for(ExternalQnaService<T> service : services){
            map.put(service.getSiteName(), service);
        }
    }

}
