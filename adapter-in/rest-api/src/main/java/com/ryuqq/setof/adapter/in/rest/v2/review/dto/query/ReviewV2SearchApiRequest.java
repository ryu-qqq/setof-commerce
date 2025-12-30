package com.ryuqq.setof.adapter.in.rest.v2.review.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Review V2 검색 조건 API Request
 *
 * <p>컨벤션: Query DTO는 @ModelAttribute로 바인딩
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "Review V2 검색 조건")
public record ReviewV2SearchApiRequest(@Schema(description = "페이징 조건") @Valid PageRequest page) {

    /** 기본 페이지 크기 */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** Compact Constructor - null 처리 */
    public ReviewV2SearchApiRequest {
        if (page == null) {
            page = PageRequest.defaultPage();
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
}
