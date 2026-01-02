package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.ComponentType;

/**
 * Brand(브랜드) 컴포넌트 상세 정보
 *
 * <p>현재는 추가 설정이 없는 빈 레코드입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record BrandDetail() implements ComponentDetail {

    /**
     * 정적 팩토리 메서드
     *
     * @return BrandDetail 인스턴스
     */
    public static BrandDetail create() {
        return new BrandDetail();
    }

    @Override
    public ComponentType getType() {
        return ComponentType.BRAND;
    }
}
