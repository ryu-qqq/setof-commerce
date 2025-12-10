package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * V1 QNA 목록 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "QNA 목록 응답")
public record FetchQnaV1ApiResponse(@Schema(description = "QNA ID", example = "1") Long qnaId,
        @Schema(description = "QNA 내용") QnaContentsV1ApiResponse qnaContents,
        @Schema(description = "비공개 여부 (Y/N)", example = "N") String privateYn,
        @Schema(description = "QNA 상태", example = "PENDING") String qnaStatus,
        @Schema(description = "QNA 타입", example = "PRODUCT") String qnaType,
        @Schema(description = "QNA 상세 타입", example = "ORDER") String qnaDetailType,
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "사용자 정보") UserInfoQnaV1ApiResponse userInfo,
        @Schema(description = "QNA 대상") QnaTargetV1ApiResponse qnaTarget,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate,
        @Schema(description = "수정 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updateDate,
        @Schema(description = "QNA 이미지 목록") List<QnaImageV1ApiResponse> qnaImages) {

    @Schema(description = "QNA 내용")
    public record QnaContentsV1ApiResponse(
            @Schema(description = "제목", example = "문의 제목") String title,
            @Schema(description = "내용", example = "문의 내용") String content) {
    }

    @Schema(description = "사용자 정보")
    public record UserInfoQnaV1ApiResponse(
            @Schema(description = "사용자 타입", example = "MEMBERS") String userType,
            @Schema(description = "사용자 ID", example = "1") Long userId,
            @Schema(description = "사용자명", example = "홍길동") String userName,
            @Schema(description = "전화번호", example = "01012345678") String phoneNumber,
            @Schema(description = "이메일", example = "user@example.com") String email,
            @Schema(description = "성별", example = "MALE") String gender) {
    }

    @Schema(description = "QNA 대상")
    public record QnaTargetV1ApiResponse(
            @Schema(description = "대상 ID", example = "12345") Long targetId) {
    }

    @Schema(description = "QNA 이미지")
    public record QnaImageV1ApiResponse(
            @Schema(description = "QNA 이슈 타입", example = "QUESTION") String qnaIssueType,
            @Schema(description = "QNA 이미지 ID", example = "1") Long qnaImageId,
            @Schema(description = "QNA ID", example = "1") Long qnaId,
            @Schema(description = "QNA 답변 ID", example = "1") Long qnaAnswerId,
            @Schema(description = "이미지 URL",
                    example = "https://example.com/image.jpg") String imageUrl,
            @Schema(description = "표시 순서", example = "1") Integer displayOrder) {
    }
}
