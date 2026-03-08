package com.ryuqq.setof.application.qna.dto.query;

import java.time.LocalDate;

/**
 * MyQnaSearchParams - 내 Q&A 목록 조회 파라미터 DTO.
 *
 * <p>Cursor 기반 페이지네이션 파라미터를 포함합니다. GET /api/v1/qna/my-page 엔드포인트 대응.
 *
 * @param userId 사용자 ID (레거시)
 * @param qnaType Q&A 유형 필터 (PRODUCT/ORDER)
 * @param lastQnaId 커서 페이징용 마지막 Q&A ID (null이면 첫 페이지)
 * @param startDate 조회 시작일
 * @param endDate 조회 종료일
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MyQnaSearchParams(
        Long userId,
        String qnaType,
        Long lastQnaId,
        LocalDate startDate,
        LocalDate endDate,
        Integer size) {

    private static final int DEFAULT_SIZE = 10;

    public MyQnaSearchParams {
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
    }

    public static MyQnaSearchParams of(
            Long userId,
            String qnaType,
            Long lastQnaId,
            LocalDate startDate,
            LocalDate endDate,
            Integer size) {
        return new MyQnaSearchParams(userId, qnaType, lastQnaId, startDate, endDate, size);
    }

    public int sizeOrDefault() {
        return size != null ? size : DEFAULT_SIZE;
    }
}
