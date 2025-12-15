package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.module.exception.payment.AlReadyRefundPaymentException;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.order.dto.query.NormalOrder;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.service.query.OrderCreateService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderCompletedService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderFailService;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePaymentInCart;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.entity.Payment;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;
import com.setof.connectly.module.payment.mapper.PaymentMapper;
import com.setof.connectly.module.payment.repository.PaymentRepository;
import com.setof.connectly.module.payment.service.bill.query.PaymentBillQueryService;
import com.setof.connectly.module.payment.service.pay.fetch.PaymentFindService;
import com.setof.connectly.module.payment.service.snapshot.PaymentSnapShotShippingQueryService;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public abstract class AbstractPayQueryService implements PaymentQueryService {
    private final PaymentFindService paymentFindService;
    private final PaymentRepository paymentRepository;
    private final PaymentBillQueryService paymentBillQueryService;
    private final PaymentMapper paymentMapper;
    private final PaymentSnapShotShippingQueryService paymentSnapShotShippingQueryService;
    private final OrderCompletedService<? extends OrderSnapShot> orderCompletedService;
    private final OrderFailService orderFailService;
    private final OrderCreateService orderCreateService;
    private final StockReservationService stockReservationService;

    @Override
    public PaymentGatewayRequestDto doPay(CreatePayment payment) {
        Payment savedPayment = savePayment(payment);
        payment.setPaymentId(savedPayment.getId());
        savePaymentBill(savedPayment.getId(), payment);
        saveShippingAddress(savedPayment.getId(), payment);
        List<Order> orders = doOrders(payment.getOrders());
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        double sum = orders.stream().mapToDouble(Order::getOrderAmount).sum();
        return generatePaymentKey(savedPayment.getId(), orderIds, sum);
    }

    @Override
    public PaymentGatewayRequestDto doPayInCart(CreatePaymentInCart payment) {
        Payment savedPayment = savePayment(payment);
        payment.setPaymentId(savedPayment.getId());
        savePaymentBill(savedPayment.getId(), payment);
        saveShippingAddress(savedPayment.getId(), payment);
        List<Order> orders = doOrders(payment.getOrders());
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        double sum = orders.stream().mapToDouble(Order::getOrderAmount).sum();
        return generatePaymentKey(savedPayment.getId(), orderIds, sum);
    }

    @Override
    public Optional<PaymentBill> doPayWebHook(PgProviderTransDto pgProviderTransDto) {
        payCompleted(pgProviderTransDto.getPaymentId());
        PaymentBill paymentBill = updatePaymentBill(pgProviderTransDto);
        NormalOrder normalOrder =
                new NormalOrder(paymentBill.getPaymentId(), 0L, OrderStatus.ORDER_COMPLETED, true);
        doOrderWebHook(normalOrder);
        return Optional.of(paymentBill);
    }

    @Override
    public void doPayFailed(long paymentId, List<Long> cartIds) {
        Payment payment = paymentFindService.fetchPaymentEntityForWebhook(paymentId);
        payment.payFailed();
        stockReservationService.cancelsReserve(paymentId);
        doOrderFail(payment.getId());
    }

    @Override
    public void doPayRefund(
            PgProviderTransDto pgProviderTransDto, RefundOrderSheet refundOrderSheet) {
        Payment payment = fetchPayment(pgProviderTransDto.getPaymentId());
        if (!payment.isAvailableRefundPayment()) throw new AlReadyRefundPaymentException(payment);

        if (refundOrderSheet.getOrderStatus().isCancelOrder()) {
            if (refundOrderSheet.isAllCanceled()) payment.paymentRefunded();
            else payment.paymentPartialRefunded();
        }
    }

    protected void payCompleted(long paymentId) {
        Payment payment = fetchPayment(paymentId);
        payment.payCompleted();
    }

    protected Payment fetchPayment(long paymentId) {
        return paymentFindService.fetchPaymentEntityForWebhook(paymentId);
    }

    protected List<Order> doOrders(List<OrderSheet> orders) {
        return orderCreateService.issueOrders(orders);
    }

    protected void doOrderWebHook(NormalOrder normalOrder) {
        orderCompletedService.updateOrder(normalOrder);
    }

    protected void doOrderFail(long paymentId) {
        NormalOrder normalOrder = new NormalOrder(paymentId, 0L, OrderStatus.ORDER_FAILED, false);
        orderFailService.updateOrder(normalOrder);
    }

    protected PaymentBill updatePaymentBill(PgProviderTransDto pgProviderTransDto) {
        return paymentBillQueryService.updatePaymentBill(pgProviderTransDto);
    }

    protected Payment savePayment(BasePayment basePayment) {
        Payment payment = paymentMapper.toEntity(basePayment.getPayAmount());
        return paymentRepository.save(payment);
    }

    protected void savePaymentBill(long paymentId, BasePayment basePayment) {
        PaymentGatewayRequestDto paymentGatewayRequestDto =
                generatePaymentKey(paymentId, new ArrayList<>(), basePayment.getPayAmount());
        PaymentBill paymentBill =
                paymentMapper.toPaymentBill(
                        paymentId, paymentGatewayRequestDto.getPaymentUniqueId(), basePayment);
        paymentBillQueryService.savePaymentBill(paymentBill);
    }

    protected PaymentGatewayRequestDto generatePaymentKey(
            long paymentId, List<Long> orderIds, double orderAmounts) {
        return paymentMapper.toGeneratePaymentKey(paymentId, orderIds, orderAmounts);
    }

    protected void saveShippingAddress(long paymentId, BasePayment basePayment) {
        PaymentSnapShotShippingAddress paymentSnapShotShippingAddress =
                paymentMapper.toSnapShotShippingAddress(paymentId, basePayment);
        paymentSnapShotShippingQueryService.saveShippingAddress(paymentSnapShotShippingAddress);
    }
}
