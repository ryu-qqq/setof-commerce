package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Banner 검색 요청 DTO
 *
 * @param bannerType 배너 타입 (nullable)
 * @param status 상태 (nullable)
 * @param displayableAt 노출 가능 시점 (nullable)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "배너 검색 요청")
public record SearchBannerV2ApiRequest(
        @Schema(description = "배너 타입", example = "CATEGORY") String bannerType,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "노출 가능 시점", example = "2024-12-15T12:00:00Z") Instant displayableAt,
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

    /** 오프셋 계산 */
    public int getOffset() {
        return getPage() * getSize();
    }
}
