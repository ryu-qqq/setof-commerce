package com.ryuqq.setof.application.qna.dto.command;

/**
 * Update QnA Reply Command
 *
 * <p>문의 답변 수정 요청 데이터를 담는 순수한 불변 객체
 *
 * @param qnaId 문의 ID
 * @param replyId 수정할 답변 ID
 * @param content 수정된 답변 내용
 * @author development-team
 * @since 1.0.0
 */
public record UpdateQnaReplyCommand(Long qnaId, Long replyId, String content) {}
