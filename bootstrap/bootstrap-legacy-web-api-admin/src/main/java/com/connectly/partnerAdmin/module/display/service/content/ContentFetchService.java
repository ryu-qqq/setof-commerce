package com.connectly.partnerAdmin.module.display.service.content;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse;
import com.connectly.partnerAdmin.module.display.filter.ContentFilter;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import org.springframework.data.domain.Pageable;

public interface ContentFetchService {

    Content fetchContentEntity(long contentId);
    ContentGroupResponse fetchContent(long contentId);
    CustomPageable<ContentResponse> fetchContents(ContentFilter filterDto, Pageable pageable);

}
