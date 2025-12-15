package com.setof.connectly.module.payment.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payment.dto.payment.PaymentResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaymentSliceMapper {

    CustomSlice<PaymentResponse> toSlice(List<PaymentResponse> paymentResponses, Pageable pageable);
}
