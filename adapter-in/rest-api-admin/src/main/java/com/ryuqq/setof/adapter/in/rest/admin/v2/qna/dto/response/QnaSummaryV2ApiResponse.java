package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response;

import com.ryuqq.setof.application.qna.dto.response.QnaSummaryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * QnA Summary V2 API Response
 *
 * <p>문의 요약 API 응답 DTO (목록 조회용)
 *
 * @param id 문의 ID
 * @param type 문의 유형 (PRODUCT, ORDER)
 * @param detailType 문의 세부 유형
 * @param writerName 작성자 이름
 * @param title 제목
 * @param isSecret 비밀글 여부
 * @param status 상태 (OPEN, CLOSED)
 * @param replyCount 답변 수
 * @param createdAt 생성일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "문의 요약 응답 (목록 조회용)")
public record QnaSummaryV2ApiResponse(
        @Schema(description = "문의 ID", example = "1")
        Long id,

        @Schema(description = "문의 유형 (PRODUCT, ORDER)", example = "PRODUCT")
        String type,

        @Schema(description = "문의 세부 유형", example = "SIZE")
        String detailType,

        @Schema(description = "작성자 이름", example = "홍길동")
        String writerName,

        @Schema(description = "제목", example = "사이즈 문의드립니다")
        String title,

        @Schema(description = "비밀글 여부", example = "false")
        boolean isSecret,

        @Schema(description = "상태 (OPEN, CLOSED)", example = "OPEN")
        String status,

        @Schema(description = "답변 수", example = "2")
        int replyCount,

        @Schema(description = "생성일시")
        LocalDateTime createdAt) {

    public static QnaSummaryV2ApiResponse from(QnaSummaryResponse response) {
        return new QnaSummaryV2ApiResponse(
                response.id(),
                response.type(),
                response.detailType(),
                response.writerName(),
                response.title(),
                response.isSecret(),
                response.status(),
                response.replyCount(),
                response.createdAt());
    }
}
