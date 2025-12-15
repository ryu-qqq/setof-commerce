package com.setof.connectly.module.product.enums.delivery;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnMethod implements EnumType {
    RETURN_SELLER("판매자 수거"),
    NOT_PROCEED_RETURN("진행안함"),
    RETURN_CONSUMER("구매자 직접 반송"),
    REFER_DETAIL("상세 정보 참고");

    private final String displayName;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return displayName;
    }
}
