package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * RegisterProductNoticeApiRequest - 상품 그룹 고시정보 등록 API Request.
 *
 * <p>API-REQ-001: Record 패턴 사용
 *
 * <p>API-VAL-001: jakarta.validation 사용
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 고시정보 등록 요청")
public record RegisterProductNoticeApiRequest(
        @Schema(description = "고시 항목 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "고시 항목 목록은 필수입니다")
                @Valid
                List<NoticeEntryApiRequest> entries) {

    /**
     * 고시 항목 데이터.
     *
     * @param noticeFieldId 고시 필드 ID
     * @param fieldName 필드명
     * @param fieldValue 필드값
     */
    @Schema(description = "고시 항목 데이터")
    public record NoticeEntryApiRequest(
            @Schema(
                            description = "고시 필드 ID",
                            example = "1",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "고시 필드 ID는 필수입니다")
                    Long noticeFieldId,
            @Schema(
                            description = "필드명",
                            example = "소재",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "필드명은 필수입니다")
                    String fieldName,
            @Schema(
                            description = "필드값",
                            example = "면 100%",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "필드값은 필수입니다")
                    String fieldValue) {}
}
