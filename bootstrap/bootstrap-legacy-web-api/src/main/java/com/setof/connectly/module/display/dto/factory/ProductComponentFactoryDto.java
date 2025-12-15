package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.product.ProductComponentDetail;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.product.ProductComponentQueryDto;
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
public class ProductComponentFactoryDto implements ComponentFactoryMakeDto {

    private ComponentQueryDto componentQueryDto;
    private List<ProductComponentQueryDto> queries;
    private List<ProductGroupThumbnail> productGroupThumbnails;

    public ProductComponentFactoryDto(
            ComponentQueryDto componentQueryDto,
            List<ProductComponentQueryDto> queries,
            List<ProductGroupThumbnail> productGroupThumbnails) {
        this.componentQueryDto = componentQueryDto;
        this.queries = queries;
        this.productGroupThumbnails = productGroupThumbnails;
    }

    @Override
    public ProductComponentDetail toComponentDetail() {
        return ProductComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .productComponentId(queries.get(0).getProductComponentId())
                .productGroupThumbnails(productGroupThumbnails)
                .componentDetails(componentQueryDto.getComponentDetails())
                .exposedProducts(componentQueryDto.getExposedProducts())
                .viewExtensionId(componentQueryDto.getViewExtensionId())
                .viewExtensionDetails(componentQueryDto.getViewExtensionDetails())
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
        if (queries.size() > 0) return queries.get(0).getComponentId();
        else return 0L;
    }

    @Override
    public Long getTargetId() {
        if (queries.size() > 0) return queries.get(0).getProductComponentId();
        else return 0L;
    }

    @Override
    public List<Long> getTargetIds() {
        return queries.stream()
                .map(ProductComponentQueryDto::getProductComponentId)
                .collect(Collectors.toList());
    }

    public void setProductGroupThumbnails(List<ProductGroupThumbnail> productGroupThumbnails) {
        this.productGroupThumbnails = productGroupThumbnails;
    }
}
