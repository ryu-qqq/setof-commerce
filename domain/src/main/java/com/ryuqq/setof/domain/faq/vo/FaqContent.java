package com.ryuqq.setof.domain.faq.vo;

/**
 * FAQ 컨텐츠 Value Object
 *
 * <p>질문과 답변을 캡슐화한 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record FaqContent(String question, String answer) {

    private static final int MAX_QUESTION_LENGTH = 500;

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException 질문이 null이거나 비어있을 때
     * @throws IllegalArgumentException 답변이 null일 때
     * @throws IllegalArgumentException 길이 제한 초과 시
     */
    public FaqContent {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("질문은 필수입니다");
        }
        if (question.length() > MAX_QUESTION_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(
                            "질문은 %d자 이하로 입력해주세요: %d", MAX_QUESTION_LENGTH, question.length()));
        }
        if (answer == null) {
            throw new IllegalArgumentException("답변은 필수입니다");
        }
    }

    /**
     * 답변 존재 여부 확인
     *
     * @return 답변이 있으면 true
     */
    public boolean hasAnswer() {
        return answer != null && !answer.isBlank();
    }

    /**
     * 질문 업데이트
     *
     * @param newQuestion 새로운 질문
     * @return 질문이 업데이트된 FaqContent
     */
    public FaqContent updateQuestion(String newQuestion) {
        return new FaqContent(newQuestion, this.answer);
    }

    /**
     * 답변 업데이트
     *
     * @param newAnswer 새로운 답변
     * @return 답변이 업데이트된 FaqContent
     */
    public FaqContent updateAnswer(String newAnswer) {
        return new FaqContent(this.question, newAnswer);
    }
}
