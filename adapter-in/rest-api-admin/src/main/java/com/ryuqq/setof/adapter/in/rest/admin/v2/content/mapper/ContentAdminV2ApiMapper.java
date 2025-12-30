package com.ryuqq.setof.adapter.in.rest.admin.v2.content.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.CreateContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.UpdateContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.query.SearchContentV2ApiRequest;
import com.ryuqq.setof.application.content.dto.command.CreateContentCommand;
import com.ryuqq.setof.application.content.dto.command.UpdateContentCommand;
import com.ryuqq.setof.application.content.dto.query.SearchContentQuery;
import org.springframework.stereotype.Component;

/**
 * Content Admin V2 API Mapper
 *
 * <p>API Request DTO → Application Command/Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ContentAdminV2ApiMapper {

    /**
     * CreateRequest → CreateCommand 변환
     *
     * @param request API 요청
     * @return Application Command
     */
    public CreateContentCommand toCreateCommand(CreateContentV2ApiRequest request) {
        return new CreateContentCommand(
                request.title(),
                request.memo(),
                request.imageUrl(),
                request.displayStartDate(),
                request.displayEndDate());
    }

    /**
     * UpdateRequest → UpdateCommand 변환
     *
     * @param contentId 콘텐츠 ID
     * @param request API 요청
     * @return Application Command
     */
    public UpdateContentCommand toUpdateCommand(Long contentId, UpdateContentV2ApiRequest request) {
        return new UpdateContentCommand(
                contentId,
                request.title(),
                request.memo(),
                request.imageUrl(),
                request.displayStartDate(),
                request.displayEndDate());
    }

    /**
     * SearchRequest → SearchQuery 변환
     *
     * @param request API 요청
     * @return Application Query
     */
    public SearchContentQuery toSearchQuery(SearchContentV2ApiRequest request) {
        return new SearchContentQuery(
                request.title(),
                request.status(),
                request.displayableAt(),
                request.getPage(),
                request.getSize());
    }
}
