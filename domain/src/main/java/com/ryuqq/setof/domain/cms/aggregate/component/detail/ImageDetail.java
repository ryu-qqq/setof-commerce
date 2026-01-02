package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.ComponentType;
import com.ryuqq.setof.domain.cms.vo.ImageType;

/**
 * Image(이미지) 컴포넌트 상세 정보
 *
 * @param imageType 이미지 표시 타입 (SINGLE, MULTI)
 * @author development-team
 * @since 1.0.0
 */
public record ImageDetail(ImageType imageType) implements ComponentDetail {

    /** Compact Constructor */
    public ImageDetail {
        if (imageType == null) {
            imageType = ImageType.SINGLE;
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param imageType 이미지 타입
     * @return ImageDetail 인스턴스
     */
    public static ImageDetail of(ImageType imageType) {
        return new ImageDetail(imageType);
    }

    /**
     * 단일 이미지 타입으로 생성
     *
     * @return ImageDetail 인스턴스
     */
    public static ImageDetail single() {
        return new ImageDetail(ImageType.SINGLE);
    }

    /**
     * 다중 이미지 타입으로 생성
     *
     * @return ImageDetail 인스턴스
     */
    public static ImageDetail multi() {
        return new ImageDetail(ImageType.MULTI);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.IMAGE;
    }
}
