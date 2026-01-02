package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.ComponentType;
import com.ryuqq.setof.domain.cms.vo.TabMovingType;

/**
 * Tab(탭) 컴포넌트 상세 정보
 *
 * @param stickyYn 탭 고정 여부
 * @param tabMovingType 탭 이동 타입 (SCROLL, CLICK)
 * @author development-team
 * @since 1.0.0
 */
public record TabDetail(boolean stickyYn, TabMovingType tabMovingType) implements ComponentDetail {

    /** Compact Constructor */
    public TabDetail {
        if (tabMovingType == null) {
            tabMovingType = TabMovingType.SCROLL;
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param stickyYn 고정 여부
     * @param tabMovingType 이동 타입
     * @return TabDetail 인스턴스
     */
    public static TabDetail of(boolean stickyYn, TabMovingType tabMovingType) {
        return new TabDetail(stickyYn, tabMovingType);
    }

    /**
     * 기본 설정으로 생성
     *
     * @return TabDetail 인스턴스
     */
    public static TabDetail defaultSettings() {
        return new TabDetail(false, TabMovingType.SCROLL);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TAB;
    }
}
