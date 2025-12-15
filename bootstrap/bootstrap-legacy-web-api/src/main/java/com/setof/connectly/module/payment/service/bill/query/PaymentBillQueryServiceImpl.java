package com.setof.connectly.module.payment.service.bill.query;

import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.repository.bill.PaymentBillRepository;
import com.setof.connectly.module.payment.service.bill.fetch.PaymentBillFindService;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentBillQueryServiceImpl implements PaymentBillQueryService {

    private final PaymentBillRepository paymentBillRepository;
    private final PaymentBillFindService paymentBillFindService;

    public void savePaymentBill(PaymentBill paymentBill) {
        paymentBillRepository.save(paymentBill);
    }

    @Override
    public PaymentBill updatePaymentBill(PgProviderTransDto pgProviderTransDto) {
        long paymentId = pgProviderTransDto.getPaymentId();
        PaymentBill paymentBill = paymentBillFindService.fetchPaymentBillEntity(paymentId);
        paymentBill.processWebHook(pgProviderTransDto);
        return paymentBill;
    }
}
