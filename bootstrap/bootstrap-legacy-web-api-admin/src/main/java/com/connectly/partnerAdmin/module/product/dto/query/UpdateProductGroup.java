package com.connectly.partnerAdmin.module.product.dto.query;

import java.util.List;

import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductGroupDetails;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UpdateProductGroup {

    @Valid
    private CreateDeliveryNotice deliveryNotice;

    @Valid
    private CreateRefundNotice refundNotice;

    @Valid
    private CreateProductNotice productNotice;

    @Valid
    @Size(min = 1, max = 10, message = "Product images must be between 1 and 10.")
    private List<CreateProductImage> productImageList;

    private UpdateProductDescription detailDescription;

    @Valid
    private List<CreateOption> productOptions;

    private ProductGroupDetails productGroupDetails;

    private UpdateStatus updateStatus;


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @AllArgsConstructor
    public static class UpdateStatus{
        private boolean productStatus;
        private boolean noticeStatus;
        private boolean imageStatus;
        private boolean descriptionStatus;
        private boolean stockOptionStatus;
        private boolean deliveryStatus;
        private boolean refundStatus;

    }

}
