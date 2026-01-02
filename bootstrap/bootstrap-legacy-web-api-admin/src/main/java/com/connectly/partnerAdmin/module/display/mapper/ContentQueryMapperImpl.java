package com.connectly.partnerAdmin.module.display.mapper;

import com.connectly.partnerAdmin.module.display.dto.content.query.CreateContent;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ContentQueryMapperImpl implements ContentQueryMapper{
    @Override
    public Content toEntity(CreateContent createContent) {

        Content.ContentBuilder contentBuilder = Content.builder()
                .displayPeriod(createContent.getDisplayPeriod())
                .displayYn(createContent.getDisplayYn())
                .memo(createContent.getMemo())
                .title(createContent.getTitle())
                .imageUrl(createContent.getImageUrl());

        if(createContent.getContentId() != null) contentBuilder.id(createContent.getContentId());

        return contentBuilder.build();
    }

}
