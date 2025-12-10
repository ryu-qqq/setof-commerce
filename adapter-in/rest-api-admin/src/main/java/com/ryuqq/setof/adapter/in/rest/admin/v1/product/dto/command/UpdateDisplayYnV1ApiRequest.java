package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 전시 여부 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "전시 여부 수정 요청")
public record UpdateDisplayYnV1ApiRequest(@Schema(description = "전시 여부 (Y/N)", example = "Y",
        requiredMode = Schema.RequiredMode.REQUIRED) String displayYn) {
}
