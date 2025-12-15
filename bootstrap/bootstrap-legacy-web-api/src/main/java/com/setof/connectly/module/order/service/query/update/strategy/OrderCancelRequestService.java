package com.setof.connectly.module.order.service.query.update.strategy;

import com.setof.connectly.module.exception.order.InvalidOrderStatusException;
import com.setof.connectly.module.exception.order.OrderNotFoundException;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.order.dto.query.ClaimOrder;
import com.setof.connectly.module.order.dto.query.RefundOrderInfo;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.order.service.status.OrderStatusProcessor;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.service.bill.fetch.PaymentBillFindService;
import com.setof.connectly.module.payment.service.pay.PayService;
import com.setof.connectly.module.product.dto.stock.UpdateProductStock;
import com.setof.connectly.module.product.service.individual.query.ProductQueryService;
import com.setof.connectly.module.product.service.stock.ProductStockQueryService;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import com.setof.connectly.module.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderCancelRequestService extends AbstractOrderUpdateService<ClaimOrder> {

    private final StockReservationService stockReservationService;
    private final PaymentBillFindService paymentBillFindService;
    private final ProductStockQueryService stockQueryService;
    private final PayService payService;
    private final ProductQueryService productQueryService;

    public OrderCancelRequestService(
            OrderFindService orderFindService,
            OrderMapper orderMapper,
            OrderStatusProcessor orderStatusProcessor,
            StockReservationService stockReservationService,
            PaymentBillFindService paymentBillFindService,
            ProductStockQueryService stockQueryService,
            PayService payService,
            ProductQueryService productQueryService) {
        super(orderFindService, orderMapper, orderStatusProcessor);
        this.stockReservationService = stockReservationService;
        this.paymentBillFindService = paymentBillFindService;
        this.stockQueryService = stockQueryService;
        this.payService = payService;
        this.productQueryService = productQueryService;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.CANCEL_REQUEST;
    }

    @Override
    public List<UpdateOrderResponse> updateOrder(ClaimOrder claimOrder) {
        checkRaffleOrderProduct(claimOrder.getOrderId());

        PaymentBill paymentBill = fetchPaymentBillEntity(claimOrder.getPaymentId());
        List<Order> orders = fetchOrders(claimOrder.getPaymentId());

        long totalOrderAmount = getTotalOrderAmount(orders);
        Order order = getTargetOrder(orders, claimOrder.getOrderId());

        checkIsRefundableOrder(order, claimOrder.getOrderStatus());

        OrderStatus pastOrderStatus = order.getOrderStatus();
        setClaimOrderStatus(order, claimOrder);
        Order updatedOrder = updateOrderStatus(order, claimOrder.getOrderStatus());

        if (updatedOrder.getOrderStatus().isCancelOrder()) {
            RefundOrderInfo refundOrderInfo =
                    makeRefundOrderSheet(
                            orders,
                            updatedOrder,
                            totalOrderAmount,
                            paymentBill.getUsedMileageAmount(),
                            claimOrder.getReason(),
                            claimOrder.getChangeDetailReason());
            refundProcess(updatedOrder, refundOrderInfo);
        }

        return Collections.singletonList(
                toUpdateOrderResponse(
                        pastOrderStatus,
                        updatedOrder,
                        claimOrder.getChangeReason(),
                        claimOrder.getChangeDetailReason()));
    }

    private void setClaimOrderStatus(Order order, ClaimOrder claimOrder) {

        if (order.getOrderStatus().isOrderCompleted()) {
            claimOrder.setOrderStatus(OrderStatus.CANCEL_REQUEST_COMPLETED);
        }

        if (order.getOrderStatus().isDeliveryPending()) {
            boolean cancelImmediately =
                    DateUtils.isCancelImmediately(order.getUpdateDate(), LocalDateTime.now());
            if (cancelImmediately) {
                claimOrder.setOrderStatus(OrderStatus.CANCEL_REQUEST_COMPLETED);
            }
        }
    }

    private PaymentBill fetchPaymentBillEntity(long paymentId) {
        return paymentBillFindService.fetchPaymentBillEntity(paymentId);
    }

    private long getTotalOrderAmount(List<Order> orders) {
        return orders.stream().mapToLong(Order::getOrderAmount).sum();
    }

    private Order getTargetOrder(List<Order> orders, long orderId) {
        return orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private void checkIsRefundableOrder(Order order, OrderStatus requestOrderStatus) {
        if (!order.isAvailableRefundOrder())
            throw new InvalidOrderStatusException(
                    order.getId(), order.getOrderStatus(), requestOrderStatus);
    }

    private void refundProcess(Order order, RefundOrderSheet refundOrderSheet) {
        payService.refundOrder(order.getPaymentId(), refundOrderSheet);
        stockQueryService.updateProductStock(
                new UpdateProductStock(order.getProductId(), order.getQuantity() * -1));
        productQueryService.rollBackUpdatesStatus(Collections.singletonList(order.getProductId()));
        stockReservationService.cancelReserve(order.getId());
    }

    private RefundOrderInfo makeRefundOrderSheet(
            List<Order> orders,
            Order order,
            long totalOrderAmount,
            double usedMileageAmount,
            String reason,
            String detailReason) {

        long refundMileage =
                calculateRefundMileage(totalOrderAmount, order.getOrderAmount(), usedMileageAmount);

        return RefundOrderInfo.builder()
                .allCanceled(areAllOrdersClaimable(orders))
                .expectedRefundAmount(order.getOrderAmount() - refundMileage)
                .expectedRefundMileage(refundMileage)
                .orderId(order.getId())
                .orderAmount(order.getOrderAmount())
                .orderStatus(order.getOrderStatus())
                .reason(reason)
                .detailReason(detailReason)
                .build();
    }

    private long calculateRefundMileage(
            long totalOrderAmount, long orderAmount, double usedMileageAmount) {
        double refundRatio = (double) orderAmount / totalOrderAmount;
        return Math.round(usedMileageAmount * refundRatio);
    }

    private boolean areAllOrdersClaimable(List<Order> orders) {
        return orders.stream().allMatch(order -> order.getOrderStatus().isCancelOrder());
    }
}
