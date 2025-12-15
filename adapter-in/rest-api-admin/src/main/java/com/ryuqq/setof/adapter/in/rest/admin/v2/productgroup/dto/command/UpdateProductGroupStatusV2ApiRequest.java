package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 상품그룹 상태 변경 요청
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 상태 변경 요청")
public record UpdateProductGroupStatusV2ApiRequest(
        @Schema(
                        description = "변경할 상태 (ACTIVE, INACTIVE, DELETED)",
                        example = "ACTIVE",
                        allowableValues = {"ACTIVE", "INACTIVE", "DELETED"})
                @NotBlank(message = "상태는 필수입니다")
                String status) {}
