package com.ryuqq.setof.domain.cms.vo;

/**
 * 이미지 크기 Value Object
 *
 * <p>너비와 높이 값 (nullable)
 *
 * @param width 너비 (nullable)
 * @param height 높이 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record ImageSize(Double width, Double height) {

    /** Compact Constructor */
    public ImageSize {
        if (width != null && width < 0) {
            throw new IllegalArgumentException("너비는 0 이상이어야 합니다: " + width);
        }
        if (height != null && height < 0) {
            throw new IllegalArgumentException("높이는 0 이상이어야 합니다: " + height);
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param width 너비
     * @param height 높이
     * @return ImageSize 인스턴스
     */
    public static ImageSize of(Double width, Double height) {
        return new ImageSize(width, height);
    }

    /**
     * 빈 크기 생성
     *
     * @return null 값을 가진 ImageSize
     */
    public static ImageSize empty() {
        return new ImageSize(null, null);
    }

    /**
     * 크기 정보 존재 여부 확인
     *
     * @return 크기 정보가 존재하면 true
     */
    public boolean hasValue() {
        return width != null || height != null;
    }
}
