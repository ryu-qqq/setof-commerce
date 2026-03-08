package com.ryuqq.setof.application.qna.port.out.query;

/**
 * QnaProductPendingQueryPort - 상품 Q&A 미답변 질문 존재 여부 조회 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaProductPendingQueryPort {

    /**
     * 사용자의 특정 상품에 대한 미답변 질문 존재 여부를 확인합니다.
     *
     * @param userId 사용자 ID
     * @param productGroupId 상품그룹 ID
     * @return 미답변 질문이 존재하면 true
     */
    boolean existsPendingProductQna(long userId, long productGroupId);
}
