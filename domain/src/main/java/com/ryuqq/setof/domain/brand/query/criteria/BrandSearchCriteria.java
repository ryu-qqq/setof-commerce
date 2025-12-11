package com.ryuqq.setof.domain.brand.query.criteria;

import com.ryuqq.setof.domain.common.vo.PageRequest;

/**
 * BrandSearchCriteria - 브랜드 검색 조건 Domain VO
 *
 * <p>브랜드 목록 조회 시 사용되는 검색 조건입니다.
 *
 * <p><strong>사용 위치:</strong>
 *
 * <ul>
 *   <li>Domain Layer에서 정의
 *   <li>Persistence Layer Repository에서 사용
 *   <li>Application Layer에서 생성 (Factory/Service)
 * </ul>
 *
 * <p><strong>의존성 방향:</strong>
 *
 * <pre>{@code
 * Application Layer → Domain Layer ← Persistence Layer
 *                      (Criteria)
 * }</pre>
 *
 * @param keyword 검색 키워드 (한글/영문 브랜드명 검색)
 * @param status 브랜드 상태 (ACTIVE, INACTIVE, null=전체)
 * @param pageRequest 페이징 정보
 * @author development-team
 * @since 1.0.0
 */
public record BrandSearchCriteria(String keyword, String status, PageRequest pageRequest) {

    /**
     * Static Factory Method - 전체 조건
     *
     * @param keyword 검색 키워드
     * @param status 브랜드 상태
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return BrandSearchCriteria 인스턴스
     */
    public static BrandSearchCriteria of(String keyword, String status, int page, int size) {
        return new BrandSearchCriteria(keyword, status, PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 키워드 검색
     *
     * @param keyword 검색 키워드
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return BrandSearchCriteria 인스턴스
     */
    public static BrandSearchCriteria ofKeyword(String keyword, int page, int size) {
        return new BrandSearchCriteria(keyword, null, PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 활성 브랜드만 조회
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return BrandSearchCriteria 인스턴스
     */
    public static BrandSearchCriteria ofActive(int page, int size) {
        return new BrandSearchCriteria(null, "ACTIVE", PageRequest.of(page, size));
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
     * offset 계산 (PageRequest 위임)
     *
     * @return offset 값
     */
    public long offset() {
        return pageRequest.offset();
    }

    /**
     * 페이지 크기 반환 (PageRequest 위임)
     *
     * @return 페이지 크기
     */
    public int size() {
        return pageRequest.size();
    }

    /**
     * 페이지 번호 반환 (PageRequest 위임)
     *
     * @return 페이지 번호
     */
    public int page() {
        return pageRequest.page();
    }
}
