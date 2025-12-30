package com.connectly.partnerAdmin.module.order.filter;

import com.connectly.partnerAdmin.auth.validator.AuthorityValidate;
import com.connectly.partnerAdmin.module.common.enums.PeriodType;
import com.connectly.partnerAdmin.module.common.filter.RoleFilter;
import com.connectly.partnerAdmin.module.common.filter.SearchAndDateFilter;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AuthorityValidate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderFilter extends SearchAndDateFilter implements RoleFilter {

    private Long lastDomainId;
    private List<OrderStatus> orderStatusList = new ArrayList<>();
    @NotNull(message = "periodType 은 빈 값일 수 없습니다.")
    private PeriodType periodType;
    private Long sellerId;


    public boolean isSettlement(){
        return this.periodType.isSettlement();
    }

    public boolean isHistory(){
        return this.periodType.isHistory();
    }

    @Override
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

}
