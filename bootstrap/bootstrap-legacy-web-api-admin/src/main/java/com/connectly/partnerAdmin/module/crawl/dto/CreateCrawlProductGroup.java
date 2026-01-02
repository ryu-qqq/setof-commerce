package com.connectly.partnerAdmin.module.crawl.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.connectly.partnerAdmin.module.product.dto.query.CreateClothesDetail;
import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductStatus;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCrawlProductGroup extends CreateProductGroup {

    private boolean soldOut;

    public CreateCrawlProductGroup(@Length(max = 100, message = "Product group name cannot exceed 100 characters.") String productGroupName, @NotNull(message = "Product seller ID is required.") long sellerId, @NotNull(message = "Option type is required.") OptionType optionType, @NotNull(message = "Management type is required.") ManagementType managementType, @NotNull(message = "Product category ID is required.") long categoryId, @NotNull(message = "Brand ID is required.") long brandId, @Valid CreateProductStatus productStatus, @Valid @NotNull(message = "Price is required.") CreatePrice price, @Valid CreateProductNotice productNotice, @Valid CreateClothesDetail clothesDetailInfo, @Valid CreateDeliveryNotice deliveryNotice, @Valid CreateRefundNotice refundNotice, @Valid @Size(min = 1, max = 10, message = "Product images must be between 1 and 10.") List<CreateProductImage> productImageList, @NotNull(message = "Detail description is required.") String detailDescription, @Size(min = 1, message = "At least one product option is required.") @Valid List<CreateOption> productOptions, boolean soldOut) {
        super(0L, productGroupName, sellerId, optionType, managementType, categoryId, brandId, productStatus, price, productNotice, clothesDetailInfo, deliveryNotice, refundNotice, productImageList, detailDescription, productOptions);
        this.soldOut = soldOut;
    }
}
