package com.ryuqq.setof.storage.legacy.composite.order.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebOrderDetailFlatDto - 주문 상세 Flat Projection DTO.
 *
 * <p>다중 JOIN 쿼리의 결과를 1행으로 받기 위한 flat DTO입니다. Projections.constructor()로 생성됩니다.
 *
 * <p>PER-DTO-001: Persistence DTO는 Record로 정의.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebOrderDetailFlatDto(
        // orders
        long orderId,
        long paymentId,
        long productId,
        long sellerId,
        long userId,
        long orderAmount,
        String orderStatus,
        int quantity,
        String reviewYn,
        LocalDateTime insertDate,
        // payment
        String paymentStatus,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        // payment_bill
        String paymentAgencyId,
        long billPaymentAmount,
        long usedMileageAmount,
        String buyerName,
        String buyerEmail,
        String buyerPhoneNumber,
        String cardName,
        String cardNumber,
        // payment_method
        String paymentMethod,
        // shipment
        String deliveryStatus,
        String companyCode,
        String invoiceNo,
        LocalDateTime shipmentInsertDate,
        // order_snapshot_product_group
        long productGroupId,
        String productGroupName,
        long brandId,
        long regularPrice,
        long salePrice,
        long directDiscountPrice,
        // order_snapshot_product_group_image (MAIN)
        String mainImageUrl,
        // brand
        String brandName,
        // payment_snapshot_shipping_address
        String receiverName,
        String addressLine1,
        String addressLine2,
        String receiverPhoneNumber,
        String zipCode,
        String country,
        String deliveryRequest,
        // order_snapshot_product_delivery
        String returnMethodDomestic,
        String returnCourierDomestic,
        Integer returnChargeDomestic,
        String returnExchangeAreaDomestic) {}
