package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * MyQnaDetailResult - 내 Q&A 단건 결과 DTO.
 *
 * <p>PRODUCT/ORDER 공통 결과. 타입별로 사용되지 않는 필드는 null.
 *
 * <p>상품 Q&A: productGroupId, productGroupName, imageUrl, brandId, brandName 사용. ORDER 필드 null.
 *
 * <p>주문 Q&A: orderId, paymentId, orderAmount, quantity, option + 스냅샷 상품 정보 사용.
 *
 * @param qnaId Q&A ID
 * @param title Q&A 제목
 * @param content Q&A 내용
 * @param privateYn 비밀글 여부 (Y/N)
 * @param qnaStatus Q&A 상태
 * @param qnaType Q&A 유형 (PRODUCT/ORDER)
 * @param qnaDetailType Q&A 상세 유형
 * @param userType 사용자 유형
 * @param userId 작성자 사용자 ID
 * @param userName 작성자 이름
 * @param productGroupId 상품그룹 ID (nullable)
 * @param productGroupName 상품그룹명 (nullable)
 * @param imageUrl 대표 이미지 URL (nullable)
 * @param brandId 브랜드 ID (nullable)
 * @param brandName 브랜드명 (nullable)
 * @param orderId 주문 ID (ORDER 전용, nullable)
 * @param paymentId 결제 ID (ORDER 전용, nullable)
 * @param orderAmount 주문 금액 (ORDER 전용, nullable)
 * @param quantity 주문 수량 (ORDER 전용, nullable)
 * @param option 옵션 문자열 (ORDER 전용, nullable)
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @param answers 답변 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MyQnaDetailResult(
        long qnaId,
        String title,
        String content,
        String privateYn,
        String qnaStatus,
        String qnaType,
        String qnaDetailType,
        String userType,
        long userId,
        String userName,
        Long productGroupId,
        String productGroupName,
        String imageUrl,
        Long brandId,
        String brandName,
        Long orderId,
        Long paymentId,
        Long orderAmount,
        Integer quantity,
        String option,
        LocalDateTime insertDate,
        LocalDateTime updateDate,
        Set<QnaAnswerDetailResult> answers) {

    public static MyQnaDetailResult of(
            long qnaId,
            String title,
            String content,
            String privateYn,
            String qnaStatus,
            String qnaType,
            String qnaDetailType,
            String userType,
            long userId,
            String userName,
            Long productGroupId,
            String productGroupName,
            String imageUrl,
            Long brandId,
            String brandName,
            Long orderId,
            Long paymentId,
            Long orderAmount,
            Integer quantity,
            String option,
            LocalDateTime insertDate,
            LocalDateTime updateDate,
            Set<QnaAnswerDetailResult> answers) {
        return new MyQnaDetailResult(
                qnaId,
                title,
                content,
                privateYn,
                qnaStatus,
                qnaType,
                qnaDetailType,
                userType,
                userId,
                userName,
                productGroupId,
                productGroupName,
                imageUrl,
                brandId,
                brandName,
                orderId,
                paymentId,
                orderAmount,
                quantity,
                option,
                insertDate,
                updateDate,
                answers);
    }
}
