package com.setof.connectly.module.payment.dto.payment;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FailPayment {

    private String paymentUniqueId;
    private List<Long> cartIds;

    public List<Long> getCartIds() {
        if (cartIds != null) {
            if (cartIds.isEmpty()) return new ArrayList<>();
        }
        return cartIds;
    }
}
