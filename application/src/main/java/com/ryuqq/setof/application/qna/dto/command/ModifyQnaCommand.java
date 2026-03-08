package com.ryuqq.setof.application.qna.dto.command;

import java.util.List;

/**
 * ModifyQnaCommand - Q&A 질문 수정 커맨드 DTO.
 *
 * <p>PUT /api/v1/qna/{qnaId} 엔드포인트 대응.
 *
 * @param qnaId 수정 대상 Q&A 레거시 ID
 * @param userId 요청자 사용자 ID (소유자 검증용)
 * @param title 수정할 제목
 * @param content 수정할 내용
 * @param secret 비밀글 여부
 * @param imageUrls 수정할 이미지 URL 목록 (교체 방식)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ModifyQnaCommand(
        Long qnaId,
        Long userId,
        String title,
        String content,
        boolean secret,
        List<String> imageUrls) {

    public ModifyQnaCommand {
        if (imageUrls == null) {
            imageUrls = List.of();
        }
    }

    public static ModifyQnaCommand of(
            Long qnaId,
            Long userId,
            String title,
            String content,
            boolean secret,
            List<String> imageUrls) {
        return new ModifyQnaCommand(qnaId, userId, title, content, secret, imageUrls);
    }
}
