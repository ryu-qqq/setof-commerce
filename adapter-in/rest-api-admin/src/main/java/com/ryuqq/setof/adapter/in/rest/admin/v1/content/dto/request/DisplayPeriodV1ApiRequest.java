package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DisplayPeriodV1ApiRequest - 전시 기간 요청 DTO.
 *
 * <p>레거시 DisplayPeriod 형식을 그대로 유지합니다.
 *
 * @param displayStartDate 전시 시작일시 (yyyy-MM-dd HH:mm:ss)
 * @param displayEndDate 전시 종료일시 (yyyy-MM-dd HH:mm:ss)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "전시 기간 요청")
public record DisplayPeriodV1ApiRequest(
        @Schema(description = "전시 시작일시", example = "2024-01-01 00:00:00")
                @NotNull(message = "전시 시작일시는 필수입니다.")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime displayStartDate,
        @Schema(description = "전시 종료일시", example = "2024-12-31 23:59:59")
                @NotNull(message = "전시 종료일시는 필수입니다.")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime displayEndDate) {}
