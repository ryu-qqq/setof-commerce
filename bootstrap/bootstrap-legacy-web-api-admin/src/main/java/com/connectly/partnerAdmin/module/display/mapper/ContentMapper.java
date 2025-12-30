package com.connectly.partnerAdmin.module.display.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.query.ContentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ContentMapper {

    ContentGroupResponse toContentGroupResponse(ContentQueryDto contentQueryDto, Map<Integer, SubComponent> combinedMap);

    ContentResponse toContentResponse(Content content);

    CustomPageable<ContentResponse> toContentResponses(List<ContentResponse> contentResponses, Pageable pageable, long total);
}
