package com.ryuqq.setof.application.qna.port.out.query;

/**
 * QnaOrderPendingQueryPort - 주문 Q&A 미답변 질문 존재 여부 조회 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaOrderPendingQueryPort {

    /**
     * 사용자의 특정 주문에 대한 미답변 질문 존재 여부를 확인합니다.
     *
     * @param userId 사용자 ID
     * @param legacyOrderId 레거시 주문 ID
     * @return 미답변 질문이 존재하면 true
     */
    boolean existsPendingOrderQna(long userId, long legacyOrderId);
}
