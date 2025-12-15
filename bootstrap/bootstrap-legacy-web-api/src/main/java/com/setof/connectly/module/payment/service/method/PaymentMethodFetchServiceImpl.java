package com.setof.connectly.module.payment.service.method;

import com.setof.connectly.module.payment.dto.paymethod.PayMethodResponse;
import com.setof.connectly.module.payment.repository.paymethod.PaymentMethodFindRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentMethodFetchServiceImpl implements PaymentMethodFindService {

    private final PaymentMethodFindRepository paymentMethodFindRepository;

    @Cacheable(cacheNames = "payMethods")
    @Override
    public List<PayMethodResponse> fetchPayMethods() {
        return paymentMethodFindRepository.fetchPayMethods();
    }
}
