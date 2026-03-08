package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaContent - Q&A 내용 Value Object.
 *
 * <p>질문과 답변 모두에서 사용하는 내용 VO. 비밀글 마스킹 처리를 위한 도메인 로직을 포함합니다.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param value Q&A 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaContent(String value) {

    private static final int MAX_LENGTH = 2000;
    private static final String MASKED_VALUE = "비밀글입니다";

    public QnaContent {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Q&A 내용은 필수입니다");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Q&A 내용은 " + MAX_LENGTH + "자 이하여야 합니다");
        }
    }

    public static QnaContent of(String value) {
        return new QnaContent(value);
    }

    /**
     * 비밀글 마스킹 처리된 내용 반환.
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
