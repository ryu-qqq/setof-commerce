package com.connectly.partnerAdmin.module.external.service.order.lf;

import java.util.List;

public record LfOrderRequestResponseDto(
    List<Body> body
) {

    public record Body(
        LfOrderRequestResponseDto.Order order
    ) {}

    public record Order(

        Integer orderNo,

        Integer ordDtlNo,

        String orderDateTime,

        String payDateTime,

        String supplyEntrCode,

        String supplyEntrName,

        int supplyEntrDeliveryFee,

        String ordererId,

        String ordererName,

        String ordererPhone,

        String ordererCellPhone,

        String receiverName,

        String receiverZipCode,

        String receiverAddr1,

        String receiverAddr2,

        String receiverPhone,

        String receiverCellPhone,

        String deliveryMemo,

        String productCode,

        String supplyProdCode,

        String itemCode,

        String itemName,

        String brandCode,

        String brandName,

        String colorCode,

        String optionCode,

        String sizeCode,

        Integer itemQty,

        Integer orderQty,

        Integer wholeOrderQty,

        Integer orderAmt,

        String realOrderAmt,

        String deliveryEntrCode,

        String deliveryEntrName,

        String orderStatusCode,

        String ordDtlStatusCode,

        String deliveryLocCode,

        String deliveryLocName,

        String optionValue,

        String invoicNo,

        String ordSite
    )  {}

}
