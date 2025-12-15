package com.setof.connectly.module.mileage.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.dto.query.OrderAmountDto;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PendingMileageQueryDto {

    private UserGradeEnum userGrade;
    private Set<OrderAmountDto> orderAmounts;

    @QueryProjection
    public PendingMileageQueryDto(UserGradeEnum userGrade, Set<OrderAmountDto> orderAmounts) {
        this.userGrade = userGrade;
        this.orderAmounts = orderAmounts;
    }

    public double getTotalOrderAmount() {
        return orderAmounts.stream().mapToLong(OrderAmountDto::getOrderAmount).sum();
    }
}
