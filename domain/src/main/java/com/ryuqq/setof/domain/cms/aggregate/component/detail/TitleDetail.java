package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.ComponentType;

/**
 * Title(제목) 컴포넌트 상세 정보
 *
 * @param title1 첫 번째 제목
 * @param title2 두 번째 제목 (nullable)
 * @param subTitle1 첫 번째 부제목 (nullable)
 * @param subTitle2 두 번째 부제목 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record TitleDetail(String title1, String title2, String subTitle1, String subTitle2)
        implements ComponentDetail {

    /** Compact Constructor */
    public TitleDetail {
        if (title1 == null || title1.isBlank()) {
            throw new IllegalArgumentException("첫 번째 제목은 필수입니다");
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param title1 첫 번째 제목
     * @param title2 두 번째 제목
     * @param subTitle1 첫 번째 부제목
     * @param subTitle2 두 번째 부제목
     * @return TitleDetail 인스턴스
     */
    public static TitleDetail of(String title1, String title2, String subTitle1, String subTitle2) {
        return new TitleDetail(title1, title2, subTitle1, subTitle2);
    }

    /**
     * 단일 제목으로 생성
     *
     * @param title1 첫 번째 제목
     * @return TitleDetail 인스턴스
     */
    public static TitleDetail ofSingle(String title1) {
        return new TitleDetail(title1, null, null, null);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TITLE;
    }
}
