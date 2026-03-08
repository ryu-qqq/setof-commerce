package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.vo.QnaType;

/**
 * QnaModificationStrategy - Q&A 유형별 수정 전략 인터페이스.
 *
 * <p>상품 Q&A와 주문 Q&A의 수정 규칙이 다릅니다:
 *
 * <ul>
 *   <li>상품 Q&A: 이미지 수정 불가, qna 테이블만 수정
 *   <li>주문 Q&A: 이미지 교체 가능, qna + qna_image 수정 (diff 패턴)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaModificationStrategy {

    /** 이 전략이 처리하는 QnaType. */
    QnaType supportType();

    /**
     * 유형별 Q&A 수정을 수행합니다.
     *
     * @param qna 기존 Q&A 도메인 객체
     * @param command 수정 커맨드
     */
    void modify(Qna qna, ModifyQnaCommand command);
}
