package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * QnaV1ApiResponse - QnA 목록 조회 응답 DTO.
 *
 * <p>레거시 FetchQnaResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.qna.dto.fetch.FetchQnaResponse
 */
@Schema(description = "QnA 응답")
public record QnaV1ApiResponse(

    @Schema(description = "QnA ID", example = "12345")
    long qnaId,

    @Schema(description = "QnA 내용")
    QnaContentsResponse qnaContents,

    @Schema(description = "비공개 여부", example = "N")
    String privateYn,

    @Schema(description = "QnA 상태", example = "OPEN")
    String qnaStatus,

    @Schema(description = "QnA 유형", example = "PRODUCT")
    String qnaType,

    @Schema(description = "QnA 상세 유형", example = "SIZE")
    String qnaDetailType,

    @Schema(description = "판매자명", example = "패션스토어")
    String sellerName,

    @Schema(description = "작성자 정보")
    UserInfoResponse userInfo,

    @Schema(description = "QnA 대상 정보 (상품 또는 주문)")
    QnaTargetResponse qnaTarget,

    @Schema(description = "등록일시", example = "2026-01-15T14:30:00")
    LocalDateTime insertDate,

    @Schema(description = "수정일시", example = "2026-01-16T10:00:00")
    LocalDateTime updateDate,

    @Schema(description = "QnA 이미지 목록")
    List<QnaImageResponse> qnaImages

) {

    @Schema(description = "QnA 내용")
    public record QnaContentsResponse(

        @Schema(description = "제목", example = "사이즈 문의드립니다")
        String title,

        @Schema(description = "내용", example = "M 사이즈 실측이 어떻게 되나요?")
        String content

    ) {}

    @Schema(description = "작성자 정보")
    public record UserInfoResponse(

        @Schema(description = "사용자 유형", example = "MEMBERS")
        String userType,

        @Schema(description = "사용자 ID", example = "5678")
        Long userId,

        @Schema(description = "사용자명", example = "홍길동")
        String userName,

        @Schema(description = "연락처", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "이메일", example = "hong@email.com")
        String email,

        @Schema(description = "성별", example = "MALE")
        String gender

    ) {}

    @Schema(description = "QnA 대상 정보")
    public record QnaTargetResponse(

        @Schema(description = "상품그룹 ID", example = "100")
        long productGroupId,

        @Schema(description = "상품그룹명", example = "클래식 티셔츠")
        String productGroupName,

        @Schema(description = "상품 메인 이미지 URL", example = "https://cdn.example.com/image.jpg")
        String productGroupMainImageUrl,

        @Schema(description = "브랜드 정보")
        BrandResponse brand,

        @Schema(description = "결제 ID (주문문의 전용)", example = "1001")
        Long paymentId,

        @Schema(description = "주문 ID (주문문의 전용)", example = "2001")
        Long orderId,

        @Schema(description = "옵션 정보 (주문문의 전용)", example = "블랙 M")
        String option

    ) {}

    @Schema(description = "브랜드 정보")
    public record BrandResponse(

        @Schema(description = "브랜드 ID", example = "10")
        long brandId,

        @Schema(description = "브랜드명", example = "BRAND_A")
        String brandName

    ) {}

    @Schema(description = "QnA 이미지")
    public record QnaImageResponse(

        @Schema(description = "이미지 이슈 유형", example = "QUESTION")
        String qnaIssueType,

        @Schema(description = "QnA 이미지 ID", example = "1")
        Long qnaImageId,

        @Schema(description = "QnA ID", example = "12345")
        Long qnaId,

        @Schema(description = "QnA 답변 ID", example = "999")
        Long qnaAnswerId,

        @Schema(description = "이미지 URL", example = "https://cdn.example.com/qna/image1.jpg")
        String imageUrl,

        @Schema(description = "표시 순서", example = "1")
        int displayOrder

    ) {}

}
