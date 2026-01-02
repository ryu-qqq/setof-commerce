package com.connectly.partnerAdmin.module.payment.dto.query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class CreatePayment extends AbstractPayment{

    protected long userId;
    protected List<OrderSheet> orders;
    protected SiteName siteName;
    private String paymentUniqueId;
    private LocalDateTime paymentDate;



    @Override
    public List<OrderSheet> getOrders() {
        return new ArrayList<>(orders);
    }




}
