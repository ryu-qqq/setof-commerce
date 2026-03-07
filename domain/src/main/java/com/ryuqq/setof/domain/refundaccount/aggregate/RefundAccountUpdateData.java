package com.ryuqq.setof.domain.refundaccount.aggregate;

import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import java.time.Instant;

/**
 * 환불 계좌 수정 데이터 Value Object.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @param bankInfo 새 은행 계좌 정보
 * @param occurredAt 변경 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RefundAccountUpdateData(RefundBankInfo bankInfo, Instant occurredAt) {

    public static RefundAccountUpdateData of(RefundBankInfo bankInfo, Instant occurredAt) {
        return new RefundAccountUpdateData(bankInfo, occurredAt);
    }
}
