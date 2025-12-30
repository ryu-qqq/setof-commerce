package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaContents;

public interface ExternalQnaSyncService {

    SiteName getSiteName();
    void doAnswer(long externalIdx, CreateQnaContents qnaContents);
}
