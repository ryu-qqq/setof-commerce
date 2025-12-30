package com.ryuqq.setof.application.component.dto.command;

import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.time.Instant;

/**
 * Component 생성 Command
 *
 * @param contentId 소속 Content ID
 * @param componentType 컴포넌트 타입 (BLANK, TEXT, TITLE, IMAGE, PRODUCT, CATEGORY, BRAND, TAB)
 * @param componentName 컴포넌트 이름 (nullable)
 * @param displayOrder 노출 순서
 * @param exposedProducts 노출 상품 수 (기본 0)
 * @param displayStartDate 노출 시작일시 (nullable)
 * @param displayEndDate 노출 종료일시 (nullable)
 * @param detail 타입별 상세 정보 (JSON 형태로 전달)
 * @author development-team
 * @since 1.0.0
 */
public record CreateComponentCommand(
        ContentId contentId,
        String componentType,
        String componentName,
        Integer displayOrder,
        Integer exposedProducts,
        Instant displayStartDate,
        Instant displayEndDate,
        ComponentDetailCommand detail) {

    /** 기본값 적용 */
    public CreateComponentCommand {
        if (displayOrder == null) {
            displayOrder = 0;
        }
        if (exposedProducts == null) {
            exposedProducts = 0;
        }
    }

    /**
     * 노출 기간이 설정되었는지 확인
     *
     * @return 노출 기간이 설정되면 true
     */
    public boolean hasDisplayPeriod() {
        return displayStartDate != null && displayEndDate != null;
    }
}
