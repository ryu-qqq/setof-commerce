package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.query.GetPaymentsQuery;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentsUseCase;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetPaymentsService - 결제 목록 조회 Service
 *
 * <p>회원의 결제 목록을 커서 기반 페이지네이션으로 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetPaymentsService implements GetPaymentsUseCase {

    private final PaymentReadManager paymentReadManager;
    private final PaymentAssembler paymentAssembler;

    public GetPaymentsService(
            PaymentReadManager paymentReadManager, PaymentAssembler paymentAssembler) {
        this.paymentReadManager = paymentReadManager;
        this.paymentAssembler = paymentAssembler;
    }

    /**
     * 결제 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식: limit + 1 조회하여 hasNext 판단
     *
     * @param query 조회 조건
     * @return SliceResponse<PaymentResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public SliceResponse<PaymentResponse> getPayments(GetPaymentsQuery query) {
        List<Payment> payments = paymentReadManager.searchPayments(query);

        boolean hasNext = payments.size() > query.pageSize();
        List<Payment> content = hasNext ? payments.subList(0, query.pageSize()) : payments;

        List<PaymentResponse> responses =
                content.stream().map(paymentAssembler::toResponse).toList();

        String nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            Payment lastPayment = content.get(content.size() - 1);
            nextCursor = lastPayment.id().value().toString();
        }

        return SliceResponse.of(responses, query.pageSize(), hasNext, nextCursor);
    }
}
