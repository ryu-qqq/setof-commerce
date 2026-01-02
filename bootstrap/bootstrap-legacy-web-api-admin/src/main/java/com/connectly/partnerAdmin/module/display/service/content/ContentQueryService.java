package com.connectly.partnerAdmin.module.display.service.content;

import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.content.query.CreateContent;
import com.connectly.partnerAdmin.module.display.dto.content.query.UpdateContentDisplayYn;

public interface ContentQueryService {

    ContentResponse enrollContent(CreateContent createContent);

    ContentResponse updateDisplayYn(long contentId, UpdateContentDisplayYn updateContentDisplayYn);
}
