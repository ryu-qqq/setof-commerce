package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("claimOrder")
public class ClaimOrder extends AbstractUpdateOrder{

    @Length(max = 200, message = "사유는 200자를 넘어갈 수 없습니다.")
    private String changeReason;

    @Length(max = 500, message = "사유는 500자를 넘어갈 수 없습니다.")
    private String changeDetailReason;


    public ClaimOrder(long orderId, @NotNull(message = "주문 상태 값은 필수입니다.") OrderStatus orderStatus, Boolean byPass, String changeReason, String changeDetailReason) {
        super(orderId, orderStatus, byPass);
        this.changeReason = changeReason;
        this.changeDetailReason = changeDetailReason;
    }

    public String getReason(){
        String result =  this.changeReason + " " + this.changeDetailReason;
        if (result.length() > 14) {
            return result.substring(0, 10) + "...";
        }
        return result;
    }


}
