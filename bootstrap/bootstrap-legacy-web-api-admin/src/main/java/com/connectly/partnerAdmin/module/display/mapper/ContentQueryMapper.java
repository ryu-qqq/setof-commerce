package com.connectly.partnerAdmin.module.display.mapper;

import com.connectly.partnerAdmin.module.display.dto.content.query.CreateContent;
import com.connectly.partnerAdmin.module.display.entity.content.Content;

public interface ContentQueryMapper {

    Content toEntity(CreateContent createContent);

}
