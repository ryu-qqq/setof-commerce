package com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 택배사 수정 요청 DTO
 *
 * @param name 택배사명 (2~50자)
 * @param trackingUrlTemplate 배송 조회 URL 템플릿 (nullable)
 * @param displayOrder 표시 순서 (nullable)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "택배사 수정 요청")
public record UpdateCarrierV2ApiRequest(
        @Schema(description = "택배사명", example = "CJ대한통운")
                @NotBlank(message = "택배사명은 필수입니다")
                @Size(min = 2, max = 50, message = "택배사명은 2~50자 이내입니다")
                String name,
        @Schema(
                        description = "배송 조회 URL 템플릿 ({invoiceNumber} 플레이스홀더 사용)",
                        example = "https://trace.cjlogistics.com/next/{invoiceNumber}")
                String trackingUrlTemplate,
        @Schema(description = "표시 순서 (낮을수록 먼저 표시)", example = "1") Integer displayOrder) {}
