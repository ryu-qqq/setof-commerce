package com.ryuqq.setof.application.brand.dto.query;

/**
 * 브랜드 검색 조건 DTO
 *
 * <p>브랜드 목록 조회 시 사용되는 검색 조건입니다.
 *
 * @param keyword 검색 키워드 (한글/영문 브랜드명 검색)
 * @param status 브랜드 상태 (ACTIVE, INACTIVE, null=전체)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 */
public record BrandSearchQuery(String keyword, String status, int page, int size) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    /** Compact Constructor - 검증 및 기본값 설정 */
    public BrandSearchQuery {
        page = Math.max(page, DEFAULT_PAGE);
        size = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
    }

    /**
     * Static Factory Method - 전체 조건
     *
     * @param keyword 검색 키워드
     * @param status 브랜드 상태
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return BrandSearchQuery 인스턴스
     */
    public static BrandSearchQuery of(String keyword, String status, int page, int size) {
        return new BrandSearchQuery(keyword, status, page, size);
    }

    /**
     * Static Factory Method - 키워드 검색
     *
     * @param keyword 검색 키워드
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return BrandSearchQuery 인스턴스
     */
    public static BrandSearchQuery ofKeyword(String keyword, int page, int size) {
        return new BrandSearchQuery(keyword, null, page, size);
    }

    /**
     * Static Factory Method - 활성 브랜드만 조회
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return BrandSearchQuery 인스턴스
     */
    public static BrandSearchQuery ofActive(int page, int size) {
        return new BrandSearchQuery(null, "ACTIVE", page, size);
    }

    /**
     * 키워드 존재 여부 확인
     *
     * @return 키워드가 존재하면 true
     */
    public boolean hasKeyword() {
        return keyword != null && !keyword.isBlank();
    }

    /**
     * 상태 필터 존재 여부 확인
     *
     * @return 상태 필터가 존재하면 true
     */
    public boolean hasStatus() {
        return status != null && !status.isBlank();
    }

    /**
     * offset 계산
     *
     * @return offset 값
     */
    public long offset() {
        return (long) page * size;
    }
}
