package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;

/**
 * UpdateGnbV1ApiRequest - GNB 일괄 등록/수정/삭제 요청 DTO.
 *
 * <p>레거시 UpdateGnb 형식을 그대로 유지합니다.
 *
 * <ul>
 *   <li>toUpdateGnbs에서 gnbId == null → 신규 등록
 *   <li>toUpdateGnbs에서 gnbId != null → 수정
 *   <li>deleteGnbIds → 삭제
 * </ul>
 *
 * @param toUpdateGnbs 등록/수정 대상 GNB 목록 (nullable)
 * @param deleteGnbIds 삭제 대상 GNB ID 목록 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "GNB 일괄 등록/수정/삭제 요청")
public record UpdateGnbV1ApiRequest(
        @Schema(description = "등록/수정 대상 GNB 목록 (gnbId null이면 등록, 있으면 수정)", nullable = true) @Valid
                List<CreateGnbV1ApiRequest> toUpdateGnbs,
        @Schema(description = "삭제 대상 GNB ID 목록", nullable = true, example = "[3, 4]")
                List<Long> deleteGnbIds) {}
