package com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Content 검색 요청 DTO
 *
 * @param title 제목 검색어 (nullable)
 * @param status 상태 필터 (nullable)
 * @param displayableAt 특정 시점에 노출 가능한 것만 (nullable)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "콘텐츠 검색 요청")
public record SearchContentV2ApiRequest(
        @Schema(description = "제목 검색어", example = "메인") String title,
        @Schema(description = "상태 필터", example = "ACTIVE") String status,
        @Schema(description = "특정 시점에 노출 가능한 것만", example = "2024-12-15T12:00:00Z")
                Instant displayableAt,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0") Integer page,
        @Schema(description = "페이지 크기", example = "20") Integer size) {

    /** 기본값 적용 */
    public int getPage() {
        return page != null ? page : 0;
    }

    /** 기본값 적용 */
    public int getSize() {
        return size != null ? size : 20;
    }
}
