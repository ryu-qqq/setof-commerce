package com.ryuqq.setof.adapter.out.persistence.component.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.persistence.component.entity.ComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.component.mapper.dto.ComponentDetailDto;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.BlankDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.BrandDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.CategoryDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.ComponentDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.ImageDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.ProductDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.TabDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.TextDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.TitleDetail;
import com.ryuqq.setof.domain.cms.vo.BadgeType;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentName;
import com.ryuqq.setof.domain.cms.vo.ComponentStatus;
import com.ryuqq.setof.domain.cms.vo.ComponentType;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageType;
import com.ryuqq.setof.domain.cms.vo.ListType;
import com.ryuqq.setof.domain.cms.vo.OrderType;
import com.ryuqq.setof.domain.cms.vo.TabMovingType;
import com.ryuqq.setof.domain.common.util.ClockHolder;

/**
 * ComponentJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>ComponentDetail sealed interface를 JSON으로 직렬화/역직렬화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@org.springframework.stereotype.Component
public class ComponentJpaEntityMapper {

    private final ClockHolder clockHolder;
    private final ObjectMapper objectMapper;

    public ComponentJpaEntityMapper(ClockHolder clockHolder, ObjectMapper objectMapper) {
        this.clockHolder = clockHolder;
        this.objectMapper = objectMapper;
    }

    /** Domain -> Entity 변환 */
    public ComponentJpaEntity toEntity(Component domain) {
        return ComponentJpaEntity.of(
                domain.id() != null ? domain.id().value() : null,
                domain.contentId().value(),
                domain.componentType().name(),
                domain.componentName() != null ? domain.componentName().value() : null,
                domain.displayOrder() != null ? domain.displayOrder().value() : 0,
                domain.status().name(),
                domain.exposedProducts(),
                domain.displayPeriod() != null ? domain.displayPeriod().startDate() : null,
                domain.displayPeriod() != null ? domain.displayPeriod().endDate() : null,
                serializeDetail(domain.detail()),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    /** Entity -> Domain 변환 */
    public Component toDomain(ComponentJpaEntity entity) {
        ComponentType componentType = ComponentType.valueOf(entity.getComponentType());
        ComponentDetail detail = deserializeDetail(componentType, entity.getComponentDetail());

        DisplayPeriod displayPeriod = null;
        if (entity.getDisplayStartDate() != null && entity.getDisplayEndDate() != null) {
            displayPeriod =
                    DisplayPeriod.of(entity.getDisplayStartDate(), entity.getDisplayEndDate());
        }

        return Component.reconstitute(
                ComponentId.of(entity.getId()),
                ContentId.of(entity.getContentId()),
                componentType,
                entity.getComponentName() != null
                        ? ComponentName.of(entity.getComponentName())
                        : ComponentName.empty(),
                DisplayOrder.of(entity.getDisplayOrder()),
                ComponentStatus.valueOf(entity.getStatus()),
                entity.getExposedProducts(),
                displayPeriod,
                detail,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                clockHolder.getClock());
    }

    /** ComponentDetail -> JSON 문자열 변환 */
    private String serializeDetail(ComponentDetail detail) {
        if (detail == null) {
            return null;
        }
        try {
            ComponentDetailDto dto = toDto(detail);
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("ComponentDetail 직렬화 실패", e);
        }
    }

    /** JSON 문자열 -> ComponentDetail 변환 */
    private ComponentDetail deserializeDetail(ComponentType type, String json) {
        if (json == null || json.isBlank()) {
            return createDefaultDetail(type);
        }
        try {
            ComponentDetailDto dto = objectMapper.readValue(json, ComponentDetailDto.class);
            return fromDto(type, dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("ComponentDetail 역직렬화 실패", e);
        }
    }

    /** ComponentDetail -> DTO 변환 (Pattern Matching 사용) */
    private ComponentDetailDto toDto(ComponentDetail detail) {
        return switch (detail) {
            case BlankDetail blank -> ComponentDetailDto.forBlank(blank.height(), blank.showLine());
            case TextDetail text -> ComponentDetailDto.forText(text.content());
            case TitleDetail title ->
                    ComponentDetailDto.forTitle(
                            title.title1(), title.title2(), title.subTitle1(), title.subTitle2());
            case ImageDetail image ->
                    ComponentDetailDto.forImage(
                            image.imageType() != null ? image.imageType().name() : null);
            case ProductDetail product ->
                    ComponentDetailDto.forProduct(
                            product.listType() != null ? product.listType().name() : null,
                            product.orderType() != null ? product.orderType().name() : null,
                            product.badgeType() != null ? product.badgeType().name() : null,
                            product.showFilter());
            case CategoryDetail category ->
                    ComponentDetailDto.forCategory(
                            category.categoryId().value(),
                            category.listType() != null ? category.listType().name() : null,
                            category.orderType() != null ? category.orderType().name() : null,
                            category.badgeType() != null ? category.badgeType().name() : null,
                            category.showFilter());
            case BrandDetail brand -> ComponentDetailDto.forBrand();
            case TabDetail tab ->
                    ComponentDetailDto.forTab(
                            tab.stickyYn(),
                            tab.tabMovingType() != null ? tab.tabMovingType().name() : null);
        };
    }

    /** DTO -> ComponentDetail 변환 */
    private ComponentDetail fromDto(ComponentType type, ComponentDetailDto dto) {
        return switch (type) {
            case BLANK ->
                    BlankDetail.of(
                            dto.height() != null ? dto.height() : 0,
                            dto.showLine() != null ? dto.showLine() : false);
            case TEXT -> TextDetail.of(dto.content());
            case TITLE ->
                    TitleDetail.of(dto.title1(), dto.title2(), dto.subTitle1(), dto.subTitle2());
            case IMAGE ->
                    ImageDetail.of(
                            dto.imageType() != null ? ImageType.valueOf(dto.imageType()) : null);
            case PRODUCT ->
                    ProductDetail.of(
                            dto.listType() != null
                                    ? ListType.valueOf(dto.listType())
                                    : ListType.GRID,
                            dto.orderType() != null
                                    ? OrderType.valueOf(dto.orderType())
                                    : OrderType.LATEST,
                            dto.badgeType() != null
                                    ? BadgeType.valueOf(dto.badgeType())
                                    : BadgeType.NONE,
                            dto.showFilter() != null ? dto.showFilter() : false);
            case CATEGORY ->
                    CategoryDetail.of(
                            CategoryId.of(dto.categoryId()),
                            dto.listType() != null
                                    ? ListType.valueOf(dto.listType())
                                    : ListType.GRID,
                            dto.orderType() != null
                                    ? OrderType.valueOf(dto.orderType())
                                    : OrderType.LATEST,
                            dto.badgeType() != null
                                    ? BadgeType.valueOf(dto.badgeType())
                                    : BadgeType.NONE,
                            dto.showFilter() != null ? dto.showFilter() : false);
            case BRAND -> BrandDetail.create();
            case TAB ->
                    TabDetail.of(
                            dto.stickyYn() != null ? dto.stickyYn() : false,
                            dto.tabMovingType() != null
                                    ? TabMovingType.valueOf(dto.tabMovingType())
                                    : null);
        };
    }

    /** 기본 ComponentDetail 생성 */
    private ComponentDetail createDefaultDetail(ComponentType type) {
        return switch (type) {
            case BLANK -> BlankDetail.of(0, false);
            case TEXT -> TextDetail.of("");
            case TITLE -> TitleDetail.of(null, null, null, null);
            case IMAGE -> ImageDetail.of(null);
            case PRODUCT -> ProductDetail.defaultSettings();
            case CATEGORY -> CategoryDetail.withDefaults(CategoryId.of(0L));
            case BRAND -> BrandDetail.create();
            case TAB -> TabDetail.of(false, null);
        };
    }
}
