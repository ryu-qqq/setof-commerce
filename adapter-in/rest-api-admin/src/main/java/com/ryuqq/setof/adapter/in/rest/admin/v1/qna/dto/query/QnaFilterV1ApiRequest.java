package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 QNA 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "QNA 필터")
public record QnaFilterV1ApiRequest(@Schema(description = "QNA ID", example = "1") Long qnaId,
        @Schema(description = "QNA 상태", example = "PENDING") String qnaStatus,
        @Schema(description = "QNA 타입", example = "PRODUCT",
                requiredMode = Schema.RequiredMode.REQUIRED) String qnaType,
        @Schema(description = "QNA 상세 타입", example = "ORDER") String qnaDetailType,
        @Schema(description = "비공개 여부 (Y/N)", example = "N") String privateYn,
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "검색어", example = "문의") String searchKeyword,
        @Schema(description = "시작 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
        @Schema(description = "종료 일시", example = "2024-12-31 23:59:59") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
}
