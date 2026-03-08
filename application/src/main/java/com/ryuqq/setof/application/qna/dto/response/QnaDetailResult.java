package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * QnaDetailResult - 상품 Q&A 단건 결과 DTO.
 *
 * <p>비밀글 마스킹 및 작성자명 마스킹이 적용된 상태로 반환됩니다.
 * QnaPageResult의 items 필드에 사용됩니다.
 *
 * @param qnaId Q&A ID
 * @param title Q&A 제목 (비밀글은 "비밀글입니다" 마스킹)
 * @param content Q&A 내용 (비밀글은 "비밀글입니다" 마스킹)
 * @param privateYn 비밀글 여부 (Y/N)
 * @param qnaStatus Q&A 상태
 * @param qnaType Q&A 유형
 * @param qnaDetailType Q&A 상세 유형
 * @param userType 사용자 유형
 * @param userId 작성자 사용자 ID
 * @param userName 작성자 이름 (끝 2자리 ** 마스킹)
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @param answers 답변 목록 (비밀글이고 비작성자이면 빈 Set)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaDetailResult(
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
        Set<QnaAnswerDetailResult> answers) {

    public static QnaDetailResult of(
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
            Set<QnaAnswerDetailResult> answers) {
        return new QnaDetailResult(
                qnaId, title, content, privateYn, qnaStatus, qnaType, qnaDetailType,
                userType, userId, userName, insertDate, updateDate, answers);
    }
}
