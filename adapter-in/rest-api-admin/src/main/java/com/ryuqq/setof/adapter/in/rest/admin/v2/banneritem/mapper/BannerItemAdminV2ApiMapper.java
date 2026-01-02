package com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.command.CreateBannerItemV2ApiRequest;
import com.ryuqq.setof.application.banneritem.dto.command.CreateBannerItemCommand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerItem Admin API Mapper
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class BannerItemAdminV2ApiMapper {

    /**
     * 생성 요청을 Command로 변환
     *
     * @param bannerId 배너 ID
     * @param request 생성 요청
     * @return CreateBannerItemCommand
     */
    public CreateBannerItemCommand toCreateCommand(
            Long bannerId, CreateBannerItemV2ApiRequest request) {
        return new CreateBannerItemCommand(
                bannerId,
                request.title(),
                request.imageUrl(),
                request.linkUrl(),
                request.displayOrder(),
                request.displayStartDate(),
                request.displayEndDate(),
                request.imageWidth(),
                request.imageHeight());
    }

    /**
     * 생성 요청 목록을 Command 목록으로 변환
     *
     * @param bannerId 배너 ID
     * @param requests 생성 요청 목록
     * @return CreateBannerItemCommand 목록
     */
    public List<CreateBannerItemCommand> toCreateCommands(
            Long bannerId, List<CreateBannerItemV2ApiRequest> requests) {
        return requests.stream().map(request -> toCreateCommand(bannerId, request)).toList();
    }
}
