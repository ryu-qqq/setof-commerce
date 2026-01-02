package com.connectly.partnerAdmin.module.product.dto.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;

import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateProductImage {

    @NotNull(message = "type is required.")
    @JsonProperty("type")
    private ProductGroupImageType productImageType;

    @Setter
    @NotBlank(message = "Product Image Url is required.")
    @Length(max = 255, message = "Product Image Url cannot exceed 255 characters.")
    @JsonProperty("productImageUrl")
    private String imageUrl;

    @JsonProperty("originUrl")
    @Length(max = 255, message = "Origin Url cannot exceed 255 characters.")
    private String originUrl;

    public CreateProductImage(ProductGroupImageType productImageType, String imageUrl) {
        this.productImageType = productImageType;
        this.imageUrl = imageUrl;
        this.originUrl = imageUrl;
    }

}
