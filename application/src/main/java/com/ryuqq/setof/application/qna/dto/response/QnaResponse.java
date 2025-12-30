package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * QnA Response
 *
 * <p>문의 상세 응답 DTO
 *
 * @param id 문의 ID
 * @param type 문의 유형
 * @param detailType 문의 세부 유형
 * @param targetId 대상 ID
 * @param writerId 작성자 ID
 * @param writerType 작성자 유형
 * @param writerName 작성자 이름
 * @param title 제목
 * @param content 내용
 * @param isSecret 비밀글 여부
 * @param status 상태
 * @param replyCount 답변 수
 * @param images 이미지 목록
 * @param createdAt 생성일시
 * @author development-team
 * @since 1.0.0
 */
public record QnaResponse(
        Long id,
        String type,
        String detailType,
        Long targetId,
        String writerId,
        String writerType,
        String writerName,
        String title,
        String content,
        boolean isSecret,
        String status,
        int replyCount,
        List<QnaImageResponse> images,
        LocalDateTime createdAt) {

    public static QnaResponse of(
            Long id,
            String type,
            String detailType,
            Long targetId,
            String writerId,
            String writerType,
            String writerName,
            String title,
            String content,
            boolean isSecret,
            String status,
            int replyCount,
            List<QnaImageResponse> images,
            LocalDateTime createdAt) {
        return new QnaResponse(
                id,
                type,
                detailType,
                targetId,
                writerId,
                writerType,
                writerName,
                title,
                content,
                isSecret,
                status,
                replyCount,
                images,
                createdAt);
    }
}
