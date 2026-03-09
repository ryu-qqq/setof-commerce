package com.ryuqq.setof.storage.legacy.composite.order.dto;

/**
 * LegacyWebOrderOptionFlatDto - 주문 옵션 Flat Projection DTO.
 *
 * <p>옵션 스냅샷 테이블 JOIN 결과를 받기 위한 flat DTO입니다.
 *
 * <p>PER-DTO-001: Persistence DTO는 Record로 정의.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebOrderOptionFlatDto(
        long orderId,
        long optionGroupId,
        long optionDetailId,
        String optionName,
        String optionValue) {}
