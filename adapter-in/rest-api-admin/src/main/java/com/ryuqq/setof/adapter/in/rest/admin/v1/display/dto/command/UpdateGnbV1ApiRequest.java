package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 GNB 업데이트 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "GNB 업데이트 요청")
public record UpdateGnbV1ApiRequest(
        @Schema(description = "GNB 아이템 목록") List<GnbItemV1ApiRequest> items) {

    /**
     * GNB 아이템 Request
     */
    @Schema(description = "GNB 아이템")
    public record GnbItemV1ApiRequest(
            @Schema(description = "GNB명", example = "홈") String name,
            @Schema(description = "링크 URL", example = "/") String linkUrl,
            @Schema(description = "정렬 순서", example = "1") Integer sortOrder) {
    }
}
