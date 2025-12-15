package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.auth.dto.UserPrincipal;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.service.query.OrderCreateService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderCompletedService;
import com.setof.connectly.module.order.service.query.update.strategy.OrderFailService;
import com.setof.connectly.module.payment.dto.payment.CreatePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePaymentInCart;
import com.setof.connectly.module.payment.dto.payment.PaymentGatewayRequestDto;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.payment.enums.PaymentChannel;
import com.setof.connectly.module.payment.enums.PaymentMethodGroup;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import com.setof.connectly.module.payment.mapper.PaymentMapper;
import com.setof.connectly.module.payment.repository.PaymentRepository;
import com.setof.connectly.module.payment.service.bill.query.PaymentBillQueryService;
import com.setof.connectly.module.payment.service.pay.fetch.PaymentFindService;
import com.setof.connectly.module.payment.service.snapshot.PaymentSnapShotShippingQueryService;
import com.setof.connectly.module.portone.dto.PortOneTransDto;
import com.setof.connectly.module.portone.enums.PortOnePaymentStatus;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import com.setof.connectly.module.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MileagePayService extends AbstractPayQueryService {

    // 자기 자신을 주입하는 순환 의존성 제거

    public MileagePayService(
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
    public PaymentGatewayRequestDto doPay(CreatePayment payment) {
        PaymentGatewayRequestDto paymentGatewayRequestDto = super.doPay(payment);
        PortOneTransDto portOneTransDto = mileageTransDto(paymentGatewayRequestDto.getPaymentId());
        this.doPayWebHook(portOneTransDto);
        return paymentGatewayRequestDto;
    }

    @Override
    public PaymentGatewayRequestDto doPayInCart(CreatePaymentInCart payment) {
        PaymentGatewayRequestDto paymentGatewayRequestDto = super.doPayInCart(payment);
        PortOneTransDto portOneTransDto = mileageTransDto(paymentGatewayRequestDto.getPaymentId());
        this.doPayWebHook(portOneTransDto);
        return paymentGatewayRequestDto;
    }

    @Override
    public PaymentMethodGroup getPaymentMethodGroup() {
        return PaymentMethodGroup.MILEAGE;
    }

    private PortOneTransDto mileageTransDto(long paymentId) {
        UserPrincipal user = SecurityUtils.getAuthentication();
        return PortOneTransDto.builder()
                .paymentId(paymentId)
                .payMethod(PaymentMethodEnum.MILEAGE)
                .paymentChannel(PaymentChannel.PC)
                .receiptUrl("")
                .portOnePaymentStatus(PortOnePaymentStatus.paid)
                .cardName("")
                .cardNumber("")
                .payAmount(0)
                .buyerInfo(new BuyerInfo(user.getName(), "", user.getPhoneNumber()))
                .build();
    }
}
