package com.ryuqq.setof.batch.legacy.shipment.dto;

/**
 * 배송중인 Shipment 정보
 *
 * @param orderId 주문 ID
 * @param invoiceNo 송장번호
 * @param companyCode 택배사 코드
 */
public record ShipmentInfo(Long orderId, String invoiceNo, String companyCode) {}
