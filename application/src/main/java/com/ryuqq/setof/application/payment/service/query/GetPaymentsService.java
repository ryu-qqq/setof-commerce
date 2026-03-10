package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.query.PaymentSearchParams;
import com.ryuqq.setof.application.payment.dto.response.PaymentSliceResult;
import com.ryuqq.setof.application.payment.factory.PaymentQueryFactory;
import com.ryuqq.setof.application.payment.internal.PaymentReadFacade;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentsUseCase;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * GetPaymentsService - 결제 목록 조회 서비스.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션.
 *
 * <p>APP-SVC-003: 1 UseCase = 1 Service.
 *
 * <p>레거시 호환을 위해 orderProducts도 함께 조회하여 CustomSlice 형태로 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetPaymentsService implements GetPaymentsUseCase {

    private final PaymentQueryFactory paymentQueryFactory;
    private final PaymentReadFacade paymentReadFacade;
    private final PaymentAssembler paymentAssembler;

    public GetPaymentsService(
            PaymentQueryFactory paymentQueryFactory,
            PaymentReadFacade paymentReadFacade,
            PaymentAssembler paymentAssembler) {
        this.paymentQueryFactory = paymentQueryFactory;
        this.paymentReadFacade = paymentReadFacade;
        this.paymentAssembler = paymentAssembler;
    }

    @Override
    public PaymentSliceResult execute(PaymentSearchParams params) {
        PaymentSearchCriteria criteria = paymentQueryFactory.createCriteria(params);

        List<Long> paymentIds = paymentReadFacade.fetchPaymentIds(criteria);

        List<Long> pagePaymentIds =
                paymentIds.size() > criteria.size()
                        ? paymentIds.subList(0, criteria.size())
                        : paymentIds;

        List<PaymentOverview> overviews = paymentReadFacade.fetchPaymentOverviews(pagePaymentIds);

        Set<Long> allOrderIds = new LinkedHashSet<>();
        for (PaymentOverview overview : overviews) {
            if (overview.orderIds() != null) {
                allOrderIds.addAll(overview.orderIds());
            }
        }

        List<OrderDetail> orderDetails = paymentReadFacade.fetchOrderDetails(allOrderIds);

        long totalElements = paymentReadFacade.countPayments(criteria);

        return paymentAssembler.toSliceResult(
                overviews, orderDetails, criteria, paymentIds, totalElements);
    }
}
