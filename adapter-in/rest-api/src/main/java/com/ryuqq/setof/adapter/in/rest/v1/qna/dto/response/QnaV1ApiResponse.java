package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "QnA 응답")
public record QnaV1ApiResponse(@Schema(description = "QnA ID", example = "1") Long id,
        @Schema(description = "질문 정보") FetchQnaV1ApiResponse qna,
        @Schema(description = "답변 목록") Set<AnswerQnaV1ApiResponse> answerQnas) {

    @Schema(description = "질문 정보")
    public record FetchQnaV1ApiResponse(@Schema(description = "QnA ID", example = "1") long qnaId,
            @Schema(description = "QnA 내용") QnaContentsV1ApiResponse qnaContents,
            @Schema(description = "비밀글 여부", example = "Y") String privateYn,
            @Schema(description = "QnA 상태", example = "ANSWERED") String qnaStatus,
            @Schema(description = "QnA 유형", example = "PRODUCT") String qnaType,
            @Schema(description = "QnA 상세 유형", example = "DELIVERY") String qnaDetailType,
            @Schema(description = "작성자 유형", example = "CUSTOMER") String userType,
            @Schema(description = "첨부 이미지 목록") List<QnaImageV1ApiResponse> qnaImages,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(
                    description = "QnA 대상 정보 (상품/주문)") QnaTargetV1ApiResponse qnaTarget,
            @Schema(description = "작성자 ID", example = "100") long userId,
            @Schema(description = "작성자명 (마스킹)", example = "홍*동") String userName,
            @Schema(description = "작성 일시",
                    example = "2024-01-01 12:00:00") LocalDateTime insertDate,
            @Schema(description = "수정 일시",
                    example = "2024-01-02 12:00:00") LocalDateTime updateDate) {
    }

    @Schema(description = "답변 정보")
    public record AnswerQnaV1ApiResponse(
            @Schema(description = "QnA 답변 ID", example = "10") long qnaAnswerId,
            @Schema(description = "부모 답변 ID", example = "9") Long qnaAnswerParentId,
            @Schema(description = "답변 작성자 유형", example = "ADMIN") String qnaWriterType,
            @Schema(description = "답변 내용") QnaContentsV1ApiResponse qnaContents,
            @Schema(description = "첨부 이미지 목록") List<QnaImageV1ApiResponse> qnaImages,
            @Schema(description = "작성 일시",
                    example = "2024-01-03 12:00:00") LocalDateTime insertDate,
            @Schema(description = "수정 일시",
                    example = "2024-01-04 12:00:00") LocalDateTime updateDate) {
    }

    @Schema(description = "QnA 이미지 정보")
    public record QnaImageV1ApiResponse(
            @Schema(description = "이슈 유형", example = "DELIVERY") String qnaIssueType,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "QnA 이미지 ID",
                    example = "11") Long qnaImageId,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "QnA ID",
                    example = "1") Long qnaId,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "QnA 답변 ID",
                    example = "10") Long qnaAnswerId,
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "노출 순서", example = "1") int displayOrder) {
    }

    @Schema(description = "QnA 본문 정보")
    public record QnaContentsV1ApiResponse(
            @Schema(description = "제목", example = "배송이 언제 되나요?") String title,
            @Schema(description = "내용", example = "주문한 상품 배송 일정이 궁금합니다.") String content) {
    }

    @Schema(description = "QnA 대상 정보 (마커 인터페이스)")
    public sealed interface QnaTargetV1ApiResponse
            permits ProductQnaTargetV1ApiResponse, OrderQnaTargetV1ApiResponse {
    }

    @Schema(description = "상품 QnA 대상 정보")
    public record ProductQnaTargetV1ApiResponse(
            @Schema(description = "상품군 ID", example = "1000") long productGroupId,
            @Schema(description = "상품군 명", example = "스니커즈") String productGroupName,
            @Schema(description = "대표 이미지 URL") String productGroupMainImageUrl,
            @Schema(description = "브랜드 정보") BrandV1ApiResponse brand)
            implements QnaTargetV1ApiResponse {
    }

    @Schema(description = "브랜드 정보")
    public record BrandV1ApiResponse(@Schema(description = "브랜드 ID", example = "10") long brandId,
            @Schema(description = "브랜드 명", example = "나이키") String brandName) {
    }

    @Schema(description = "주문 QnA 대상 정보")
    public record OrderQnaTargetV1ApiResponse(
            @Schema(description = "상품 정보") ProductQnaTargetV1ApiResponse product,
            @Schema(description = "결제 ID", example = "500") Long paymentId,
            @Schema(description = "주문 ID", example = "600") Long orderId,
            @Schema(description = "주문 금액", example = "150000") Long orderAmount,
            @Schema(description = "수량", example = "2") Integer quantity,
            @Schema(description = "옵션", example = "블랙 / 260") String option)
            implements QnaTargetV1ApiResponse {
    }
}
