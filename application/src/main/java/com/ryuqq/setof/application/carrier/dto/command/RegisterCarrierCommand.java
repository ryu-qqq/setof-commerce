package com.ryuqq.setof.application.carrier.dto.command;

/**
 * 택배사 등록 Command DTO
 *
 * @param code 택배사 코드 (스마트택배 API 기준)
 * @param name 택배사명
 * @param trackingUrlTemplate 배송 조회 URL 템플릿 (nullable)
 * @param displayOrder 표시 순서 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record RegisterCarrierCommand(
        String code, String name, String trackingUrlTemplate, Integer displayOrder) {}
