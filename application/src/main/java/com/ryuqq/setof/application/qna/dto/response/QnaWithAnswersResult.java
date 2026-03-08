package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * QnaWithAnswersResult - Q&A + 답변 목록 조회 결과 DTO.
 *
 * <p>상품 Q&A 조회 (fetchProductQnas) 공통 응답.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param qnaId Q&A ID
 * @param title Q&A 제목
 * @param content Q&A 내용
 * @param privateYn 비밀글 여부 (Y/N)
 * @param qnaStatus Q&A 상태
 * @param qnaType Q&A 유형 (PRODUCT / ORDER)
 * @param qnaDetailType Q&A 상세 유형
 * @param userType 사용자 유형
 * @param userId 작성자 사용자 ID
 * @param userName 작성자 이름
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @param answers 답변 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaWithAnswersResult(
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
        LocalDateTime insertDate,
        LocalDateTime updateDate,
        Set<QnaAnswerResult> answers) {

    public static QnaWithAnswersResult of(
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
            LocalDateTime insertDate,
            LocalDateTime updateDate,
            Set<QnaAnswerResult> answers) {
        return new QnaWithAnswersResult(
                qnaId, title, content, privateYn, qnaStatus, qnaType, qnaDetailType,
                userType, userId, userName, insertDate, updateDate, answers);
    }
}
