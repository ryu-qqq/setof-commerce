package com.ryuqq.setof.application.legacy.qna.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyQnaResult - 레거시 Q&A 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param qnaId Q&A ID
 * @param title 제목
 * @param content 내용
 * @param privateYn 비밀글 여부 (Y/N)
 * @param qnaStatus Q&A 상태
 * @param qnaType Q&A 유형 (PRODUCT/ORDER)
 * @param qnaDetailType Q&A 상세 유형
 * @param userType 사용자 유형
 * @param userId 사용자 ID
 * @param userName 사용자 이름
 * @param sellerId 판매자 ID
 * @param productGroupId 상품그룹 ID
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyQnaResult(
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
        long sellerId,
        Long productGroupId,
        LocalDateTime insertDate,
        LocalDateTime updateDate) {

    /**
     * 팩토리 메서드.
     *
     * @param qnaId Q&A ID
     * @param title 제목
     * @param content 내용
     * @param privateYn 비밀글 여부 (Y/N)
     * @param qnaStatus Q&A 상태
     * @param qnaType Q&A 유형
     * @param qnaDetailType Q&A 상세 유형
     * @param userType 사용자 유형
     * @param userId 사용자 ID
     * @param userName 사용자 이름
     * @param sellerId 판매자 ID
     * @param productGroupId 상품그룹 ID
     * @param insertDate 등록일
     * @param updateDate 수정일
     * @return LegacyQnaResult
     */
    public static LegacyQnaResult of(
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
            long sellerId,
            Long productGroupId,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new LegacyQnaResult(
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
                sellerId,
                productGroupId,
                insertDate,
                updateDate);
    }
}
