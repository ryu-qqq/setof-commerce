package com.ryuqq.setof.domain.productimage.vo;

/**
 * 이미지 타입 Enum
 *
 * <p>상품 이미지의 용도를 구분합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - Enum 특성
 * </ul>
 */
public enum ImageType {

    /**
     * 메인 이미지
     *
     * <p>상품 목록과 상세 페이지 상단에 표시되는 대표 이미지
     */
    MAIN("메인"),

    /**
     * 서브 이미지
     *
     * <p>상품 상세 페이지에서 추가로 표시되는 보조 이미지
     */
    SUB("서브"),

    /**
     * 상세 이미지
     *
     * <p>상품 상세 설명 영역에 표시되는 이미지
     */
    DETAIL("상세");

    private final String description;

    ImageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 메인 이미지 여부 확인
     *
     * @return MAIN 타입이면 true
     */
    public boolean isMain() {
        return this == MAIN;
    }

    /**
     * 서브 이미지 여부 확인
     *
     * @return SUB 타입이면 true
     */
    public boolean isSub() {
        return this == SUB;
    }

    /**
     * 상세 이미지 여부 확인
     *
     * @return DETAIL 타입이면 true
     */
    public boolean isDetail() {
        return this == DETAIL;
    }

    /**
     * 목록 표시용 이미지 여부 확인
     *
     * @return MAIN 또는 SUB 타입이면 true
     */
    public boolean isListDisplayable() {
        return this == MAIN || this == SUB;
    }

    /**
     * 문자열로부터 ImageType을 반환
     *
     * @param value 이미지 타입 문자열
     * @return ImageType, 매칭되지 않으면 null
     */
    public static ImageType fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return ImageType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
