package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CreateQnaV1ApiResponse - Q&A 질문 등록 응답 DTO.
 *
 * <p>레거시 Qna(엔티티 직렬화) 기반 변환. POST /api/v1/qna 응답에 대응.
 *
 * <p>레거시에서는 Qna 엔티티를 직접 반환했으나, 신규 아키텍처에서는 전용 Response DTO를 사용합니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-003: Response는 @Schema 어노테이션.
 *
 * @param qnaId 생성된 Q&A ID
 * @param qnaType Q&A 유형 (PRODUCT / ORDER)
 * @param qnaDetailType Q&A 상세 유형
 * @param title 제목
 * @param content 내용
 * @param secret 비밀글 여부
 * @param qnaStatus Q&A 상태 (OPEN / CLOSED)
 * @param sellerId 셀러 ID
 * @param insertDate 등록일시
 * @param updateDate 수정일시
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 질문 등록 응답")
public record CreateQnaV1ApiResponse(
        @Schema(description = "생성된 Q&A ID", example = "789") long qnaId,
        @Schema(description = "Q&A 유형", example = "ORDER") String qnaType,
        @Schema(description = "Q&A 상세 유형", example = "SHIPMENT") String qnaDetailType,
        @Schema(description = "제목", example = "배송 문의") String title,
        @Schema(description = "내용", example = "주문한 상품이 아직 안 왔어요.") String content,
        @Schema(description = "비밀글 여부", example = "true") boolean secret,
        @Schema(description = "Q&A 상태 (등록 직후는 항상 OPEN)", example = "OPEN") String qnaStatus,
        @Schema(description = "셀러 ID", example = "1") long sellerId,
        @Schema(description = "등록일시", example = "2024-01-01 00:00:00") String insertDate,
        @Schema(description = "수정일시", example = "2024-01-01 00:00:00") String updateDate) {}
