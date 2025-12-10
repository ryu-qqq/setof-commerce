package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전시 컨텐츠 ID 응답")
public record DisplayV1ApiResponse(
        @Schema(description = "전시 컨텐츠 ID", example = "[1,2]") List<Long> contentIds) {}
