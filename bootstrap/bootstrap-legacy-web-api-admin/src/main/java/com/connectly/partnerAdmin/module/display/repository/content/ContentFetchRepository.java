package com.connectly.partnerAdmin.module.display.repository.content;

import com.connectly.partnerAdmin.module.display.filter.ContentFilter;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.query.ContentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContentFetchRepository {

     Optional<ContentQueryDto> fetchContentQueryInfo(long contentId);
     Optional<Content> fetchContentEntity(long contentId);
     List<ContentResponse> fetchContents(ContentFilter filterDto, Pageable pageable);
     JPAQuery<Long> fetchContentQuery(ContentFilter filterDto, Pageable pageable);

}
