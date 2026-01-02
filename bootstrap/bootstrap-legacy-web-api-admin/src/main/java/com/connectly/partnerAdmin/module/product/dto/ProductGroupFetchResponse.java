package com.connectly.partnerAdmin.module.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.product.core.ProductGroupInfo;
import com.connectly.partnerAdmin.module.product.dto.image.ProductImageDto;
import com.connectly.partnerAdmin.module.product.dto.notice.ProductNoticeDto;
import com.querydsl.core.annotations.QueryProjection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupFetchResponse extends ProductGroupDetailResponse{

    private ProductNoticeDto productNotices;
    private List<ProductImageDto> productGroupImages;
    private String detailDescription;

    @Setter
    private List<TreeCategoryContext> categories;

    @QueryProjection
    public ProductGroupFetchResponse(ProductGroupInfo productGroup, ProductNoticeDto productNotices, Set<ProductImageDto> productGroupImages, String detailDescription) {
        super(productGroup);
        this.productNotices = productNotices;
        this.productGroupImages = new ArrayList<>(productGroupImages);
        this.detailDescription = detailDescription;
        setProductGroupMainImage();
    }

    private void setProductGroupMainImage(){
        Optional<ProductImageDto> first = this.productGroupImages
                .stream()
                .filter(productImageDto -> productImageDto.getType().isMain())
                .findFirst();

        first.ifPresent(productImageDto ->
                this.getProductGroup().setProductGroupMainImageUrl(productImageDto.getProductImageUrl()));
    }



}
