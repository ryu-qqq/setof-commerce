package com.ryuqq.setof.storage.legacy.composite.web.content.mapper;

import com.ryuqq.setof.application.legacy.content.dto.response.LegacyComponentResult;
import com.ryuqq.setof.application.legacy.content.dto.response.LegacyContentResult;
import com.ryuqq.setof.application.legacy.content.dto.response.LegacyViewExtensionResult;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebComponentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebContentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.content.dto.LegacyWebViewExtensionQueryDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebContentMapper - 레거시 웹 콘텐츠 Mapper.
 *
 * <p>QueryDto -> Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebContentMapper {

    /**
     * ContentQueryDto -> ContentResult 변환 (메타데이터만).
     *
     * @param dto ContentQueryDto
     * @return LegacyContentResult
     */
    public LegacyContentResult toMetaDataResult(LegacyWebContentQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyContentResult.ofMetaData(
                dto.contentId(),
                dto.title(),
                dto.memo(),
                dto.imageUrl(),
                dto.displayStartDate(),
                dto.displayEndDate());
    }

    /**
     * ContentQueryDto + ComponentResult 목록 -> ContentResult 변환.
     *
     * @param dto ContentQueryDto
     * @param components 컴포넌트 목록
     * @return LegacyContentResult
     */
    public LegacyContentResult toResult(
            LegacyWebContentQueryDto dto, List<LegacyComponentResult> components) {
        if (dto == null) {
            return null;
        }
        return LegacyContentResult.of(
                dto.contentId(),
                dto.title(),
                dto.memo(),
                dto.imageUrl(),
                dto.displayStartDate(),
                dto.displayEndDate(),
                components);
    }

    /**
     * ComponentQueryDto 목록 -> ComponentResult 목록 변환.
     *
     * @param dtos ComponentQueryDto 목록
     * @param viewExtensionMap 뷰 확장 맵 (viewExtensionId -> ViewExtensionQueryDto)
     * @return LegacyComponentResult 목록
     */
    public List<LegacyComponentResult> toComponentResults(
            List<LegacyWebComponentQueryDto> dtos,
            Map<Long, LegacyWebViewExtensionQueryDto> viewExtensionMap) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(dto -> toComponentResult(dto, viewExtensionMap))
                .collect(Collectors.toList());
    }

    /**
     * ComponentQueryDto -> ComponentResult 변환.
     *
     * @param dto ComponentQueryDto
     * @param viewExtensionMap 뷰 확장 맵
     * @return LegacyComponentResult
     */
    public LegacyComponentResult toComponentResult(
            LegacyWebComponentQueryDto dto,
            Map<Long, LegacyWebViewExtensionQueryDto> viewExtensionMap) {
        if (dto == null) {
            return null;
        }

        LegacyViewExtensionResult viewExtension = LegacyViewExtensionResult.empty();
        if (dto.viewExtensionId() != null && viewExtensionMap.containsKey(dto.viewExtensionId())) {
            viewExtension = toViewExtensionResult(viewExtensionMap.get(dto.viewExtensionId()));
        }

        return LegacyComponentResult.of(
                dto.componentId(),
                dto.contentId(),
                dto.componentName(),
                dto.componentType(),
                dto.listType(),
                dto.orderType(),
                dto.badgeType(),
                dto.filterYn(),
                dto.exposedProducts(),
                dto.displayOrder(),
                dto.displayStartDate(),
                dto.displayEndDate(),
                viewExtension);
    }

    /**
     * ViewExtensionQueryDto -> ViewExtensionResult 변환.
     *
     * @param dto ViewExtensionQueryDto
     * @return LegacyViewExtensionResult
     */
    public LegacyViewExtensionResult toViewExtensionResult(LegacyWebViewExtensionQueryDto dto) {
        if (dto == null) {
            return LegacyViewExtensionResult.empty();
        }
        return LegacyViewExtensionResult.of(
                dto.viewExtensionId(),
                dto.viewExtensionType(),
                dto.linkUrl(),
                dto.buttonName(),
                dto.productCountPerClick(),
                dto.maxClickCount(),
                dto.afterMaxActionType(),
                dto.afterMaxActionLinkUrl());
    }

    /**
     * ViewExtensionQueryDto 목록 -> Map 변환.
     *
     * @param dtos ViewExtensionQueryDto 목록
     * @return Map (viewExtensionId -> ViewExtensionQueryDto)
     */
    public Map<Long, LegacyWebViewExtensionQueryDto> toViewExtensionMap(
            List<LegacyWebViewExtensionQueryDto> dtos) {
        if (dtos == null) {
            return Map.of();
        }
        return dtos.stream()
                .collect(
                        Collectors.toMap(
                                LegacyWebViewExtensionQueryDto::viewExtensionId, dto -> dto));
    }
}
