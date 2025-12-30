package com.connectly.partnerAdmin.module.discount.dto.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUseDiscount {
    List<Long> discountPolicyIds;
    public Yn activeYn;

    @Builder
    public UpdateUseDiscount(List<Long> discountPolicyIds, Yn activeYn) {
        this.discountPolicyIds = discountPolicyIds;
        this.activeYn = activeYn;
    }


    public boolean isActive(){
        return activeYn.isYes();
    }
}
