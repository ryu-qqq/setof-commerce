package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.brand.BrandComponentDetail;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.brand.BrandComponentQueryDto;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandComponentFactoryDto implements ComponentFactoryMakeDto {

    private ComponentQueryDto componentQueryDto;
    private List<BrandComponentQueryDto> queries;
    private List<ProductGroupThumbnail> productGroupThumbnails;

    public BrandComponentFactoryDto(
            ComponentQueryDto componentQueryDto,
            List<BrandComponentQueryDto> queries,
            List<ProductGroupThumbnail> productGroupThumbnails) {
        this.componentQueryDto = componentQueryDto;
        this.queries = queries;
        this.productGroupThumbnails = productGroupThumbnails;
    }

    @Override
    public BrandComponentDetail toComponentDetail() {
        return BrandComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .brandComponentId(queries.get(0).getBrandComponentId())
                .brandList(getBrands())
                .categoryId(queries.get(0).getCategoryId())
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
        if (queries.size() > 0) return queries.get(0).getBrandComponentId();
        else return 0L;
    }

    @Override
    public List<Long> getTargetIds() {
        return queries.stream()
                .map(BrandComponentQueryDto::getBrand)
                .map(BrandDto::getBrandId)
                .collect(Collectors.toList());
    }

    public void setProductGroupThumbnails(List<ProductGroupThumbnail> productGroupThumbnails) {
        this.productGroupThumbnails = productGroupThumbnails;
    }

    public List<BrandDto> getBrands() {
        return queries.stream().map(BrandComponentQueryDto::getBrand).collect(Collectors.toList());
    }
}
