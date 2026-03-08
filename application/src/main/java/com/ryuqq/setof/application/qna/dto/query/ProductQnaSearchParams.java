package com.ryuqq.setof.application.qna.dto.query;

/**
 * ProductQnaSearchParams - 상품 Q&A 목록 조회 파라미터 DTO.
 *
 * <p>Offset 기반 페이지네이션 파라미터를 포함합니다. GET /api/v1/qna/product/{productGroupId} 엔드포인트 대응.
 *
 * @param productGroupId 상품그룹 ID
 * @param viewerUserId 조회자 사용자 ID (비밀글 마스킹 판단용, null이면 비로그인)
 * @param page 페이지 번호 (0-based)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductQnaSearchParams(
        Long productGroupId, Long viewerUserId, Integer page, Integer size) {

    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_PAGE = 0;

    public ProductQnaSearchParams {
        if (page == null || page < 0) {
            page = DEFAULT_PAGE;
        }
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
    }

    public static ProductQnaSearchParams of(
            Long productGroupId, Long viewerUserId, Integer page, Integer size) {
        return new ProductQnaSearchParams(productGroupId, viewerUserId, page, size);
    }

    public int pageOrDefault() {
        return page != null ? page : DEFAULT_PAGE;
    }

    public int sizeOrDefault() {
        return size != null ? size : DEFAULT_SIZE;
    }

    public long offset() {
        return (long) pageOrDefault() * sizeOrDefault();
    }
}
