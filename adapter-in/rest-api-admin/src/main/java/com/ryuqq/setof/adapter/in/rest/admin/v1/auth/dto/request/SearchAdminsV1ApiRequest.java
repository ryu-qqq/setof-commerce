package com.ryuqq.setof.adapter.in.rest.admin.v1.auth.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchAdminsV1ApiRequest - 관리자 목록 검색 요청 DTO.
 *
 * <p>레거시 Pageable 기반 변환.
 *
 * <p>GET /api/v1/auth - 관리자 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>Pageable → record 내부 page, size 필드
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "관리자 목록 검색 요청")
public record SearchAdminsV1ApiRequest(
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {}
