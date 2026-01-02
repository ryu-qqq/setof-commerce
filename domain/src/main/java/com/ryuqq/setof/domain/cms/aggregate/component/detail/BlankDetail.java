package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.ComponentType;

/**
 * Blank(빈 공간) 컴포넌트 상세 정보
 *
 * @param height 높이 값
 * @param showLine 구분선 표시 여부
 * @author development-team
 * @since 1.0.0
 */
public record BlankDetail(double height, boolean showLine) implements ComponentDetail {

    /** Compact Constructor */
    public BlankDetail {
        if (height < 0) {
            throw new IllegalArgumentException("높이는 0 이상이어야 합니다: " + height);
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param height 높이 값
     * @param showLine 구분선 표시 여부
     * @return BlankDetail 인스턴스
     */
    public static BlankDetail of(double height, boolean showLine) {
        return new BlankDetail(height, showLine);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.BLANK;
    }
}
