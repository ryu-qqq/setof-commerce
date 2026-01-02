package com.connectly.partnerAdmin.module.external.dto.order.shein;

import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("sheInOrder")
public class SheInOrder  {
//implements ExMallOrder, ExMallOrderProduct
    private SiteName siteName;
    private long productGroupId;
    private String optionName;
    private int quantity;
    private String userName;
    private String phoneNumber;
    private String email;

}
