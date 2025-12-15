package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.service.query.OrderCreateService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderCompletedService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderFailService;
import com.setof.connectly.module.payment.enums.PaymentMethodGroup;
import com.setof.connectly.module.payment.mapper.PaymentMapper;
import com.setof.connectly.module.payment.repository.PaymentRepository;
import com.setof.connectly.module.payment.service.bill.query.PaymentBillQueryService;
import com.setof.connectly.module.payment.service.pay.fetch.PaymentFindService;
import com.setof.connectly.module.payment.service.snapshot.PaymentSnapShotShippingQueryService;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import org.springframework.stereotype.Service;

@Service
public class CardPayService extends AbstractPayQueryService {

    public CardPayService(
            PaymentFindService paymentFindService,
            PaymentRepository paymentRepository,
            PaymentBillQueryService paymentBillQueryService,
            PaymentMapper paymentMapper,
            PaymentSnapShotShippingQueryService paymentSnapShotShippingQueryService,
            OrderCompletedService<? extends OrderSnapShot> orderCompletedService,
            OrderFailService orderFailService,
            OrderCreateService orderCreateService,
            StockReservationService stockReservationService) {
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
    }

    @Override
    public PaymentMethodGroup getPaymentMethodGroup() {
        return PaymentMethodGroup.CARD;
    }
}
