package com.connectly.partnerAdmin.module.external.dto.order.lf;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallUser;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("lfOrder")
public class LfOrder implements ExMallOrder {
    @Override
    public String getBuyerName() {
        return "";
    }

    @Override
    public String getBuyerPhoneNumber() {
        return "";
    }

    @Override
    public OrderStatus getOrderStatus() {
        return null;
    }

    @Override
    public String getBuyerEmail() {
        return "";
    }

    @Override
    public ExMallUser getExMallUser() {
        return null;
    }

    @Override
    public long getExMallOrderId() {
        return 0;
    }

    @Override
    public String getExMallOrderCode() {
        return "";
    }

    @Override
    public List<Long> getProductGroupIds() {
        return List.of();
    }

    @Override
    public long getSiteId() {
        return 0;
    }

    @Override
    public SiteName getSiteName() {
        return null;
    }
}
