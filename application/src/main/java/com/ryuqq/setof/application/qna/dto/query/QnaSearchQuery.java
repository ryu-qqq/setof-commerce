package com.ryuqq.setof.application.qna.dto.query;

/**
 * QnA Search Query
 *
 * <p>문의 목록 조회 조건을 담는 순수한 불변 객체
 *
 * @param qnaType 문의 유형 (PRODUCT, ORDER, nullable)
 * @param targetId 대상 ID (상품 그룹 ID 또는 주문 ID, nullable)
 * @param status 상태 (OPEN, CLOSED, nullable)
 * @param writerName 작성자명 (LIKE 검색, nullable)
 * @param sortBy 정렬 기준 (ID, CREATED_AT, UPDATED_AT, nullable)
 * @param sortDirection 정렬 방향 (ASC, DESC, nullable)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record QnaSearchQuery(
        String qnaType,
        Long targetId,
        String status,
        String writerName,
        String sortBy,
        String sortDirection,
        int page,
        int size) {

    public int offset() {
        return page * size;
    }
}
