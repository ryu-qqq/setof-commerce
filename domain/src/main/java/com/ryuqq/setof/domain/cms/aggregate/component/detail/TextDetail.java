package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.ComponentType;

/**
 * Text(텍스트) 컴포넌트 상세 정보
 *
 * @param content 텍스트 내용
 * @author development-team
 * @since 1.0.0
 */
public record TextDetail(String content) implements ComponentDetail {

    /** Compact Constructor */
    public TextDetail {
        if (content == null) {
            content = "";
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param content 텍스트 내용
     * @return TextDetail 인스턴스
     */
    public static TextDetail of(String content) {
        return new TextDetail(content);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TEXT;
    }
}
