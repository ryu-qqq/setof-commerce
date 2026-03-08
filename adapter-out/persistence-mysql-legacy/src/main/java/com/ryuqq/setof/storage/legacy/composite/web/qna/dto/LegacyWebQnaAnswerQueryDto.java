package com.ryuqq.setof.storage.legacy.composite.web.qna.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebQnaAnswerQueryDto - 레거시 Q&A 답변 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (@QueryProjection 금지).
 *
 * <p>GroupBy.set()으로 Q&A 본문과 함께 집계됩니다.
 *
 * @param qnaAnswerId 답변 ID (null 가능 - 답변 없는 Q&A)
 * @param qnaParentId 부모 답변 ID (대댓글 지원, null 가능)
 * @param qnaWriterType 답변 작성자 유형 (SELLER / CUSTOMER)
 * @param title 답변 제목
 * @param content 답변 내용
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebQnaAnswerQueryDto(
        Long qnaAnswerId,
        Long qnaParentId,
        String qnaWriterType,
        String title,
        String content,
        LocalDateTime insertDate,
        LocalDateTime updateDate) {

    /**
     * 유효한 답변인지 확인 (qnaAnswerId가 null이면 답변 없음).
     *
     * @return 답변이 존재하면 true
     */
    public boolean isPresent() {
        return qnaAnswerId != null;
    }
}
