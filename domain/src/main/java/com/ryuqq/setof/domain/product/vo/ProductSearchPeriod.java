package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.common.vo.DateRange;
import java.time.Instant;
import java.time.LocalDate;

/**
 * ProductSearchPeriod - 상품 검색 기간 Value Object
 *
 * <p>상품 검색 시 날짜 기준 필드와 기간을 함께 표현합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 등록일 기준 최근 7일
 * ProductSearchPeriod period = ProductSearchPeriod.createdAt(DateRange.lastDays(7));
 *
 * // 수정일 기준 이번 달
 * ProductSearchPeriod period = ProductSearchPeriod.updatedAt(DateRange.thisMonth());
 *
 * // Criteria에서 사용
 * ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.builder()
 *     .searchPeriod(ProductSearchPeriod.createdAt(DateRange.lastDays(30)))
 *     .build();
 * }</pre>
 *
 * @param dateType 날짜 기준 타입 (CREATED_AT, UPDATED_AT)
 * @param dateRange 날짜 범위
 * @author development-team
 * @since 1.0.0
 */
public record ProductSearchPeriod(ProductDateType dateType, DateRange dateRange) {

    /**
     * Static Factory Method
     *
     * @param dateType 날짜 기준 타입
     * @param dateRange 날짜 범위
     * @return ProductSearchPeriod 인스턴스
     */
    public static ProductSearchPeriod of(ProductDateType dateType, DateRange dateRange) {
        return new ProductSearchPeriod(dateType, dateRange);
    }

    /**
     * 등록일 기준 검색 기간 생성
     *
     * @param dateRange 날짜 범위
     * @return ProductSearchPeriod 인스턴스
     */
    public static ProductSearchPeriod createdAt(DateRange dateRange) {
        return new ProductSearchPeriod(ProductDateType.CREATED_AT, dateRange);
    }

    /**
     * 수정일 기준 검색 기간 생성
     *
     * @param dateRange 날짜 범위
     * @return ProductSearchPeriod 인스턴스
     */
    public static ProductSearchPeriod updatedAt(DateRange dateRange) {
        return new ProductSearchPeriod(ProductDateType.UPDATED_AT, dateRange);
    }

    /**
     * 등록일 기준 최근 N일 검색 기간 생성
     *
     * @param days 일수
     * @return ProductSearchPeriod 인스턴스
     */
    public static ProductSearchPeriod createdAtLastDays(int days) {
        return createdAt(DateRange.lastDays(days));
    }

    /**
     * 수정일 기준 최근 N일 검색 기간 생성
     *
     * @param days 일수
     * @return ProductSearchPeriod 인스턴스
     */
    public static ProductSearchPeriod updatedAtLastDays(int days) {
        return updatedAt(DateRange.lastDays(days));
    }

    /**
     * 날짜 범위 존재 여부 확인
     *
     * @return dateRange가 존재하고 비어있지 않으면 true
     */
    public boolean hasDateRange() {
        return dateRange != null && !dateRange.isEmpty();
    }

    /**
     * 등록일 기준인지 확인
     *
     * @return CREATED_AT이면 true
     */
    public boolean isCreatedAt() {
        return dateType == ProductDateType.CREATED_AT;
    }

    /**
     * 수정일 기준인지 확인
     *
     * @return UPDATED_AT이면 true
     */
    public boolean isUpdatedAt() {
        return dateType == ProductDateType.UPDATED_AT;
    }

    /**
     * 시작일 반환
     *
     * @return 시작일 (null 가능)
     */
    public LocalDate startDate() {
        return dateRange != null ? dateRange.startDate() : null;
    }

    /**
     * 종료일 반환
     *
     * @return 종료일 (null 가능)
     */
    public LocalDate endDate() {
        return dateRange != null ? dateRange.endDate() : null;
    }

    /**
     * 시작일을 Instant로 변환
     *
     * @return 시작 Instant (null 가능)
     */
    public Instant startInstant() {
        return dateRange != null ? dateRange.startInstant() : null;
    }

    /**
     * 종료일을 Instant로 변환
     *
     * @return 종료 Instant (null 가능)
     */
    public Instant endInstant() {
        return dateRange != null ? dateRange.endInstant() : null;
    }
}
