package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * DiscountUseHistorySearchV1ApiRequest - 할인 사용 이력 검색 요청 DTO.
 *
 * <p>레거시 DiscountFilter 기반 변환 (사용 이력 전용 필터).
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Spring Pageable → page, size 필드 내장
 *   <li>SearchAndDateFilter 필드 중 사용 이력에 필요한 것만 포함
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.discount.filter.DiscountFilter
 */
@Schema(description = "할인 사용 이력 검색 요청")
public record DiscountUseHistorySearchV1ApiRequest(
        @Parameter(description = "조회 시작일", example = "2026-01-01 00:00:00")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(description = "조회 종료일", example = "2026-12-31 23:59:59")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Parameter(description = "사용자 ID 필터 (특정 사용자의 이력만 조회)", example = "1001") Long userId,
        @Parameter(description = "검색 키워드 유형", example = "NAME") String searchKeyword,
        @Parameter(description = "검색어", example = "홍길동") String searchWord,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    /**
     * 기본값이 적용된 인스턴스를 반환합니다.
     *
     * @return 기본값이 적용된 DiscountUseHistorySearchV1ApiRequest
     */
    public DiscountUseHistorySearchV1ApiRequest withDefaults() {
        return new DiscountUseHistorySearchV1ApiRequest(
                this.startDate,
                this.endDate,
                this.userId,
                this.searchKeyword,
                this.searchWord,
                this.page != null ? this.page : 0,
                this.size != null ? this.size : 20);
    }

    /**
     * 특정 사용자 필터링 여부를 반환합니다.
     *
     * @return userId가 있으면 true
     */
    public boolean hasUserFilter() {
        return userId != null;
    }

    /**
     * 기간 필터링 여부를 반환합니다.
     *
     * @return startDate와 endDate가 모두 있으면 true
     */
    public boolean hasDateRange() {
        return startDate != null && endDate != null;
    }
}
