package com.ryuqq.setof.adapter.in.rest.v1.board.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchBoardsV1ApiRequest - 공지사항 목록 조회 요청 DTO.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*ApiRequest).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "공지사항 목록 조회 요청")
public record SearchBoardsV1ApiRequest(
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Schema(description = "페이지 번호", nullable = true)
                @Min(0)
                Integer page,
        @Parameter(description = "페이지 크기", example = "20")
                @Schema(description = "페이지 크기", nullable = true)
                @Min(1)
                @Max(100)
                Integer size) {}
