package com.ryuqq.setof.application.shipment.dto.command;

/**
 * 운송장 번호 변경 Command DTO
 *
 * <p>발송 전(PENDING 상태)에만 변경 가능합니다.
 *
 * @param carrierId 새 택배사 ID
 * @param invoiceNumber 새 운송장 번호
 * @author development-team
 * @since 1.0.0
 */
public record ChangeInvoiceCommand(Long carrierId, String invoiceNumber) {}
