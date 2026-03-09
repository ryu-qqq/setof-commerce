package com.ryuqq.setof.application.payment.assembler;

import com.ryuqq.setof.application.payment.dto.response.PaymentDetailResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentOverviewResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentSliceResult;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * PaymentAssembler - 결제 Result DTO 조립.
 *
 * <p>Manager에서 조회한 도메인 데이터를 조합하여 최종 응답 객체를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentAssembler {

    /**
     * 결제 목록 슬라이스 결과 조립.
     *
     * @param overviews 결제 개요 목록 (도메인 VO)
     * @param criteria 검색 조건 (size 판단용)
     * @param paymentIds 조회된 결제 ID 목록 (hasNext 판단용, fetchSize 포함)
     * @return PaymentSliceResult
     */
    public PaymentSliceResult toSliceResult(
            List<PaymentOverview> overviews,
            PaymentSearchCriteria criteria,
            List<Long> paymentIds) {

        boolean hasNext = paymentIds.size() > criteria.size();
        List<Long> pagePaymentIds = hasNext ? paymentIds.subList(0, criteria.size()) : paymentIds;
        Long lastId =
                pagePaymentIds.isEmpty() ? null : pagePaymentIds.get(pagePaymentIds.size() - 1);

        List<PaymentOverviewResult> content =
                overviews.stream().map(PaymentOverviewResult::from).toList();

        return new PaymentSliceResult(content, hasNext, lastId);
    }

    /**
     * 결제 단건 상세 결과 조립.
     *
     * @param detail 결제 전체 상세 (도메인 VO)
     * @param orderDetails 주문 상세 목록 (도메인 VO)
     * @return PaymentDetailResult
     */
    public PaymentDetailResult toDetailResult(
            PaymentFullDetail detail, List<OrderDetail> orderDetails) {
        return PaymentDetailResult.from(detail, orderDetails);
    }
}
