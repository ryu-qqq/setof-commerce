package com.connectly.partnerAdmin.module.external.dto;

import java.util.List;

public record ProductGroupCommandContextRequestDto(

        ProductGroupInsertRequestDto productGroup,
        ProductNoticeInsertRequestDto productNotice,
        ProductDeliveryRequestDto productDelivery,
        List<ProductGroupImageRequestDto> productImageList,
        ProductGroupDetailDescriptionRequestDto detailDescription,
        List<ProductInsertRequestDto> productOptions
) {
}
