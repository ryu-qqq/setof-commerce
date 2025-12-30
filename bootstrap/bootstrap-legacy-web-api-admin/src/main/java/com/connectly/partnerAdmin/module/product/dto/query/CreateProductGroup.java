package com.connectly.partnerAdmin.module.product.dto.query;

import jakarta.persistence.Lob;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.connectly.partnerAdmin.module.product.annotation.ProductValidate;
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

@ProductValidate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateProductGroup {

    private Long productGroupId;

    @Length(max = 200, message = "Product group name cannot exceed 200 characters.")
    private String productGroupName;

    @NotNull(message = "Product seller ID is required.")
    private long sellerId;

    @NotNull(message = "Option type is required.")
    private OptionType optionType;

    @NotNull(message = "Management type is required.")
    private ManagementType managementType;

    @NotNull(message = "Product category ID is required.")
    private long categoryId;

    @NotNull(message = "Brand ID is required.")
    private long brandId;

    @Valid
    private CreateProductStatus productStatus;

    @Valid
    @NotNull(message = "Price is required.")
    private CreatePrice price;

    @Valid
    private CreateProductNotice productNotice;

    @Valid
    private CreateClothesDetail clothesDetailInfo;

    @Valid
    private CreateDeliveryNotice deliveryNotice;

    @Valid
    private CreateRefundNotice refundNotice;

    @Valid
    @Size(min = 1, max = 10, message = "Product images must be between 1 and 10.")
    private List<CreateProductImage> productImageList;

    @NotNull(message = "Detail description is required.")
    @Lob
    private String detailDescription;

    @Size(min = 1, message = "At least one product option is required.")
    @Valid
    private List<CreateOption> productOptions;

}
