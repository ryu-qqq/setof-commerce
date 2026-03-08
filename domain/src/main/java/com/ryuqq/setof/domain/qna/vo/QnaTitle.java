package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaTitle - Q&A 제목 Value Object.
 *
 * <p>비밀글 마스킹 처리를 위한 도메인 로직을 포함합니다.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param value Q&A 제목
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaTitle(String value) {

    private static final int MAX_LENGTH = 200;
    private static final String MASKED_VALUE = "비밀글입니다";

    public QnaTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Q&A 제목은 필수입니다");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Q&A 제목은 " + MAX_LENGTH + "자 이하여야 합니다");
        }
    }

    public static QnaTitle of(String value) {
        return new QnaTitle(value);
    }

    /**
     * 비밀글 마스킹 처리된 제목 반환.
     *
     * @param isOwner 작성자 본인 여부
     * @param isSeller 판매자 여부
     * @return 본인 또는 판매자면 원본, 아니면 마스킹
     */
    public String displayValue(boolean isOwner, boolean isSeller) {
        if (isOwner || isSeller) {
            return value;
        }
        return MASKED_VALUE;
    }
}
