package com.ryuqq.setof.domain.member.aggregate;

import com.ryuqq.setof.domain.member.vo.RefundBankInfo;

/**
 * 환불 계좌 수정 데이터 Value Object.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @param bankInfo 새 은행 계좌 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RefundAccountUpdateData(RefundBankInfo bankInfo) {

    public static RefundAccountUpdateData of(RefundBankInfo bankInfo) {
        return new RefundAccountUpdateData(bankInfo);
    }
}
