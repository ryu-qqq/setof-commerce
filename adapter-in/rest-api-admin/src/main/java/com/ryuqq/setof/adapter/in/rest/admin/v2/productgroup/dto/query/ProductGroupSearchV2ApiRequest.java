package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * 상품그룹 검색 조건 요청
 *
 * <p>컨벤션: Query DTO는 @ModelAttribute로 바인딩, Nested Record 패턴 사용
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 검색 조건")
public record ProductGroupSearchV2ApiRequest(
        @Schema(description = "필터 조건") @Valid FilterRequest filter,
        @Schema(description = "정렬 조건") @Valid SortRequest sort,
        @Schema(description = "페이징 조건") @Valid PageRequest page) {

    /** 기본 페이지 크기 */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** Compact Constructor - null 처리 */
    public ProductGroupSearchV2ApiRequest {
        if (filter == null) {
            filter = FilterRequest.empty();
        }
        if (sort == null) {
            sort = SortRequest.defaultSort();
        }
        if (page == null) {
            page = PageRequest.defaultPage();
        }
    }

    /** 필터 조건 Nested Record */
    @Schema(description = "검색 필터 조건")
    public record FilterRequest(
            @Schema(description = "셀러 ID", example = "1") Long sellerId,
            @Schema(description = "카테고리 ID", example = "100") Long categoryId,
            @Schema(description = "브랜드 ID", example = "10") Long brandId,
            @Schema(description = "상품그룹명 (LIKE 검색)", example = "티셔츠") String name,
            @Schema(
                            description = "상태 (ACTIVE, INACTIVE)",
                            example = "ACTIVE",
                            allowableValues = {"ACTIVE", "INACTIVE"})
                    String status,
            @Schema(
                            description = "날짜 기준 타입 (CREATED_AT, UPDATED_AT)",
                            example = "CREATED_AT",
                            allowableValues = {"CREATED_AT", "UPDATED_AT"})
                    String dateType,
            @Schema(description = "시작일 (yyyy-MM-dd)", example = "2024-01-01") LocalDate startDate,
            @Schema(description = "종료일 (yyyy-MM-dd)", example = "2024-12-31") LocalDate endDate) {

        /** Compact Constructor - 기본값 처리 */
        public FilterRequest {}

        /**
         * 빈 필터 생성
         *
         * @return 빈 필터
         */
        public static FilterRequest empty() {
            return new FilterRequest(null, null, null, null, null, null, null, null);
        }

        /**
         * 날짜 범위 존재 여부 확인
         *
         * @return 시작일 또는 종료일이 존재하면 true
         */
        public boolean hasDateRange() {
            return startDate != null || endDate != null;
        }
    }

    /** 정렬 조건 Nested Record */
    @Schema(description = "정렬 조건")
    public record SortRequest(
            @Schema(
                            description = "정렬 필드 (ID, PRICE, CREATED_AT, UPDATED_AT, NAME)",
                            example = "CREATED_AT",
                            allowableValues = {"ID", "PRICE", "CREATED_AT", "UPDATED_AT", "NAME"})
                    String field,
            @Schema(
                            description = "정렬 방향 (ASC, DESC)",
                            example = "DESC",
                            allowableValues = {"ASC", "DESC"})
                    @Pattern(regexp = "ASC|DESC|asc|desc", message = "정렬 방향은 ASC 또는 DESC이어야 합니다")
                    String direction) {

        /** Compact Constructor - 기본값 처리 */
        public SortRequest {
            if (field == null || field.isBlank()) {
                field = "CREATED_AT";
            }
            if (direction == null || direction.isBlank()) {
                direction = "DESC";
            } else {
                direction = direction.toUpperCase();
            }
        }

        /**
         * 기본 정렬 조건 생성
         *
         * @return 등록일 내림차순
         */
        public static SortRequest defaultSort() {
            return new SortRequest("CREATED_AT", "DESC");
        }
    }

    /** 페이징 조건 Nested Record */
    @Schema(description = "페이징 조건")
    public record PageRequest(
            @Schema(description = "페이지 번호 (0부터 시작)", example = "0", minimum = "0")
                    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
                    Integer number,
            @Schema(description = "페이지 크기", example = "20", minimum = "1", maximum = "100")
                    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
                    @Max(value = 100, message = "페이지 크기는 100 이하이어야 합니다")
                    Integer size) {

        /** Compact Constructor - 기본값 처리 */
        public PageRequest {
            if (number == null) {
                number = 0;
            }
            if (size == null) {
                size = DEFAULT_PAGE_SIZE;
            }
        }

        /**
         * 기본 페이징 조건 생성
         *
         * @return 첫 페이지, 기본 크기
         */
        public static PageRequest defaultPage() {
            return new PageRequest(0, DEFAULT_PAGE_SIZE);
        }
    }

    // ========== Convenience Methods ==========

    /**
     * 페이지 번호 반환
     *
     * @return 페이지 번호
     */
    public int pageNumber() {
        return page.number();
    }

    /**
     * 페이지 크기 반환
     *
     * @return 페이지 크기
     */
    public int pageSize() {
        return page.size();
    }

    /**
     * 정렬 필드 반환
     *
     * @return 정렬 필드
     */
    public String sortField() {
        return sort.field();
    }

    /**
     * 정렬 방향 반환
     *
     * @return 정렬 방향
     */
    public String sortDirection() {
        return sort.direction();
    }
}
