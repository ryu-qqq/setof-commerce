package com.connectly.partnerAdmin.module.product.core;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.product.dto.crawl.CrawlProductDto;
import com.connectly.partnerAdmin.module.product.dto.external.ExternalProductDto;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ClothesDetail;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupInfo {

    private long productGroupId;
    private String productGroupName;

    private long sellerId;
    private String sellerName;

    private long categoryId;

    private OptionType optionType;
    private ManagementType managementType;
    private BaseBrandContext brand;

    @Setter
    private Price price;
    private ClothesDetail clothesDetailInfo;
    private DeliveryNotice deliveryNotice;
    private RefundNotice refundNotice;

    @Setter
    private String productGroupMainImageUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    private String categoryFullName;

    @JsonIgnore
    private String path;

    private ProductStatus productStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    private String insertOperator;
    private String updateOperator;
    private long crawlProductSku;
    private String externalProductUuId;

    private CrawlProductDto crawlProductInfo;
    private Set<ExternalProductDto> externalProductInfos;


    @QueryProjection
    public ProductGroupInfo(long productGroupId, String productGroupName, long sellerId, String sellerName, long categoryId, OptionType optionType, ManagementType managementType, BaseBrandContext brand, Price price, ClothesDetail clothesDetailInfo, DeliveryNotice deliveryNotice, RefundNotice refundNotice, ProductStatus productStatus, LocalDateTime insertDate, LocalDateTime updateDate, String insertOperator, String updateOperator, long crawlProductSku, CrawlProductDto crawlProductInfo, String externalProductUuId, Set<ExternalProductDto> externalProductInfos) {
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.categoryId = categoryId;
        this.optionType = optionType;
        this.managementType = managementType;
        this.brand = brand;
        this.price = price;
        this.clothesDetailInfo = clothesDetailInfo;
        this.deliveryNotice = deliveryNotice;
        this.refundNotice = refundNotice;
        this.productStatus = productStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.insertOperator = insertOperator;
        this.updateOperator = updateOperator;
        this.crawlProductSku = crawlProductSku;
        this.externalProductUuId = externalProductUuId;
        setCrawlProductInfo(crawlProductInfo);
        setExternalProductInfos(externalProductInfos);
    }

    @QueryProjection
    public ProductGroupInfo(long productGroupId, String productGroupName, long sellerId, String sellerName, long categoryId, OptionType optionType, ManagementType managementType, BaseBrandContext brand, Price price, ClothesDetail clothesDetailInfo, DeliveryNotice deliveryNotice, RefundNotice refundNotice, String productGroupMainImageUrl, String path, ProductStatus productStatus, LocalDateTime insertDate, LocalDateTime updateDate, String insertOperator, String updateOperator, long crawlProductSku, CrawlProductDto crawlProductInfo, Set<ExternalProductDto> externalProductInfos) {
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.categoryId = categoryId;
        this.optionType = optionType;
        this.managementType = managementType;
        this.brand = brand;
        this.price = price;
        this.clothesDetailInfo = clothesDetailInfo;
        this.deliveryNotice = deliveryNotice;
        this.refundNotice = refundNotice;
        this.productGroupMainImageUrl = productGroupMainImageUrl;
        this.path = path;
        this.productStatus = productStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.insertOperator = insertOperator;
        this.updateOperator = updateOperator;
        this.crawlProductSku = crawlProductSku;
        setCrawlProductInfo(crawlProductInfo);
        setExternalProductInfos(externalProductInfos);

    }

    private void setCrawlProductInfo(CrawlProductDto crawlProductInfo) {
        if(crawlProductInfo.getSiteName().equals(SiteName.OUR_MALL.getName())){
            this.crawlProductInfo = new CrawlProductDto("", 0L);
        }else{
            this.crawlProductInfo = crawlProductInfo;
        }

    }


    private void setExternalProductInfos(Set<ExternalProductDto> externalProductInfos) {
        this.externalProductInfos = externalProductInfos.stream()
                .filter(externalProductDto -> !externalProductDto.getSiteName().equals(SiteName.OUR_MALL.getName()))
                .collect(Collectors.toSet());
    }

}
