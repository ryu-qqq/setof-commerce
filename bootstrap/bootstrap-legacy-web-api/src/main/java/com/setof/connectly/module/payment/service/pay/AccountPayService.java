package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.order.dto.query.NormalOrder;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.service.query.OrderCreateService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderCompletedService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderFailService;
import com.setof.connectly.module.payment.dto.payment.CreatePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePaymentInCart;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.entity.VBankAccount;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotRefundAccount;
import com.setof.connectly.module.payment.enums.PaymentMethodGroup;
import com.setof.connectly.module.payment.mapper.PaymentMapper;
import com.setof.connectly.module.payment.repository.PaymentRepository;
import com.setof.connectly.module.payment.repository.account.PaymentSnapShotRefundAccountRepository;
import com.setof.connectly.module.payment.service.bill.query.PaymentBillQueryService;
import com.setof.connectly.module.payment.service.pay.fetch.PaymentFindService;
import com.setof.connectly.module.payment.service.snapshot.PaymentSnapShotShippingQueryService;
import com.setof.connectly.module.payment.service.vbank.query.VBankQueryService;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.service.account.fetch.RefundAccountFindService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AccountPayService extends AbstractPayQueryService {

    private final VBankQueryService vBankQueryService;
    private final RefundAccountFindService refundAccountFindService;
    private final PaymentSnapShotRefundAccountRepository paymentSnapShotRefundAccountRepository;

    public AccountPayService(
            PaymentFindService paymentFindService,
            PaymentRepository paymentRepository,
            PaymentBillQueryService paymentBillQueryService,
            PaymentMapper paymentMapper,
            PaymentSnapShotShippingQueryService paymentSnapShotShippingQueryService,
            OrderCompletedService<? extends OrderSnapShot> orderCompletedService,
            OrderFailService orderFailService,
            OrderCreateService orderCreateService,
            StockReservationService stockReservationService,
            VBankQueryService vBankQueryService,
            RefundAccountFindService refundAccountFindService,
            PaymentSnapShotRefundAccountRepository paymentSnapShotRefundAccountRepository) {
        super(
                paymentFindService,
                paymentRepository,
                paymentBillQueryService,
                paymentMapper,
                paymentSnapShotShippingQueryService,
                orderCompletedService,
                orderFailService,
                orderCreateService,
                stockReservationService);
        this.vBankQueryService = vBankQueryService;
        this.refundAccountFindService = refundAccountFindService;
        this.paymentSnapShotRefundAccountRepository = paymentSnapShotRefundAccountRepository;
    }

    @Override
    public PaymentGatewayRequestDto doPay(CreatePayment payment) {
        PaymentGatewayRequestDto paymentGatewayRequestDto = super.doPay(payment);
        saveRefundAccount(paymentGatewayRequestDto.getPaymentId());
        return paymentGatewayRequestDto;
    }

    @Override
    public PaymentGatewayRequestDto doPayInCart(CreatePaymentInCart payment) {
        PaymentGatewayRequestDto paymentGatewayRequestDto = super.doPayInCart(payment);
        saveRefundAccount(paymentGatewayRequestDto.getPaymentId());
        return paymentGatewayRequestDto;
    }

    @Override
    public Optional<PaymentBill> doPayWebHook(PgProviderTransDto pgProviderTransDto) {
        if (pgProviderTransDto.getPaymentStatus().isProcessing()) {
            Optional<PaymentBill> paymentBill = Optional.of(updatePaymentBill(pgProviderTransDto));
            paymentBill.ifPresent(
                    p -> {
                        NormalOrder normalOrder =
                                new NormalOrder(
                                        p.getPaymentId(), 0L, OrderStatus.ORDER_PROCESSING, true);
                        doOrderWebHook(normalOrder);
                        saveVBankAccount(p.getUserId(), pgProviderTransDto);
                    });
            return paymentBill;
        }

        NormalOrder normalOrder =
                new NormalOrder(
                        pgProviderTransDto.getPaymentId(), 0L, OrderStatus.ORDER_COMPLETED, false);
        doOrderWebHook(normalOrder);
        payCompleted(pgProviderTransDto.getPaymentId());
        return Optional.of(updatePaymentBill(pgProviderTransDto));
    }

    @Override
    public void doPayRefund(
            PgProviderTransDto pgProviderTransDto, RefundOrderSheet refundOrderSheet) {
        RefundAccountInfo refundAccountInfo =
                refundAccountFindService.fetchRefundAccount(pgProviderTransDto.getPaymentId());
        refundOrderSheet.setRefundAccountInfo(refundAccountInfo);
        super.doPayRefund(pgProviderTransDto, refundOrderSheet);
    }

    private void saveVBankAccount(long userId, PgProviderTransDto pgProviderTransDto) {
        VBankAccount vBankAccount = pgProviderTransDto.getVBankAccount();
        vBankAccount.setUserId(userId);
        vBankQueryService.saveVBankAccount(vBankAccount);
    }

    @Override
    public PaymentMethodGroup getPaymentMethodGroup() {
        return PaymentMethodGroup.ACCOUNT;
    }

    private void saveRefundAccount(long paymentId) {
        RefundAccountInfo refundAccountInfo = refundAccountFindService.fetchRefundAccountInfo();
        PaymentSnapShotRefundAccount paymentSnapShotRefundAccount =
                new PaymentSnapShotRefundAccount(paymentId, refundAccountInfo);
        paymentSnapShotRefundAccountRepository.save(paymentSnapShotRefundAccount);
    }
}
