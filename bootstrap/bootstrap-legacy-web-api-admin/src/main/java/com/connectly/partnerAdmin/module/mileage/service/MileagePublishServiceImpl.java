package com.connectly.partnerAdmin.module.mileage.service;

import com.connectly.partnerAdmin.module.mileage.entity.Mileage;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.entity.embedded.PaymentDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class MileagePublishServiceImpl implements MileagePublishService {

    private final MileageFetchService mileageFetchService;

    @Override
    public void publicMileage(Order order){
        PaymentDetails paymentDetails = order.getPayment().getPaymentDetails();
        if(paymentDetails.getSiteName().isOurMall()){
            List<Mileage> mileages = mileageFetchService.fetchMileageEntities(order.getId());
            mileages.forEach(Mileage::approved);
        }
    }

}
