package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;

/**
 * QnA Summary Response
 *
 * <p>문의 목록 응답 DTO
 *
 * @param id 문의 ID
 * @param type 문의 유형
 * @param detailType 문의 세부 유형
 * @param writerName 작성자 이름
 * @param title 제목
 * @param isSecret 비밀글 여부
 * @param status 상태
 * @param replyCount 답변 수
 * @param createdAt 생성일시
 * @author development-team
 * @since 1.0.0
 */
public record QnaSummaryResponse(
        Long id,
        String type,
        String detailType,
        String writerName,
        String title,
        boolean isSecret,
        String status,
        int replyCount,
        LocalDateTime createdAt) {

    public static QnaSummaryResponse of(
            Long id,
            String type,
            String detailType,
            String writerName,
            String title,
            boolean isSecret,
            String status,
            int replyCount,
            LocalDateTime createdAt) {
        return new QnaSummaryResponse(
                id, type, detailType, writerName, title, isSecret, status, replyCount, createdAt);
    }
}
