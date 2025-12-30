package com.connectly.partnerAdmin.module.product.dto.query;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateDisplayYn {

    private Yn displayYn;
}
