package com.ryuqq.setof.application.qna.dto.command;

/**
 * Create QnA Reply Command
 *
 * <p>문의 답변 생성 요청 데이터를 담는 순수한 불변 객체
 *
 * @param qnaId 문의 ID
 * @param parentReplyId 부모 답변 ID (루트 답변인 경우 null)
 * @param writerId 작성자 ID (UUID 문자열)
 * @param writerType 작성자 유형 (SELLER, CUSTOMER, ADMIN)
 * @param writerName 작성자 이름
 * @param content 답변 내용
 * @author development-team
 * @since 1.0.0
 */
public record CreateQnaReplyCommand(
        Long qnaId,
        Long parentReplyId,
        String writerId,
        String writerType,
        String writerName,
        String content) {}
