package com.ryuqq.setof.application.payment.assembler;

import com.ryuqq.setof.application.order.dto.response.OrderDetailResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentDetailResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentOverviewResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentSliceResult;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
     * 결제 목록 슬라이스 결과 조립 (레거시 CustomSlice 호환).
     *
     * @param overviews 결제 개요 목록 (도메인 VO)
     * @param orderDetails 주문 상세 목록 (도메인 VO)
     * @param criteria 검색 조건 (size 판단용)
     * @param paymentIds 조회된 결제 ID 목록 (hasNext 판단용, fetchSize 포함)
     * @param totalElements 전체 결제 건수
     * @return PaymentSliceResult (CustomSlice 호환)
     */
    public PaymentSliceResult toSliceResult(
            List<PaymentOverview> overviews,
            List<OrderDetail> orderDetails,
            PaymentSearchCriteria criteria,
            List<Long> paymentIds,
            long totalElements) {

        boolean hasNext = paymentIds.size() > criteria.size();
        List<Long> pagePaymentIds = hasNext ? paymentIds.subList(0, criteria.size()) : paymentIds;
        Long lastId =
                pagePaymentIds.isEmpty() ? null : pagePaymentIds.get(pagePaymentIds.size() - 1);

        Map<Long, List<OrderDetailResult.OrderProductResult>> orderProductsByPaymentId =
                groupOrderProductsByPaymentId(overviews, orderDetails);

        List<PaymentOverviewResult> content =
                overviews.stream()
                        .map(
                                vo -> {
                                    List<OrderDetailResult.OrderProductResult> products =
                                            orderProductsByPaymentId.getOrDefault(
                                                    vo.paymentId(), List.of());
                                    return PaymentOverviewResult.from(vo, products);
                                })
                        .toList();

        boolean first = !criteria.hasCursor();
        boolean last = !hasNext;
        boolean empty = content.isEmpty();
        int numberOfElements = content.size();

        Long resolvedTotalElements = empty ? null : totalElements;

        return new PaymentSliceResult(
                content,
                last,
                first,
                0,
                criteria.size(),
                numberOfElements,
                empty,
                lastId,
                lastId != null ? String.valueOf(lastId) : null,
                resolvedTotalElements);
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

    /**
     * orderDetails를 paymentId 기준으로 그룹핑.
     *
     * <p>PaymentOverview에서 orderIds를 추출 → OrderDetail의 orderId와 매칭하여 payment별로 분류합니다.
     */
    private Map<Long, List<OrderDetailResult.OrderProductResult>> groupOrderProductsByPaymentId(
            List<PaymentOverview> overviews, List<OrderDetail> orderDetails) {
        Map<Long, OrderDetail> orderDetailMap =
                orderDetails.stream()
                        .collect(Collectors.toMap(OrderDetail::orderId, d -> d, (a, b) -> a));

        Map<Long, List<OrderDetailResult.OrderProductResult>> result = new LinkedHashMap<>();
        for (PaymentOverview overview : overviews) {
            Set<Long> orderIds = overview.orderIds();
            List<OrderDetailResult.OrderProductResult> products = new ArrayList<>();
            if (orderIds != null) {
                for (Long orderId : orderIds) {
                    OrderDetail detail = orderDetailMap.get(orderId);
                    if (detail != null) {
                        products.add(OrderDetailResult.OrderProductResult.from(detail));
                    }
                }
            }
            result.put(overview.paymentId(), products);
        }
        return result;
    }
}
