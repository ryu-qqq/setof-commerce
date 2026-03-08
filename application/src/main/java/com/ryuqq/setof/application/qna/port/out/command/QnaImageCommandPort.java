package com.ryuqq.setof.application.qna.port.out.command;

import com.ryuqq.setof.domain.qna.aggregate.QnaImages;

/**
 * QnaImageCommandPort - Q&A 이미지 명령 출력 포트.
 *
 * <p>Q&A 이미지를 별도로 영속합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaImageCommandPort {

    /**
     * Q&A 이미지 일괄 저장.
     *
     * @param qnaId 레거시 Q&A ID
     * @param images 이미지 일급 컬렉션
     */
    void persistAll(long qnaId, QnaImages images);

    /**
     * Q&A 이미지 전체 삭제 (소프트 삭제).
     *
     * @param qnaId 레거시 Q&A ID
     */
    void deleteAllByQnaId(long qnaId);
}
