package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.CreateBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.query.SearchBannerV2ApiRequest;
import com.ryuqq.setof.application.banner.dto.command.CreateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerCommand;
import com.ryuqq.setof.application.banner.dto.query.SearchBannerQuery;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import org.springframework.stereotype.Component;

/**
 * Banner Admin V2 API Mapper
 *
 * <p>API Request DTO → Application Command/Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class BannerAdminV2ApiMapper {

    /**
     * CreateRequest → CreateCommand 변환
     *
     * @param request API 요청
     * @return Application Command
     */
    public CreateBannerCommand toCreateCommand(CreateBannerV2ApiRequest request) {
        return new CreateBannerCommand(
                request.title(),
                request.bannerType(),
                request.displayStartDate(),
                request.displayEndDate());
    }

    /**
     * UpdateRequest → UpdateCommand 변환
     *
     * @param bannerId 배너 ID
     * @param request API 요청
     * @return Application Command
     */
    public UpdateBannerCommand toUpdateCommand(Long bannerId, UpdateBannerV2ApiRequest request) {
        return new UpdateBannerCommand(
                BannerId.of(bannerId),
                request.title(),
                request.displayStartDate(),
                request.displayEndDate());
    }

    /**
     * SearchRequest → SearchQuery 변환
     *
     * @param request API 요청
     * @return Application Query
     */
    public SearchBannerQuery toSearchQuery(SearchBannerV2ApiRequest request) {
        return new SearchBannerQuery(
                request.bannerType(),
                request.status(),
                request.displayableAt(),
                request.getOffset(),
                request.getSize());
    }
}
