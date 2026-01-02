package com.connectly.partnerAdmin.module.external.dto.order.oco;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OcoClaimOrderItem {

    private long pid;
    private long oid;
    private long ocid;
    private String optionItem;
}
