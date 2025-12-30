package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Create QnA Reply V2 API Request
 *
 * <p>문의 답변 생성 API 요청 DTO
 *
 * @param parentReplyId 부모 답변 ID (루트 답변인 경우 null)
 * @param content 답변 내용
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "문의 답변 생성 요청")
public record CreateQnaReplyV2ApiRequest(
        @Schema(description = "부모 답변 ID (대댓글인 경우)", example = "null") Long parentReplyId,
        @Schema(description = "답변 내용", example = "해당 상품의 M 사이즈 실측은...")
                @NotBlank(message = "답변 내용은 필수입니다.")
                @Size(max = 4000, message = "답변 내용은 4000자를 초과할 수 없습니다.")
                String content) {}
