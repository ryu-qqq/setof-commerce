package com.connectly.partnerAdmin.module.display.mapper;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.query.ContentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContentMapperImpl implements ContentMapper {

    @Override
    public ContentGroupResponse toContentGroupResponse(ContentQueryDto contentQueryDto, Map<Integer, SubComponent> combinedMap){
        List<SubComponent> components = combinedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        return ContentGroupResponse.builder()
                .contentId(contentQueryDto.getContentId())
                .memo(contentQueryDto.getMemo())
                .displayYn(contentQueryDto.getDisplayYn())
                .title(contentQueryDto.getTitle())
                .displayPeriod(contentQueryDto.getDisplayPeriod())
                .components(components)
                .imageUrl(contentQueryDto.getImageUrl())
                .build();
    }



    @Override
    public ContentResponse toContentResponse(Content content) {
        return ContentResponse.builder()
                .contentId(content.getId())
                .displayPeriod(content.getDisplayPeriod())
                .title(content.getTitle())
                .displayYn(content.getDisplayYn())
                .insertDate(content.getInsertDate())
                .insertOperator(content.getInsertOperator())
                .updateDate(content.getUpdateDate())
                .updateOperator(content.getUpdateOperator())
                .build();
    }

    @Override
    public CustomPageable<ContentResponse> toContentResponses(List<ContentResponse> contentResponses, Pageable pageable, long total) {
        Long lastDomainId = contentResponses.isEmpty() ? null : contentResponses.getLast().getContentId();
        return new CustomPageable<>(contentResponses, pageable, total, lastDomainId);

    }



}
