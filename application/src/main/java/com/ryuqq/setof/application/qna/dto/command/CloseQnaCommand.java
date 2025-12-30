package com.ryuqq.setof.application.qna.dto.command;

/**
 * Close QnA Command
 *
 * <p>문의 종료 요청 데이터를 담는 순수한 불변 객체
 *
 * @param qnaId 문의 ID
 * @author development-team
 * @since 1.0.0
 */
public record CloseQnaCommand(Long qnaId) {}
