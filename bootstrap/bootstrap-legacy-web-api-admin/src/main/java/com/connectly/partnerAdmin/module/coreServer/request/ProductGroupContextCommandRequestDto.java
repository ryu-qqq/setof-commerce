package com.connectly.partnerAdmin.module.coreServer.request;

import java.util.List;

public record ProductGroupContextCommandRequestDto(
    ProductGroupInsertRequestDto productGroup,

    ProductNoticeInsertRequestDto productNotice,

    ProductDeliveryRequestDto productDelivery,

    List<ProductGroupImageRequestDto> productImageList,

    String detailDescription,

    List<ProductInsertRequestDto> productOptions
) {
}
