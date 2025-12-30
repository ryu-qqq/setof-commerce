package com.ryuqq.setof.domain.faq.vo;

/**
 * FAQ 카테고리 코드 Value Object
 *
 * <p>FAQ가 속한 카테고리를 나타내는 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record FaqCategoryCode(String value) {

    private static final int MAX_LENGTH = 30;

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException value가 null이거나 비어있을 때
     * @throws IllegalArgumentException 길이 제한 초과 시
     */
    public FaqCategoryCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("FAQ 카테고리 코드는 필수입니다");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("FAQ 카테고리 코드는 %d자 이하로 입력해주세요: %d", MAX_LENGTH, value.length()));
        }
        // 대문자 + 언더스코어 형식으로 정규화
        value = value.toUpperCase();
    }

    /**
     * Static Factory Method
     *
     * @param code 카테고리 코드
     * @return FaqCategoryCode 인스턴스
     */
    public static FaqCategoryCode of(String code) {
        return new FaqCategoryCode(code);
    }
}
