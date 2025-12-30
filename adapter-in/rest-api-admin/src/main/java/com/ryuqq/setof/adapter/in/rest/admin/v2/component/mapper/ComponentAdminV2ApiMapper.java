package com.ryuqq.setof.adapter.in.rest.admin.v2.component.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.ComponentDetailV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.CreateComponentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.UpdateComponentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.response.ComponentDetailV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.response.ComponentV2ApiResponse;
import com.ryuqq.setof.application.component.dto.command.ComponentDetailCommand;
import com.ryuqq.setof.application.component.dto.command.CreateComponentCommand;
import com.ryuqq.setof.application.component.dto.command.UpdateComponentCommand;
import com.ryuqq.setof.application.component.dto.response.ComponentDetailResponse;
import com.ryuqq.setof.application.component.dto.response.ComponentResponse;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Component Admin API Mapper
 *
 * <p>REST API DTO ↔ Application Command/Response 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ComponentAdminV2ApiMapper {

    /**
     * 생성 요청 → Command 변환
     *
     * @param contentId 컨텐츠 ID
     * @param request 생성 요청
     * @return CreateComponentCommand
     */
    public CreateComponentCommand toCreateCommand(
            Long contentId, CreateComponentV2ApiRequest request) {
        return new CreateComponentCommand(
                ContentId.of(contentId),
                request.componentType(),
                request.componentName(),
                request.displayOrder(),
                request.exposedProducts(),
                request.displayStartDate(),
                request.displayEndDate(),
                toDetailCommand(request.detail()));
    }

    /**
     * 수정 요청 → Command 변환
     *
     * @param componentId 컴포넌트 ID
     * @param request 수정 요청
     * @return UpdateComponentCommand
     */
    public UpdateComponentCommand toUpdateCommand(
            Long componentId, UpdateComponentV2ApiRequest request) {
        return new UpdateComponentCommand(
                ComponentId.of(componentId),
                request.componentName(),
                request.displayOrder(),
                request.exposedProducts(),
                request.displayStartDate(),
                request.displayEndDate(),
                request.detail() != null ? toDetailCommand(request.detail()) : null);
    }

    /**
     * Detail 요청 → Command 변환
     *
     * @param request Detail 요청
     * @return ComponentDetailCommand
     */
    private ComponentDetailCommand toDetailCommand(ComponentDetailV2ApiRequest request) {
        if (request == null) {
            return null;
        }
        return new ComponentDetailCommand(
                request.height(),
                request.showLine(),
                request.content(),
                request.title1(),
                request.title2(),
                request.subTitle1(),
                request.subTitle2(),
                request.imageType(),
                request.listType(),
                request.orderType(),
                request.badgeType(),
                request.showFilter(),
                request.categoryId(),
                request.stickyYn(),
                request.tabMovingType());
    }

    /**
     * Response → API Response 변환
     *
     * @param response Application Response
     * @return ComponentV2ApiResponse
     */
    public ComponentV2ApiResponse toApiResponse(ComponentResponse response) {
        return new ComponentV2ApiResponse(
                response.componentId(),
                response.contentId(),
                response.componentType(),
                response.componentName(),
                response.displayOrder(),
                response.status(),
                response.exposedProducts(),
                response.displayStartDate(),
                response.displayEndDate(),
                toDetailApiResponse(response.detail()),
                response.createdAt(),
                response.updatedAt());
    }

    /**
     * Response 목록 → API Response 목록 변환
     *
     * @param responses Application Response 목록
     * @return ComponentV2ApiResponse 목록
     */
    public List<ComponentV2ApiResponse> toApiResponses(List<ComponentResponse> responses) {
        return responses.stream().map(this::toApiResponse).toList();
    }

    /**
     * Detail Response → API Response 변환
     *
     * @param response Detail Response
     * @return ComponentDetailV2ApiResponse
     */
    private ComponentDetailV2ApiResponse toDetailApiResponse(ComponentDetailResponse response) {
        if (response == null) {
            return null;
        }
        return new ComponentDetailV2ApiResponse(
                response.height(),
                response.showLine(),
                response.content(),
                response.title1(),
                response.title2(),
                response.subTitle1(),
                response.subTitle2(),
                response.imageType(),
                response.listType(),
                response.orderType(),
                response.badgeType(),
                response.showFilter(),
                response.categoryId(),
                response.stickyYn(),
                response.tabMovingType());
    }
}
