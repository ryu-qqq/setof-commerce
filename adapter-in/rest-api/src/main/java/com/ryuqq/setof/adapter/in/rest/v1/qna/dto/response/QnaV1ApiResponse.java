package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * QnaV1ApiResponse - Q&A 응답 DTO.
 *
 * <p>레거시 QnaResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param qna Q&A 본문 정보
 * @param answers 답변 목록
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.qna.dto.QnaResponse
 */
@Schema(description = "Q&A 응답")
public record QnaV1ApiResponse(

        @Schema(description = "Q&A 본문 정보")
        QnaDetailResponse qna,

        @Schema(description = "답변 목록")
        Set<QnaAnswerResponse> answers

) {

    /**
     * QnaDetailResponse - Q&A 본문 상세 응답.
     *
     * @param qnaId Q&A ID
     * @param title 제목
     * @param content 내용
     * @param privateYn 비밀글 여부
     * @param qnaStatus Q&A 상태
     * @param qnaType Q&A 유형
     * @param qnaDetailType Q&A 상세 유형
     * @param userType 작성자 유형
     * @param images 첨부 이미지 목록
     * @param target 대상 정보 (상품/주문)
     * @param userName 작성자 이름
     * @param insertDate 등록일시
     * @param updateDate 수정일시
     */
    @Schema(description = "Q&A 본문 상세")
    public record QnaDetailResponse(

            @Schema(description = "Q&A ID", example = "1001")
            long qnaId,

            @Schema(description = "제목", example = "사이즈 문의")
            String title,

            @Schema(description = "내용", example = "이 상품 M 사이즈 어깨 실측이 어떻게 되나요?")
            String content,

            @Schema(description = "비밀글 여부", example = "N")
            String privateYn,

            @Schema(description = "Q&A 상태", example = "ANSWERED")
            String qnaStatus,

            @Schema(description = "Q&A 유형", example = "PRODUCT")
            String qnaType,

            @Schema(description = "Q&A 상세 유형", example = "SIZE")
            String qnaDetailType,

            @Schema(description = "작성자 유형", example = "BUYER")
            String userType,

            @Schema(description = "첨부 이미지 목록")
            List<QnaImageResponse> images,

            @Schema(description = "대상 정보 (상품/주문)")
            @JsonInclude(JsonInclude.Include.NON_NULL)
            QnaTargetResponse target,

            @Schema(description = "작성자 이름", example = "홍길동")
            String userName,

            @Schema(description = "등록일시", example = "2024-01-15 10:30:00")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime insertDate,

            @Schema(description = "수정일시", example = "2024-01-15 10:30:00")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime updateDate

    ) {}

    /**
     * QnaAnswerResponse - Q&A 답변 응답.
     *
     * @param answerId 답변 ID
     * @param parentAnswerId 부모 답변 ID (대댓글인 경우)
     * @param writerType 작성자 유형 (SELLER, BUYER 등)
     * @param title 답변 제목
     * @param content 답변 내용
     * @param images 첨부 이미지 목록
     * @param insertDate 등록일시
     * @param updateDate 수정일시
     */
    @Schema(description = "Q&A 답변")
    public record QnaAnswerResponse(

            @Schema(description = "답변 ID", example = "2001")
            long answerId,

            @Schema(description = "부모 답변 ID (대댓글인 경우)", example = "null", nullable = true)
            Long parentAnswerId,

            @Schema(description = "작성자 유형", example = "SELLER")
            String writerType,

            @Schema(description = "답변 제목", example = "답변드립니다")
            String title,

            @Schema(description = "답변 내용", example = "M 사이즈 어깨 실측 45cm입니다.")
            String content,

            @Schema(description = "첨부 이미지 목록")
            List<QnaImageResponse> images,

            @Schema(description = "등록일시", example = "2024-01-15 14:00:00")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime insertDate,

            @Schema(description = "수정일시", example = "2024-01-15 14:00:00")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime updateDate

    ) {}

    /**
     * QnaImageResponse - Q&A 이미지 응답.
     *
     * @param imageUrl 이미지 URL
     */
    @Schema(description = "Q&A 이미지")
    public record QnaImageResponse(

            @Schema(description = "이미지 URL", example = "https://cdn.example.com/qna/img1.jpg")
            String imageUrl

    ) {}

    /**
     * QnaTargetResponse - Q&A 대상 정보 응답 (상품 또는 주문).
     *
     * <p>상품 Q&A인 경우 order* 필드는 null입니다.
     *
     * @param productGroupId 상품그룹 ID
     * @param productGroupName 상품그룹명
     * @param productGroupMainImageUrl 대표 이미지 URL
     * @param brand 브랜드 정보
     * @param paymentId 결제 ID (주문 Q&A인 경우)
     * @param orderId 주문 ID (주문 Q&A인 경우)
     * @param orderAmount 주문 금액 (주문 Q&A인 경우)
     * @param quantity 주문 수량 (주문 Q&A인 경우)
     * @param option 옵션 문자열 (주문 Q&A인 경우)
     */
    @Schema(description = "Q&A 대상 정보")
    public record QnaTargetResponse(

            @Schema(description = "상품그룹 ID", example = "5001")
            long productGroupId,

            @Schema(description = "상품그룹명", example = "클래식 코튼 티셔츠")
            String productGroupName,

            @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/images/5001_main.jpg")
            String productGroupMainImageUrl,

            @Schema(description = "브랜드 정보")
            BrandResponse brand,

            @Schema(description = "결제 ID (주문 Q&A인 경우)", example = "8001", nullable = true)
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Long paymentId,

            @Schema(description = "주문 ID (주문 Q&A인 경우)", example = "9001", nullable = true)
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Long orderId,

            @Schema(description = "주문 금액 (주문 Q&A인 경우)", example = "35000", nullable = true)
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Long orderAmount,

            @Schema(description = "주문 수량 (주문 Q&A인 경우)", example = "1", nullable = true)
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Integer quantity,

            @Schema(description = "옵션 문자열 (주문 Q&A인 경우)", example = "블랙 M", nullable = true)
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String option

    ) {}

    /**
     * BrandResponse - 브랜드 정보 응답.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(

            @Schema(description = "브랜드 ID", example = "100")
            long brandId,

            @Schema(description = "브랜드명", example = "브랜드A")
            String brandName

    ) {}

}
