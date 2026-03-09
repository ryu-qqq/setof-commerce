package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.response.PaymentDetailResult;
import com.ryuqq.setof.application.payment.internal.PaymentReadFacade;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentDetailUseCase;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetPaymentDetailService - 결제 단건 상세 조회 서비스.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션.
 *
 * <p>APP-SVC-003: 1 UseCase = 1 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetPaymentDetailService implements GetPaymentDetailUseCase {

    private final PaymentReadFacade paymentReadFacade;
    private final PaymentAssembler paymentAssembler;

    public GetPaymentDetailService(
            PaymentReadFacade paymentReadFacade, PaymentAssembler paymentAssembler) {
        this.paymentReadFacade = paymentReadFacade;
        this.paymentAssembler = paymentAssembler;
    }

    @Override
    public PaymentDetailResult execute(long paymentId, long userId) {
        PaymentFullDetail detail = paymentReadFacade.fetchPaymentDetail(paymentId, userId);
        if (detail == null) {
            return null;
        }
        List<OrderDetail> orderDetails = paymentReadFacade.fetchOrderDetails(detail.orderIds());
        return paymentAssembler.toDetailResult(detail, orderDetails);
    }
}
