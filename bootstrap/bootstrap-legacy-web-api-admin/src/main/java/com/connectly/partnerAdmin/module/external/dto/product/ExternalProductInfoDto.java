package com.connectly.partnerAdmin.module.external.dto.product;

import com.connectly.partnerAdmin.module.category.core.ExternalCategoryContext;
import com.connectly.partnerAdmin.module.category.enums.CategoryType;
import com.connectly.partnerAdmin.module.category.enums.TargetGroup;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.image.ProductImageDto;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ClothesDetail;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.entity.notice.embedded.NoticeDetail;
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalProductInfoDto {
    private long externalId;
    private long productGroupId;
    private long siteId;
    private String siteName;
    private MappingStatus mappingStatus;

    private String externalIdx;
    private String productGroupName;
    private long sellerId;
    private String sellerName;

    private long brandId;
    private String brandName;
    private String brandMappingId;
    private long categoryId;
    private String categoryPath;
    private String categoryMappingId;
    private String categoryDescription;
    private CategoryType categoryType;
    private TargetGroup targetGroup;

    private OptionType optionType;
    private ManagementType managementType;
    private Price price;
    private ClothesDetail clothesDetailInfo;
    private DeliveryNotice deliveryNotice;
    private RefundNotice refundNotice;
    private NoticeDetail noticeDetail;
    private ProductStatus productStatus;
    private String colorCode;
    private BigDecimal fixedPrice;
    private Set<ProductImageDto> productImages;
    private String detailDescription;

    @Setter
    protected Set<ProductFetchResponse> products;

    @QueryProjection
    public ExternalProductInfoDto(long externalId, long productGroupId, long siteId, MappingStatus mappingStatus, String externalIdx, String productGroupName, long sellerId, String sellerName, long brandId, String brandName, String brandMappingId, long categoryId, String categoryPath, String categoryMappingId, String categoryDescription, CategoryType categoryType, TargetGroup targetGroup, OptionType optionType, ManagementType managementType, Price price, ClothesDetail clothesDetailInfo, DeliveryNotice deliveryNotice, RefundNotice refundNotice, NoticeDetail noticeDetail, ProductStatus productStatus, String colorCode, BigDecimal fixedPrice, Set<ProductImageDto> productImages, String detailDescription) {
        this.externalId = externalId;
        this.productGroupId = productGroupId;
        this.siteId = siteId;
        this.mappingStatus = mappingStatus;
        this.externalIdx = externalIdx;
        this.productGroupName = productGroupName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandMappingId = brandMappingId;
        this.categoryId = categoryId;
        this.categoryPath = categoryPath;
        this.categoryMappingId = categoryMappingId;
        this.categoryDescription = categoryDescription;
        this.categoryType = categoryType;
        this.targetGroup = targetGroup;
        this.optionType = optionType;
        this.managementType = managementType;
        this.price = price;
        this.clothesDetailInfo = clothesDetailInfo;
        this.deliveryNotice = deliveryNotice;
        this.refundNotice = refundNotice;
        this.noticeDetail = noticeDetail;
        this.productStatus = productStatus;
        this.colorCode = colorCode;
        this.fixedPrice = fixedPrice;
        this.productImages= productImages;
        this.siteName = SiteName.of(siteId).getName();
        this.detailDescription = detailDescription;
    }

    public void setCategoryMapping(ExternalCategoryContext externalCategoryContext){
        this.categoryMappingId = externalCategoryContext.getMappingCategoryId();
        this.categoryDescription = externalCategoryContext.getMappingCategoryDescription();

    }
}
