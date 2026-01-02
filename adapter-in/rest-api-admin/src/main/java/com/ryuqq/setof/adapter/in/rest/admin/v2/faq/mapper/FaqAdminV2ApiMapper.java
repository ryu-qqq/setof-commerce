package com.ryuqq.setof.adapter.in.rest.admin.v2.faq.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command.ChangeFaqCategoryV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command.CreateFaqV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command.UpdateFaqV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.query.SearchFaqV2ApiRequest;
import com.ryuqq.setof.application.faq.dto.command.ChangeFaqCategoryCommand;
import com.ryuqq.setof.application.faq.dto.command.CreateFaqCommand;
import com.ryuqq.setof.application.faq.dto.command.UpdateFaqCommand;
import com.ryuqq.setof.application.faq.dto.query.SearchFaqQuery;
import org.springframework.stereotype.Component;

/**
 * FAQ Admin V2 API Mapper
 *
 * <p>API Request DTO → Application Command/Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class FaqAdminV2ApiMapper {

    /**
     * CreateRequest → CreateCommand 변환
     *
     * @param request API 요청
     * @param createdBy 작성자 ID (인증 정보에서 추출)
     * @return Application Command
     */
    public CreateFaqCommand toCreateCommand(CreateFaqV2ApiRequest request, Long createdBy) {
        return new CreateFaqCommand(
                request.categoryCode(),
                request.question(),
                request.answer(),
                request.displayOrder(),
                createdBy);
    }

    /**
     * UpdateRequest → UpdateCommand 변환
     *
     * @param faqId FAQ ID
     * @param request API 요청
     * @param updatedBy 수정자 ID (인증 정보에서 추출)
     * @return Application Command
     */
    public UpdateFaqCommand toUpdateCommand(
            Long faqId, UpdateFaqV2ApiRequest request, Long updatedBy) {
        return new UpdateFaqCommand(
                faqId, request.question(), request.answer(), request.displayOrder(), updatedBy);
    }

    /**
     * ChangeCategoryRequest → ChangeCategoryCommand 변환
     *
     * @param faqId FAQ ID
     * @param request API 요청
     * @param updatedBy 수정자 ID (인증 정보에서 추출)
     * @return Application Command
     */
    public ChangeFaqCategoryCommand toChangeCategoryCommand(
            Long faqId, ChangeFaqCategoryV2ApiRequest request, Long updatedBy) {
        return new ChangeFaqCategoryCommand(faqId, request.newCategoryCode(), updatedBy);
    }

    /**
     * SearchRequest → SearchQuery 변환 (Admin용)
     *
     * @param request API 요청
     * @return Application Query
     */
    public SearchFaqQuery toSearchQuery(SearchFaqV2ApiRequest request) {
        return SearchFaqQuery.forAdmin(
                request.categoryCode(),
                request.status(),
                request.isTop(),
                request.keyword(),
                request.getPage(),
                request.getSize());
    }
}
