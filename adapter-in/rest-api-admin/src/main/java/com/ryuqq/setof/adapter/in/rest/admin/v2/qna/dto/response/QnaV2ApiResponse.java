package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response;

import com.ryuqq.setof.application.qna.dto.response.QnaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * QnA V2 API Response
 *
 * <p>문의 상세 API 응답 DTO
 *
 * @param id 문의 ID
 * @param type 문의 유형 (PRODUCT, ORDER)
 * @param detailType 문의 세부 유형
 * @param targetId 대상 ID (상품 그룹 ID 또는 주문 ID)
 * @param writerId 작성자 ID
 * @param writerType 작성자 유형
 * @param writerName 작성자 이름
 * @param title 제목
 * @param content 내용
 * @param isSecret 비밀글 여부
 * @param status 상태 (OPEN, CLOSED)
 * @param replyCount 답변 수
 * @param images 이미지 목록
 * @param createdAt 생성일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "문의 상세 응답")
public record QnaV2ApiResponse(
        @Schema(description = "문의 ID", example = "1")
        Long id,

        @Schema(description = "문의 유형 (PRODUCT, ORDER)", example = "PRODUCT")
        String type,

        @Schema(description = "문의 세부 유형", example = "SIZE")
        String detailType,

        @Schema(description = "대상 ID", example = "1001")
        Long targetId,

        @Schema(description = "작성자 ID", example = "01912345-6789-7abc-def0-123456789012")
        String writerId,

        @Schema(description = "작성자 유형", example = "CUSTOMER")
        String writerType,

        @Schema(description = "작성자 이름", example = "홍길동")
        String writerName,

        @Schema(description = "제목", example = "사이즈 문의드립니다")
        String title,

        @Schema(description = "내용", example = "M 사이즈 실측이 어떻게 되나요?")
        String content,

        @Schema(description = "비밀글 여부", example = "false")
        boolean isSecret,

        @Schema(description = "상태 (OPEN, CLOSED)", example = "OPEN")
        String status,

        @Schema(description = "답변 수", example = "2")
        int replyCount,

        @Schema(description = "이미지 목록")
        List<QnaImageV2ApiResponse> images,

        @Schema(description = "생성일시")
        LocalDateTime createdAt) {

    public static QnaV2ApiResponse from(QnaResponse response) {
        List<QnaImageV2ApiResponse> images = response.images() == null
                ? List.of()
                : response.images().stream()
                        .map(QnaImageV2ApiResponse::from)
                        .toList();

        return new QnaV2ApiResponse(
                response.id(),
                response.type(),
                response.detailType(),
                response.targetId(),
                response.writerId(),
                response.writerType(),
                response.writerName(),
                response.title(),
                response.content(),
                response.isSecret(),
                response.status(),
                response.replyCount(),
                images,
                response.createdAt());
    }
}
