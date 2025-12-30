package com.ryuqq.setof.application.qna.dto.command;

/**
 * Update QnA Command
 *
 * <p>문의 수정 요청 데이터를 담는 순수한 불변 객체
 *
 * @param qnaId 수정할 QnA ID
 * @param title 새 제목
 * @param content 새 내용
 * @author development-team
 * @since 1.0.0
 */
public record UpdateQnaCommand(Long qnaId, String title, String content) {}
