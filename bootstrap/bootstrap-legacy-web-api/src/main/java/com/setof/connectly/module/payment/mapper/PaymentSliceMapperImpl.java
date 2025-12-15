package com.setof.connectly.module.payment.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.mapper.AbstractGeneralSliceMapper;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PaymentSliceMapperImpl extends AbstractGeneralSliceMapper<PaymentResponse>
        implements PaymentSliceMapper {

    @Override
    public CustomSlice<PaymentResponse> toSlice(
            List<PaymentResponse> paymentResponses, Pageable pageable) {
        return super.toSlice(paymentResponses, pageable);
    }
}
