package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;

/**
 * QnA Reply Response
 *
 * <p>문의 답변 응답 DTO
 *
 * @param id 답변 ID
 * @param qnaId 문의 ID
 * @param parentReplyId 부모 답변 ID
 * @param writerId 작성자 ID
 * @param writerType 작성자 유형
 * @param writerName 작성자 이름
 * @param content 내용
 * @param path 답변 경로 (Materialized Path)
 * @param depth 답변 깊이
 * @param createdAt 생성일시
 * @author development-team
 * @since 1.0.0
 */
public record QnaReplyResponse(
        Long id,
        Long qnaId,
        Long parentReplyId,
        String writerId,
        String writerType,
        String writerName,
        String content,
        String path,
        int depth,
        LocalDateTime createdAt) {

    public static QnaReplyResponse of(
            Long id,
            Long qnaId,
            Long parentReplyId,
            String writerId,
            String writerType,
            String writerName,
            String content,
            String path,
            int depth,
            LocalDateTime createdAt) {
        return new QnaReplyResponse(
                id,
                qnaId,
                parentReplyId,
                writerId,
                writerType,
                writerName,
                content,
                path,
                depth,
                createdAt);
    }
}
