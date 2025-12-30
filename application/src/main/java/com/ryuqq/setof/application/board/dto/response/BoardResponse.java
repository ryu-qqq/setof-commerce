package com.ryuqq.setof.application.board.dto.response;

import java.time.Instant;

/**
 * Board 응답 DTO
 *
 * @param id 게시글 ID
 * @param boardType 게시판 타입
 * @param title 제목
 * @param content 내용
 * @param summary 요약
 * @param thumbnailUrl 썸네일 URL
 * @param pinned 상단 고정 여부
 * @param pinOrder 상단 고정 순서
 * @param status 상태
 * @param viewCount 조회수
 * @param displayStartAt 노출 시작일시
 * @param displayEndAt 노출 종료일시
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record BoardResponse(
        Long id,
        String boardType,
        String title,
        String content,
        String summary,
        String thumbnailUrl,
        boolean pinned,
        int pinOrder,
        String status,
        long viewCount,
        Instant displayStartAt,
        Instant displayEndAt,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * Static Factory Method
     *
     * @return BoardResponse 인스턴스
     */
    public static BoardResponse of(
            Long id,
            String boardType,
            String title,
            String content,
            String summary,
            String thumbnailUrl,
            boolean pinned,
            int pinOrder,
            String status,
            long viewCount,
            Instant displayStartAt,
            Instant displayEndAt,
            Instant createdAt,
            Instant updatedAt) {
        return new BoardResponse(
                id,
                boardType,
                title,
                content,
                summary,
                thumbnailUrl,
                pinned,
                pinOrder,
                status,
                viewCount,
                displayStartAt,
                displayEndAt,
                createdAt,
                updatedAt);
    }
}
