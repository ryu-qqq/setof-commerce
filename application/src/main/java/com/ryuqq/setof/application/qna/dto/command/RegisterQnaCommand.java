package com.ryuqq.setof.application.qna.dto.command;

import java.util.List;

/**
 * RegisterQnaCommand - Q&A 질문 등록 커맨드 DTO.
 *
 * <p>POST /api/v1/qna 엔드포인트 대응. PRODUCT/ORDER 분기, 이미지 첨부 지원.
 *
 * @param userId 사용자 ID (레거시)
 * @param sellerId 판매자 ID
 * @param qnaType Q&A 유형 (PRODUCT/ORDER)
 * @param qnaDetailType Q&A 상세 유형
 * @param productGroupId 상품그룹 ID (PRODUCT 유형, null 가능)
 * @param legacyOrderId 레거시 주문 ID (ORDER 유형, null 가능)
 * @param title 질문 제목
 * @param content 질문 내용
 * @param secret 비밀글 여부
 * @param imageUrls 첨부 이미지 URL 목록 (최대 5개)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterQnaCommand(
        Long userId,
        Long sellerId,
        String qnaType,
        String qnaDetailType,
        Long productGroupId,
        Long legacyOrderId,
        String title,
        String content,
        boolean secret,
        List<String> imageUrls) {

    public RegisterQnaCommand {
        if (imageUrls == null) {
            imageUrls = List.of();
        }
    }

    public static RegisterQnaCommand of(
            Long userId,
            Long sellerId,
            String qnaType,
            String qnaDetailType,
            Long productGroupId,
            Long legacyOrderId,
            String title,
            String content,
            boolean secret,
            List<String> imageUrls) {
        return new RegisterQnaCommand(
                userId,
                sellerId,
                qnaType,
                qnaDetailType,
                productGroupId,
                legacyOrderId,
                title,
                content,
                secret,
                imageUrls);
    }

    public boolean isProductQna() {
        return "PRODUCT".equals(qnaType);
    }

    public boolean isOrderQna() {
        return "ORDER".equals(qnaType);
    }

    public boolean hasImages() {
        return imageUrls != null && !imageUrls.isEmpty();
    }
}
