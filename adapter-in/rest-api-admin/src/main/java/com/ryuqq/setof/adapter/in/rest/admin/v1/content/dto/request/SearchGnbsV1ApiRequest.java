package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * SearchGnbsV1ApiRequest - GNB 목록 검색 요청 DTO.
 *
 * <p>레거시 GnbFilter 기반 변환.
 *
 * <p>GET /api/v1/content/gnbs - GNB 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>SearchKeyword enum → String 타입
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.display.dto.gnb.filter.GnbFilter
 */
@Schema(description = "GNB 목록 검색 요청")
public record SearchGnbsV1ApiRequest(
        @Parameter(description = "조회 시작일 (yyyy-MM-dd HH:mm:ss)", example = "2024-01-01 00:00:00")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(description = "조회 종료일 (yyyy-MM-dd HH:mm:ss)", example = "2024-12-31 23:59:59")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Parameter(
                        description = "검색 키워드 타입",
                        example = "INSERT_OPERATOR",
                        schema = @Schema(allowableValues = {"INSERT_OPERATOR", "UPDATE_OPERATOR"}))
                String searchKeyword,
        @Parameter(description = "검색어", example = "admin") String searchWord) {}
