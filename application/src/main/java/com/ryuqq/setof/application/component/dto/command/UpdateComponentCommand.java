package com.ryuqq.setof.application.component.dto.command;

import com.ryuqq.setof.domain.cms.vo.ComponentId;
import java.time.Instant;

/**
 * Component 수정 Command
 *
 * @param componentId 컴포넌트 ID
 * @param componentName 컴포넌트 이름 (nullable)
 * @param displayOrder 노출 순서
 * @param exposedProducts 노출 상품 수
 * @param displayStartDate 노출 시작일시 (nullable)
 * @param displayEndDate 노출 종료일시 (nullable)
 * @param detail 타입별 상세 정보
 * @author development-team
 * @since 1.0.0
 */
public record UpdateComponentCommand(
        ComponentId componentId,
        String componentName,
        Integer displayOrder,
        Integer exposedProducts,
        Instant displayStartDate,
        Instant displayEndDate,
        ComponentDetailCommand detail) {}
