package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Create Product QnA V2 API Request
 *
 * <p>상품 문의 생성 API 요청 DTO
 *
 * @param productGroupId 상품 그룹 ID
 * @param writerName 작성자 이름
 * @param title 문의 제목
 * @param content 문의 내용
 * @param detailType 문의 세부 유형 (SIZE, DELIVERY, RESTOCK, OTHER)
 * @param isSecret 비밀글 여부
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품 문의 생성 요청")
public record CreateProductQnaV2ApiRequest(
        @Schema(description = "상품 그룹 ID", example = "1001") @NotNull(message = "상품 그룹 ID는 필수입니다.")
                Long productGroupId,
        @Schema(description = "작성자 이름", example = "홍길동")
                @NotBlank(message = "작성자 이름은 필수입니다.")
                @Size(max = 50, message = "작성자 이름은 50자를 초과할 수 없습니다.")
                String writerName,
        @Schema(description = "문의 제목", example = "사이즈 문의드립니다")
                @NotBlank(message = "제목은 필수입니다.")
                @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
                String title,
        @Schema(description = "문의 내용", example = "M 사이즈 실측이 어떻게 되나요?")
                @NotBlank(message = "내용은 필수입니다.")
                @Size(max = 4000, message = "내용은 4000자를 초과할 수 없습니다.")
                String content,
        @Schema(description = "문의 세부 유형 (SIZE, DELIVERY, RESTOCK, OTHER)", example = "SIZE")
                @NotBlank(message = "문의 유형은 필수입니다.")
                String detailType,
        @Schema(description = "비밀글 여부", example = "false") boolean isSecret) {}
