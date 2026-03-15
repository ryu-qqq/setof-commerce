package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 배너 슬라이드 일괄 수정 API 요청.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: Validation 어노테이션은 API Request에만 적용.
 *
 * <p>slideId가 null이면 신규 생성, 값이 있으면 기존 수정, 요청에 미포함이면 삭제로 처리됩니다.
 *
 * @param slides 슬라이드 수정 요청 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 슬라이드 일괄 수정 요청")
public record UpdateBannerSlidesApiRequest(
        @Schema(description = "슬라이드 수정 요청 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "슬라이드 목록은 필수입니다")
                @Valid
                List<UpdateBannerSlideApiRequest> slides) {}
