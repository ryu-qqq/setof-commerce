package com.ryuqq.setof.domain.qna.vo;

/**
 * QnaUpdateData - Q&A 수정 데이터 VO.
 *
 * <p>Q&A 질문 수정 시 변경 가능한 필드를 캡슐화합니다.
 *
 * @param title 수정할 제목
 * @param content 수정할 내용
 * @param secret 비밀글 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaUpdateData(QnaTitle title, QnaContent content, boolean secret) {

    public static QnaUpdateData of(QnaTitle title, QnaContent content, boolean secret) {
        return new QnaUpdateData(title, content, secret);
    }
}
