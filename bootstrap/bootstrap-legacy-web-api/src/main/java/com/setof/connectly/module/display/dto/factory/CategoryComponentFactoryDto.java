package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.category.CategoryComponentDetail;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.category.CategoryComponentQueryDto;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryComponentFactoryDto implements ComponentFactoryMakeDto {

    private ComponentQueryDto componentQueryDto;
    private List<CategoryComponentQueryDto> queries;
    private List<ProductGroupThumbnail> productGroupThumbnails;

    public CategoryComponentFactoryDto(
            ComponentQueryDto componentQueryDto,
            List<CategoryComponentQueryDto> queries,
            List<ProductGroupThumbnail> productGroupThumbnails) {
        this.componentQueryDto = componentQueryDto;
        this.queries = queries;
        this.productGroupThumbnails = productGroupThumbnails;
    }

    @Override
    public CategoryComponentDetail toComponentDetail() {
        return CategoryComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .categoryComponentId(queries.get(0).getCategoryComponentId())
                .categoryId(getTargetId())
                .componentDetails(componentQueryDto.getComponentDetails())
                .exposedProducts(componentQueryDto.getExposedProducts())
                .viewExtensionId(componentQueryDto.getViewExtensionId())
                .viewExtensionDetails(componentQueryDto.getViewExtensionDetails())
                .productGroupThumbnails(productGroupThumbnails)
                .displayYn(componentQueryDto.getDisplayYn())
                .displayPeriod(componentQueryDto.getDisplayPeriod())
                .componentName(componentQueryDto.getComponentName())
                .displayOrder(componentQueryDto.getDisplayOrder())
                .build();
    }

    @Override
    public long getContentId() {
        return componentQueryDto.getContentId();
    }

    @Override
    public long getComponentId() {
        return componentQueryDto.getComponentId();
    }

    @Override
    public long getSubComponentId() {
        if (queries.size() > 0) return queries.get(0).getCategoryComponentId();
        else return 0L;
    }

    @Override
    public Long getTargetId() {
        if (queries.size() > 0) return queries.get(0).getCategoryId();
        else return 0L;
    }

    @Override
    public List<Long> getTargetIds() {
        return queries.stream()
                .map(CategoryComponentQueryDto::getCategoryId)
                .collect(Collectors.toList());
    }

    public void setProductGroupThumbnails(List<ProductGroupThumbnail> productGroupThumbnails) {
        this.productGroupThumbnails = productGroupThumbnails;
    }
}
