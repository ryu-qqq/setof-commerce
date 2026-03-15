package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * CreateGnbV1ApiRequest - GNB 개별 등록/수정 요청 DTO.
 *
 * <p>gnbId가 null이면 신규 등록, 값이 있으면 수정으로 처리합니다.
 *
 * @param gnbId GNB ID (null이면 신규 등록)
 * @param gnbDetails GNB 상세 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "GNB 개별 등록/수정 요청")
public record CreateGnbV1ApiRequest(
        @Schema(description = "GNB ID (null이면 신규 등록)", nullable = true, example = "2")
                @JsonInclude(JsonInclude.Include.NON_NULL)
                Long gnbId,
        @Schema(description = "GNB 상세 정보") @NotNull(message = "GNB 상세 정보는 필수입니다.") @Valid
                GnbDetailsV1ApiRequest gnbDetails) {}
