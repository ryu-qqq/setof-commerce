package com.ryuqq.setof.application.carrier.dto.command;

/**
 * 택배사 수정 Command DTO
 *
 * @param carrierId 택배사 ID
 * @param name 택배사명
 * @param trackingUrlTemplate 배송 조회 URL 템플릿 (nullable)
 * @param displayOrder 표시 순서 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateCarrierCommand(
        Long carrierId, String name, String trackingUrlTemplate, Integer displayOrder) {}
