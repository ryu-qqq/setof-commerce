package com.ryuqq.setof.application.qna.internal;

import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.domain.qna.vo.QnaType;

/**
 * QnaRegistrationStrategy - Q&A 유형별 등록 전략 인터페이스.
 *
 * <p>상품 Q&A와 주문 Q&A의 등록 규칙이 다릅니다:
 * <ul>
 *   <li>상품 Q&A: 이미지 첨부 불가, productGroupId 필수
 *   <li>주문 Q&A: 이미지 첨부 가능(최대 5장), legacyOrderId 필수
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface QnaRegistrationStrategy {

    /** 이 전략이 처리하는 QnaType. */
    QnaType supportType();

    /**
     * 유형별 검증 + 도메인 생성 + 영속화를 수행합니다.
     *
     * @param command 등록 커맨드
     * @return 영속화된 Q&A ID
     */
    Long register(RegisterQnaCommand command);
}
