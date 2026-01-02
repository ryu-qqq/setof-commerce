package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.external.entity.ExternalQna;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

public interface ExternalQnaService<T extends ExMallQna> {

    ExternalQna syncQna(T t);
    SiteName getSiteName();

}
