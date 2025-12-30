package com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.command.CreateFaqCategoryV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.command.UpdateFaqCategoryV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.query.SearchFaqCategoryV2ApiRequest;
import com.ryuqq.setof.application.faqcategory.dto.command.CreateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.dto.command.UpdateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.dto.query.SearchFaqCategoryQuery;
import org.springframework.stereotype.Component;

/**
 * FAQ Category Admin V2 API Mapper
 *
 * <p>API Request DTO → Application Command/Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class FaqCategoryAdminV2ApiMapper {

    /**
     * CreateRequest → CreateCommand 변환
     *
     * @param request API 요청
     * @param createdBy 작성자 ID (인증 정보에서 추출)
     * @return Application Command
     */
    public CreateFaqCategoryCommand toCreateCommand(
            CreateFaqCategoryV2ApiRequest request, Long createdBy) {
        return new CreateFaqCategoryCommand(
                request.code(),
                request.name(),
                request.description(),
                request.displayOrder(),
                createdBy);
    }

    /**
     * UpdateRequest → UpdateCommand 변환
     *
     * @param categoryId 카테고리 ID
     * @param request API 요청
     * @param updatedBy 수정자 ID (인증 정보에서 추출)
     * @return Application Command
     */
    public UpdateFaqCategoryCommand toUpdateCommand(
            Long categoryId, UpdateFaqCategoryV2ApiRequest request, Long updatedBy) {
        return new UpdateFaqCategoryCommand(
                categoryId,
                request.name(),
                request.description(),
                request.displayOrder(),
                updatedBy);
    }

    /**
     * SearchRequest → SearchQuery 변환 (Admin용)
     *
     * @param request API 요청
     * @return Application Query
     */
    public SearchFaqCategoryQuery toSearchQuery(SearchFaqCategoryV2ApiRequest request) {
        return SearchFaqCategoryQuery.forAdmin(
                request.status(), request.keyword(), request.getPage(), request.getSize());
    }
}
